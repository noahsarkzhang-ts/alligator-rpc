package org.noahsark.rpc.socket.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.Connection;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RpcPromise;
import org.noahsark.rpc.socket.event.ClientConnectionSuccessEvent;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.noahsark.rpc.socket.session.ConnectionManager;
import org.noahsark.rpc.socket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 客户端
 *
 * @author zhangxt
 * @date 2021/3/14
 */
public abstract class AbstractRemotingClient implements RemotingClient {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingClient.class);

    /**
     * 参数配置
     */
    private Map<RemoteOption<?>, Object> clientOptions = new HashMap<>();

    /**
     * 服务器信息
     */
    protected ServerInfo current;

    /**
     * 服务器管理器，用于服务器切换
     */
    protected ServerManager serverManager;

    /**
     * 客户端工作队列
     */
    private WorkQueue workQueue;

    /**
     * 连接管理器
     */
    protected ConnectionManager connectionManager;

    /**
     * 客户端连接对象
     */
    protected Connection connection;

    /**
     * 客户端会话对象
     */
    private Session session;

    /**
     * Netty 事件循环对象
     */
    private EventLoopGroup group;

    /**
     * Netty 启动器
     */
    private Bootstrap bootstrap;

    /**
     * 清理线程，清理过期的请求
     */
    private ClientClearThread clearThread;

    /**
     * 通用任务线程，如重连任务
     */
    private CommonServiceThread commonThread;

    public AbstractRemotingClient() {
    }

    public AbstractRemotingClient(String url) {

        ServerInfo serverInfo = convert(url);

        List<ServerInfo> servers = new ArrayList<>();
        servers.add(serverInfo);

        serverManager = new ServerManager(servers);

        this.current = serverManager.toggleServer();

        init();
    }

    public AbstractRemotingClient(List<String> urls) {
        List<ServerInfo> servers = new ArrayList<>();

        urls.stream().forEach(url -> servers.add(this.convert(url)));

        serverManager = new ServerManager(servers);
        this.current = serverManager.toggleServer();

        init();
    }

    public AbstractRemotingClient(String host, int port) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(host + ":" + port);
        serverInfo.setHost(host);
        serverInfo.setPort(port);

        List<ServerInfo> servers = new ArrayList<>();
        servers.add(serverInfo);

        serverManager = new ServerManager(servers);

        this.current = serverManager.toggleServer();

        init();
    }

    protected void init() {
        try {
            preInit();

            if (!this.existOption(RemoteOption.THREAD_NUM_OF_QUEUE)) {
                this.option(RemoteOption.THREAD_NUM_OF_QUEUE, 5);
            }

            initWorkQueue();

            connection = new Connection();

            clearThread = new ClientClearThread();
            clearThread.start();

            commonThread = new CommonServiceThread();
            commonThread.start();

            group = new NioEventLoopGroup();

            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(getChannelInitializer(this));

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);
        }
    }

    private void initWorkQueue() {
        this.workQueue = new WorkQueue();
        this.workQueue.setMaxQueueNum(this.option(RemoteOption.CAPACITY_OF_QUEUE));
        this.workQueue.setMaxThreadNum(this.option(RemoteOption.THREAD_NUM_OF_QUEUE));

        this.workQueue.init();
    }

    protected abstract ChannelInitializer<SocketChannel> getChannelInitializer(
            AbstractRemotingClient server);

    public <T> void option(RemoteOption<T> option, T value) {
        this.clientOptions.put(option, value);

    }

    public <T> T option(RemoteOption<T> option) {
        return this.clientOptions.containsKey(option) ? (T) this.clientOptions.get(option)
                : option.getDefaultValue();
    }

    public <T> boolean existOption(RemoteOption<T> option) {
        return this.clientOptions.containsKey(option);
    }

    @Override
    public void connect() {

        Runnable runnable = () -> internalConnect();

        commonThread.offer(runnable);
    }

    @Override
    public Session connectAndSession() {

        internalConnect();

        return session;
    }

    private void internalConnect() {
        synchronized (bootstrap) {
            ChannelFuture future;

            try {
                future = bootstrap
                        .connect(current.getHost(), current.getPort());
                future.addListener(getConnectionListener());

                // log.info("connect is Done: {}", future.isDone());

                Channel channel = future.sync().channel();
                connection.setChannel(channel);

                session = Session.getOrCreatedSession(connection);
            } catch (InterruptedException ex) {
                log.warn("catch an exception.", ex);
            }


        }
    }

    @Override
    public void shutdown() {
        if (this.connection != null) {
            connection.close();
        }
        group.shutdownGracefully();

        if (clearThread != null) {
            clearThread.shutdown();
        }

    }

    @Override
    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();
        request.setRequestId(this.connection.nextId());

        promise.invoke(this.connection, request, commandCallback, timeoutMillis);

        return promise;
    }

    public Object invokeSync(Request request, int timeoutMillis) {
        RpcPromise promise = new RpcPromise();
        request.setRequestId(this.connection.nextId());

        Object result = promise.invokeSync(this.connection, request, timeoutMillis);

        return result;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    @Override
    public void toggleServer() {
        ServerInfo serverInfo = serverManager.toggleServer();

        if (serverInfo != null) {
            this.current = serverInfo;

            log.info("toggle to new server: {} : {}",
                    serverInfo.getHost(), serverInfo.getPort());

            this.connect();
        } else {

            log.info("No server to toggle,reset servers.");
            serverManager.reset();

            final EventLoop eventLoop = this.connection.getChannel().eventLoop();

            eventLoop.schedule(() -> {
                log.info("Try Reconnecting ...");

                this.toggleServer();
            }, 3000, TimeUnit.MILLISECONDS);
        }

    }

    public WorkQueue getWorkQueue() {
        return this.workQueue;
    }

    @Override
    public ServerInfo getServerInfo() {
        return this.current;
    }

    protected abstract void preInit();

    public abstract ServerInfo convert(String url);

    public void registerProcessor(AbstractProcessor<?> processor) {
        processor.register();
    }

    public void unregisterProcessor(AbstractProcessor<?> processor) {
        processor.unregister();
    }

    private ChannelFutureListener getConnectionListener() {
        return future -> {
            if (!future.isSuccess()) {
                future.channel().pipeline().fireChannelInactive();
            } else {

                log.info("Connect Completely!!!");
                EventBus.getInstance().post(new ClientConnectionSuccessEvent(null));
            }
        };
    }


}

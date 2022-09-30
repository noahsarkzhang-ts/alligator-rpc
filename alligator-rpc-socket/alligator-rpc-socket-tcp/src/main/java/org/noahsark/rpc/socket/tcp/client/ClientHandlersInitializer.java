package org.noahsark.rpc.socket.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.socket.handler.ClientBizServiceHandler;
import org.noahsark.rpc.socket.handler.ClientIdleStateTrigger;
import org.noahsark.rpc.socket.remote.AbstractRemotingClient;
import org.noahsark.rpc.socket.remote.ReconnectHandler;
import org.noahsark.rpc.socket.tcp.handler.CommandDecoder;
import org.noahsark.rpc.socket.tcp.handler.CommandEncoder;
import org.noahsark.rpc.socket.tcp.handler.PongHandler;

/**
 * 客户端处理器初始化类
 *
 * @author zhangxt
 * @date 2021/3/7
 */
public class ClientHandlersInitializer extends ChannelInitializer<SocketChannel> {

    private AbstractRemotingClient client;

    private ReconnectHandler reconnectHandler;

    private WorkQueue workQueue;

    public ClientHandlersInitializer(AbstractRemotingClient client) {
        this.client = client;

        this.workQueue = client.getWorkQueue();

        reconnectHandler = new ReconnectHandler(this.client);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 15, 0));
        pipeline.addLast(new ClientIdleStateTrigger(this.client));
        pipeline.addLast(reconnectHandler);

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new CommandDecoder());
        pipeline.addLast(new CommandEncoder());
        pipeline.addLast(new PongHandler(client));
        pipeline.addLast(new ClientBizServiceHandler(this.workQueue));
    }
}

package org.noahsark.rpc.socket.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.socket.heartbeat.CommonHeartbeatFactory;
import org.noahsark.rpc.socket.heartbeat.PingPayloadGenerator;
import org.noahsark.rpc.socket.remote.AbstractRemotingClient;
import org.noahsark.rpc.socket.remote.ExponentialBackOffRetry;
import org.noahsark.rpc.socket.remote.ServerInfo;
import org.noahsark.rpc.socket.session.ConnectionManager;

import java.util.List;

/**
 * TCP 客户端
 * @author zhangxt
 * @date 2021/3/7 17:23
 */
public class TcpClient extends AbstractRemotingClient {

    public TcpClient(String host, int port) {
        super(host, port);
    }

    public TcpClient(String url) {
        super(url);
    }

    public TcpClient(List<String> urls) {
        super(urls);
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(
        AbstractRemotingClient server) {
        return new ClientHandlersInitializer(this);
    }

    @Override
    protected void preInit() {

        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.setHeartbeatFactory(new CommonHeartbeatFactory());
        connectionManager
            .setRetryPolicy(new ExponentialBackOffRetry(1000, 4, 60 * 1000));

        this.connectionManager = connectionManager;

    }

    @Override
    public ServerInfo convert(String url) {

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(url);

        String[] parts = url.split(":");

        serverInfo.setHost(parts[0]);
        serverInfo.setPort(Integer.valueOf(parts[1]));

        return serverInfo;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    @Override
    public void ping() {

        this.sendMessage((RpcCommand) this.connectionManager.getHeartbeatFactory().getPing());
    }

    @Override
    public void sendMessage(RpcCommand command) {
        this.connection.getChannel().writeAndFlush(command);
    }

    public void registerPingPayloadGenerator(PingPayloadGenerator payloadGenerator) {
        if (this.connectionManager != null) {
            this.connectionManager.getHeartbeatFactory()
                .setPayloadGenerator(payloadGenerator);
        }
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 2222);
        tcpClient.connect();
    }

}

package org.noahsark.rpc.socket.remote;

import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcPromise;
import org.noahsark.rpc.socket.session.ConnectionManager;

/**
 * 客户端接口
 *
 * @author zhangxt
 * @date 2021/03/07
 */
public interface RemotingClient {

    /**
     * 连接服务器
     */
    void connect();

    /**
     * 关闭连接
     */
    void shutdown();

    /**
     * 发送 ping
     */
    void ping();

    /**
     * 切换服务器
     */
    void toggleServer();

    /**
     * 获取服务器信息
     * @return 服务器信息
     */
    ServerInfo getServerInfo();

    /**
     * 获取连接管理
     * @return 连接管理器
     */
    ConnectionManager getConnectionManager();

    /**
     * 发送数据（响应）
     * @param command 响应
     */
    void sendMessage(RpcCommand command);

    /**
     * 异步发送请求
     * @param request 请求
     * @param commandCallback 回调
     * @param timeoutMillis 超时时间
     * @return RpcPromise
     */
    RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis);

}

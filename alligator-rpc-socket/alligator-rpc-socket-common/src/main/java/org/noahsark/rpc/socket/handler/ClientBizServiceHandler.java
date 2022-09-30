package org.noahsark.rpc.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.remote.Connection;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.socket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端通用处理类
 * @author zhangxt
 * @date 2021/4/3
 */
public class ClientBizServiceHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(ClientBizServiceHandler.class);

    private WorkQueue workQueue;

    public ClientBizServiceHandler() {
    }

    public ClientBizServiceHandler(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {

        Connection connection = ctx.channel().attr(Connection.CONNECTION).get();
        if (connection == null) {
            log.warn("No connection,requestId : {}", msg.getRequestId());
            return;
        }
        log.info("receive msg: {}", msg);

        try {
            Session session = Session.getOrCreatedSession(connection);

            if (msg.getType() == RpcCommandType.REQUEST
                    || msg.getType() == RpcCommandType.REQUEST_ONEWAY) {
                RequestHandler.processRequest(ctx, msg, workQueue, session);
            } else {
                RequestHandler.processResponse(connection, msg);
            }

        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);
        }
    }
}

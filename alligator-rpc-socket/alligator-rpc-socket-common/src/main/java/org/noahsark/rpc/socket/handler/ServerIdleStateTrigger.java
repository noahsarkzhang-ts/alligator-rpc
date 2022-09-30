package org.noahsark.rpc.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.noahsark.rpc.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 在规定时间内未收到客户端的任何数据包, 将主动断开该连接
 * @author zhangxt
 * @date 2021/5/13
 */
public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ServerIdleStateTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {

                log.info("client timeout!!!");
                // 连接超过，删除会话
                SessionManager.getInstance().disconnect(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

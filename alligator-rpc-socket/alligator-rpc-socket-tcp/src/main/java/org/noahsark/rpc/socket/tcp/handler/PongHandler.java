package org.noahsark.rpc.socket.tcp.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.remote.RemotingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pong 处理器
 * @author zhangxt
 * @date 2021/4/3
 */
@ChannelHandler.Sharable
public class PongHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(PongHandler.class);

    private RemotingClient client;

    public PongHandler(RemotingClient client) {
        this.client = client;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {
        if (msg.getBiz() == 1 && msg.getCmd() == 1
            && msg.getType() == RpcCommandType.RESPONSE) {

            byte [] result = (byte[]) msg.getPayload();
            String json = new String(result, CharsetUtil.UTF_8);

            // 清空心跳计数
            client.getConnectionManager().getHeartbeatStatus().reset();

            log.info("receive a pong message:{}", JsonUtils.toJson(JsonUtils
                .fromJsonObject(json,Void.class)));

            return;
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}

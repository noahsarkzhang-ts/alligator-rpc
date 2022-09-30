package org.noahsark.rpc.socket.tcp.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.noahsark.rpc.common.remote.MultiRequest;
import org.noahsark.rpc.common.remote.RpcCommand;

import java.util.List;

/**
 * 编码处理器
 * @author zhangxt
 * @date 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToMessageEncoder<RpcCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out)
        throws Exception {

        if (msg instanceof MultiRequest) {
            out.add(MultiRequest.encode(ctx, msg));
        } else {
            out.add(RpcCommand.encode(ctx, msg));
        }

    }
}

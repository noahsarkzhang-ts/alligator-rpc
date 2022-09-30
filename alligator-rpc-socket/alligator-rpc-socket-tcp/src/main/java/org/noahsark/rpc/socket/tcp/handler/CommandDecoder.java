package org.noahsark.rpc.socket.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.noahsark.rpc.common.remote.MultiRequest;
import org.noahsark.rpc.common.remote.RpcCommand;

import java.util.List;

/**
 * 解码处理器
 * @author zhangxt
 * @date 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
        throws Exception {

        msg.markReaderIndex();
        short headSize = msg.readShort();
        msg.resetReaderIndex();

        if (RpcCommand.RPC_COMMAND_SIZE == headSize) {
            out.add(RpcCommand.decode(msg));
        } else {
            out.add(MultiRequest.decode(msg));
        }


    }
}

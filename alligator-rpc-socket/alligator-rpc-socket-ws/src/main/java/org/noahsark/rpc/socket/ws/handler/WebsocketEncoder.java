package org.noahsark.rpc.socket.ws.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;

import java.util.List;

/**
 * 编码处理器
 * @author zhangxt
 * @date 2021/4/3
 */
public class WebsocketEncoder extends MessageToMessageEncoder<RpcCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out)
            throws Exception {

        Object payload = msg.getPayload();
        if (payload instanceof byte[]) {
            String sPayload = new String((byte[]) payload);
            JsonObject fPaylpad = new JsonParser().parse(sPayload).getAsJsonObject();

            msg.setPayload(fPaylpad);
        }

        out.add(new TextWebSocketFrame(JsonUtils.toJson(msg)));
    }
}

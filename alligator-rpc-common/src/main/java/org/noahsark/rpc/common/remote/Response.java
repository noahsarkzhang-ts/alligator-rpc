package org.noahsark.rpc.common.remote;

import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.constant.RpcCommandVer;
import org.noahsark.rpc.common.constant.SerializerType;

import java.io.Serializable;

/**
 * 响应类
 *
 * @author zhangxt
 * @date 2021/3/13
 */
public class Response extends RpcCommand implements Serializable {

    public Response() {

        this.setVer(RpcCommandVer.V1);
        this.setSerializer(SerializerType.JSON);
    }

    public Response(Builder builder) {

        super(builder.commandBuilder);

        this.setVer(RpcCommandVer.V1);

    }

    public static class Builder {

        private RpcCommand.Builder commandBuilder = new RpcCommand.Builder();

        public Builder requestId(int requestId) {
            this.commandBuilder.requestId(requestId);
            return this;
        }

        public Builder biz(int biz) {
            this.commandBuilder.biz(biz);
            return this;
        }

        public Builder cmd(int cmd) {
            this.commandBuilder.cmd(cmd);
            return this;
        }

        public Builder type(byte type) {
            this.commandBuilder.type(type);
            return this;
        }

        public Builder ver(byte ver) {
            this.commandBuilder.ver(ver);
            return this;
        }

        public Builder serializer(byte serializer) {
            this.commandBuilder.serializer(serializer);
            return this;
        }

        public Builder payload(Object payload) {
            this.commandBuilder.payload(payload);
            return this;
        }

        public Response build() {
            this.commandBuilder.ver(RpcCommandVer.V1);

            return new Response(this);
        }
    }

    public static Response buildCommonResponse(RpcCommand request, int code, String message) {

        return buildCommonResponse(request, RpcCommandType.RESPONSE, code, message);
    }

    public static Response buildCommonStream(RpcCommand request, int code, String message) {

        return buildCommonResponse(request, RpcCommandType.STREAM, code, message);
    }

    public static Response buildResponse(RpcCommand request, Object t, int code, String message) {

        return buildResponse(request, RpcCommandType.RESPONSE, t, code, message);
    }

    public static Response buildStream(RpcCommand request, Object t, int code, String message) {

        return buildResponse(request, RpcCommandType.STREAM, t, code, message);
    }

    public static Response buildResponseFromResult(RpcCommand request, Object result) {

        return buildResponseFromResult(request, RpcCommandType.RESPONSE, result);
    }

    public static Response buildStreamFromResult(RpcCommand request, Object result) {

        return buildResponseFromResult(request, RpcCommandType.STREAM, result);
    }

    public static Response buildCommonResponse(RpcCommand request, byte responseType, int code, String message) {
        Result result = new Result.Builder()
                .code(code)
                .message(message)
                .build();

        Response command = new Response.Builder()
                .requestId(request.getRequestId())
                .biz(request.getBiz())
                .cmd(request.getCmd())
                .type(responseType)
                .serializer(request.getSerializer())
                .payload(result)
                .build();

        return command;
    }

    public static Response buildResponse(RpcCommand request, byte responseType, Object t, int code, String message) {

        Result result = new Result.Builder()
                .code(code)
                .message(message)
                .data(t)
                .build();

        Response command = new Response.Builder()
                .requestId(request.getRequestId())
                .biz(request.getBiz())
                .cmd(request.getCmd())
                .type(responseType)
                .serializer(request.getSerializer())
                .payload(result)
                .build();

        return command;
    }

    public static Response buildResponseFromResult(RpcCommand request, byte responseType, Object result) {

        Response command = new Response.Builder()
                .requestId(request.getRequestId())
                .biz(request.getBiz())
                .cmd(request.getCmd())
                .type(responseType)
                .payload(result)
                .build();

        return command;
    }

}

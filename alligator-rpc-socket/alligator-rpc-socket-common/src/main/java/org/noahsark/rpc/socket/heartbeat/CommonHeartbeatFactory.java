package org.noahsark.rpc.socket.heartbeat;

import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.constant.RpcCommandVer;
import org.noahsark.rpc.common.constant.SerializerType;
import org.noahsark.rpc.common.remote.Result;
import org.noahsark.rpc.common.remote.RpcCommand;

/**
 * Tcp 心跳构造器
 * @author zhangxt
 * @date 2021/4/3.
 */
public class CommonHeartbeatFactory implements HeartbeatFactory<RpcCommand> {

    private PingPayloadGenerator payloadGenerator;

    @Override
    public RpcCommand getPing() {

        Object payload;
        if (payloadGenerator != null) {
            payload = payloadGenerator.getPayload();
        } else {
            Ping hearBeat = new Ping();
            hearBeat.setLoad(0);

            payload = hearBeat;
        }

        RpcCommand command = new RpcCommand.Builder()
            .requestId(0)
            .biz(1)
            .cmd(1)
            .type(RpcCommandType.REQUEST)
            .ver(RpcCommandVer.V1)
            .serializer(SerializerType.JSON)
            .payload(payload)
            .build();

        return command;
    }

    @Override
    public PingPayloadGenerator getPayloadGenerator() {
        return this.payloadGenerator;
    }

    @Override
    public void setPayloadGenerator(PingPayloadGenerator payload) {
        this.payloadGenerator = payload;

    }

    public static RpcCommand getPong(RpcCommand ping) {

        Result<Void> result = new Result.Builder<Void>()
            .code(0)
            .message("success")
            .build();

        RpcCommand command = new RpcCommand.Builder()
            .requestId(0)
            .biz(1)
            .cmd(1)
            .type(RpcCommandType.RESPONSE)
            .ver(RpcCommandVer.V1)
            .serializer(SerializerType.JSON)
            .payload(result)
            .build();

        return command;
    }
}

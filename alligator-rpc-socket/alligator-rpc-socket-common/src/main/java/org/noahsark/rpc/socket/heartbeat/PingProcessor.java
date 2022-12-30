package org.noahsark.rpc.socket.heartbeat;

import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ping 消息处理器
 *
 * @author zhangxt
 * @date 2021/4/3
 */
public class PingProcessor extends AbstractProcessor<Ping> {

    private static Logger log = LoggerFactory.getLogger(PingProcessor.class);


    @Override
    protected void execute(Ping request, RpcContext context) {
        log.debug("Receive a ping message: {}", JsonUtils.toJson(request));

        RpcCommand command = CommonHeartbeatFactory.getPong(context.getCommand());

        context.sendResponse(command);
    }

    @Override
    protected Class<Ping> getParamsClass() {
        return Ping.class;
    }

    @Override
    protected int getBiz() {
        return 1;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}

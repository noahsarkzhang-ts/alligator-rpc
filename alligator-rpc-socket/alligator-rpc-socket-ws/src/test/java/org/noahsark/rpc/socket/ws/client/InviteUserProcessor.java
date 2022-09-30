package org.noahsark.rpc.socket.ws.client;

import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcContext;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/13
 */
public class InviteUserProcessor extends AbstractProcessor<InviteInfo> {
    @Override
    protected void execute(InviteInfo request, RpcContext context) {
        context.sendResponse(Response.buildCommonResponse(context.getCommand(),0,"success"));
    }

    @Override
    protected Class<InviteInfo> getParamsClass() {
        return InviteInfo.class;
    }

    @Override
    protected int getBiz() {
        return 300;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}

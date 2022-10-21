package org.noahsark.rpc.mq.rocketmq.processor;

import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.MultiRequest;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.mq.rocketmq.RocketmqTopic;
import org.noahsark.rpc.mq.rocketmq.bean.UserEvent;
import org.noahsark.rpc.mq.rocketmq.bean.UserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户事件处理器
 *
 * @author zhangxt
 * @date 2021/6/27
 */
public class UserEventProcessor extends AbstractProcessor<UserEvent> {

    private static Logger log = LoggerFactory.getLogger(UserEventProcessor.class);


    @Override
    protected void execute(UserEvent request, RpcContext context) {

        log.info("receive user event: {}", request);

        MultiRequest command = (MultiRequest) context.getCommand();

        UserResult userResult = new UserResult(request.getUserId(),(byte) 1);

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(command.getTopic());

        Response response = Response.buildResponse(command, userResult, 0, "success");
        response.setAttachment(topic);

        context.sendResponse(response);
    }

    @Override
    protected Class<UserEvent> getParamsClass() {
        return UserEvent.class;
    }

    @Override
    protected int getBiz() {
        return 3;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}

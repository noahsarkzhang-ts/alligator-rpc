package org.noahsark.mq.rabbitmq;

import org.noahsark.rpc.common.constant.BizServiceType;
import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.constant.RpcCommandVer;
import org.noahsark.rpc.common.constant.SerializerType;
import org.noahsark.rpc.common.remote.MultiRequest;
import org.noahsark.rpc.mq.rabbitmq.RabbitmqProxy;
import org.noahsark.rpc.mq.rabbitmq.RabbitmqTopic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/10/13
 */
public class RabbitmqProxyTest {

    private static final String VHOST = "alligator";

    private static final String URLS = "120.79.235.83:5672";

    private static final String USERNAME = "allen";

    private static final String PASSWORD = "allen147";

    private static final String TOPIC = "user-event";

    private static final String SOURCE_TOPIC = "test";

    public static void main(String[] args) {

        List<RabbitmqTopic> topics = new ArrayList<>();
        RabbitmqTopic topic = new RabbitmqTopic();
        topic.setQueueName(TOPIC);
        topics.add(topic);

        RabbitmqProxy.Builder builder = new RabbitmqProxy.Builder();
        builder.vhost(VHOST);
        builder.urls(URLS);
        builder.username(USERNAME);
        builder.password(PASSWORD);
        builder.topics(topics);

        RabbitmqProxy rabbitmqProxy = builder.build();
        rabbitmqProxy.start();

        sendMessage(rabbitmqProxy);

    }

    public static void sendMessage(RabbitmqProxy mqProxy) {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("100001");
        userInfo.setName("allan");
        userInfo.setServiceId("1001");
        userInfo.setState((byte) 1);

        Set<String> targetIds = new HashSet<>();
        targetIds.add("*");

        MultiRequest multiRequest = new MultiRequest.Builder()
                .biz(BizServiceType.BIZ_CLIENT)
                .cmd(10)
                .serializer(SerializerType.JSON)
                .type(RpcCommandType.REQUEST_ONEWAY)
                .ver(RpcCommandVer.V1)
                .payload(userInfo)
                .topic(SOURCE_TOPIC)
                .targetIds(targetIds)
                .build();


        RabbitmqTopic topic = new RabbitmqTopic();
        topic.setQueueName(TOPIC);
        mqProxy.sendOneway(topic, multiRequest);

    }

    private static class UserInfo implements Serializable {

        private String userId;

        private String name;

        private String serviceId;

        /**
         * ?????????????????????0????????????1?????????
         */
        private byte state;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public byte getState() {
            return state;
        }

        public void setState(byte state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "userId='" + userId + '\'' +
                    ", name='" + name + '\'' +
                    ", serviceId='" + serviceId + '\'' +
                    ", state=" + state +
                    '}';
        }

    }
}

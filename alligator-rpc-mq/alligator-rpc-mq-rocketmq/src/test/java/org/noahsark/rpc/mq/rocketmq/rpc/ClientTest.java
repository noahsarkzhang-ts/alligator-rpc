package org.noahsark.rpc.mq.rocketmq.rpc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.constant.RpcCommandVer;
import org.noahsark.rpc.common.constant.SerializerType;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.MultiRequest;
import org.noahsark.rpc.mq.rocketmq.RocketmqProxy;
import org.noahsark.rpc.mq.rocketmq.RocketmqTopic;
import org.noahsark.rpc.mq.rocketmq.bean.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MQ Client Test
 *
 * @author zhangxt
 * @date 2022/10/14 15:49
 **/
public class ClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClientTest.class);

    private static final String NAME_SRV = "120.79.235.83:9876";

    private static final String CONSUMER_GROUP = "client-consumer-1";

    private static final String PRODUCER_GROUP = "client-produce-1";

    private static final String CLIENT_TOPIC_NAME = "rpc-client-topic";

    private static final String SERVER_TOPIC_NAME = "rpc-server-topic";

    private RocketmqProxy rocketmqProxy;

    @Before
    public void proxyStart() {
        String nameSrv = NAME_SRV;
        String consumerGroup = CONSUMER_GROUP;
        String producerGroup = PRODUCER_GROUP;

        List<RocketmqTopic> topics = new ArrayList<>();

        String topicName = CLIENT_TOPIC_NAME;
        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(topicName);
        topics.add(topic);

        rocketmqProxy = new RocketmqProxy.Builder()
                .topics(topics)
                .consumerGroup(consumerGroup)
                .producerGroup(producerGroup)
                .namesrvAddr(nameSrv)
                .build();

        rocketmqProxy.start();
    }

    @Test
    public void testRequestResponse() {

        UserEvent userEvent = new UserEvent();
        userEvent.setName("allen");
        userEvent.setUserId("10001");
        userEvent.setServiceId("1");
        userEvent.setType((byte)1);
        userEvent.setTimestamp(System.currentTimeMillis());

        MultiRequest multiRequest = new MultiRequest.Builder()
                .biz(3)
                .cmd(1)
                .serializer(SerializerType.JSON)
                .type(RpcCommandType.REQUEST)
                .ver(RpcCommandVer.V1)
                .payload(userEvent)
                .topic(CLIENT_TOPIC_NAME)
                .build();

        LOG.info("send a request: {}", multiRequest);

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(SERVER_TOPIC_NAME);
        rocketmqProxy.sendAsync(topic, multiRequest, new CommandCallback() {
            @Override
            public void callback(Object result, int currentFanout, int fanout) {

                LOG.info("currentFanout:{},fanout:{}", currentFanout, fanout);

                List<Object> results = new ArrayList<>();
                if (result instanceof List) {
                    results = (List<Object>) result;
                } else {
                    results.add(result);
                }

                LOG.info("receive responses size: {}", results.size());

                results.stream().forEach(rs -> {
                    String sPayload = new String((byte[]) rs);
                    JsonObject paylpad = new JsonParser().parse(sPayload).getAsJsonObject();
                    LOG.info("receive a response: {}", paylpad);
                });

            }

            @Override
            public void failure(Throwable cause, int currentFanout, int fanout) {
                LOG.warn("Invoke catch an exception!", cause);
                LOG.info("currentFanout:{},fanout:{}", currentFanout, fanout);

            }
        }, 10000);

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    @After
    public void proxyStop() {
        if (rocketmqProxy != null) {
            rocketmqProxy.shutdown();
        }
    }

}

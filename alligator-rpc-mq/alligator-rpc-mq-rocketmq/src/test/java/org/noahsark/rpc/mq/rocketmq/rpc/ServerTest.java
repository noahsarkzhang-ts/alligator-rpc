package org.noahsark.rpc.mq.rocketmq.rpc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.noahsark.rpc.mq.rocketmq.RocketmqProxy;
import org.noahsark.rpc.mq.rocketmq.RocketmqTopic;
import org.noahsark.rpc.mq.rocketmq.processor.UserEventProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MQ Server Test
 *
 * @author zhangxt
 * @date 2022/10/14 15:50
 **/
public class ServerTest {

    private static final String NAME_SRV = "120.79.235.83:9876";

    private static final String CONSUMER_GROUP = "server-consumer-1";

    private static final String PRODUCER_GROUP = "server-produce-1";

    private static final String CLIENT_TOPIC_NAME = "rpc-client-topic";

    private static final String SERVER_TOPIC_NAME = "rpc-server-topic";

    private RocketmqProxy rocketmqProxy;

    @Before
    public void proxyStart() {
        String nameSrv = NAME_SRV;
        String consumerGroup = CONSUMER_GROUP;
        String producerGroup = PRODUCER_GROUP;

        List<RocketmqTopic> topics = new ArrayList<>();

        String topicName = SERVER_TOPIC_NAME;
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

        UserEventProcessor userEventProcessor = new UserEventProcessor();
        userEventProcessor.register();

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

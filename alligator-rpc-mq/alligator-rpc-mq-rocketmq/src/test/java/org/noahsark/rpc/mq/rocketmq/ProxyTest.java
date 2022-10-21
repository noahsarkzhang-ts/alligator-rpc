package org.noahsark.rpc.mq.rocketmq;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Mq Proxy Test
 *
 * @author zhangxt
 * @date 2022/10/14 17:37
 **/
public class ProxyTest {

    private static final String NAME_SRV = "120.79.235.83:9876";

    private static final String CONSUMER_GROUP = "online-consumer-1";

    private static final String PRODUCER_GROUP = "online-produce-1";

    private static final String TOPIC_NAME = "TopicTest";

    @Test
    public void testClient() {
        String nameSrv = NAME_SRV;
        String consumerGroup = CONSUMER_GROUP;
        String producerGroup = PRODUCER_GROUP;

        List<RocketmqTopic> topics = new ArrayList<>();

        String topicName = TOPIC_NAME;
        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(topicName);
        topics.add(topic);


        RocketmqProxy rocketmqProxy = new RocketmqProxy.Builder()
                .topics(topics)
                .consumerGroup(consumerGroup)
                .producerGroup(producerGroup)
                .namesrvAddr(nameSrv)
                .build();

        rocketmqProxy.start();
    }
}

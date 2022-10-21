package org.noahsark.rpc.mq.rocketmq;

import org.apache.rocketmq.client.log.ClientLogger;
import org.junit.Test;

/**
 * Created by hadoop on 2021/5/2.
 */
public class ProducerTest {

    @Test
    public void testProducer() throws Exception {

        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J,"true");

        RocketmqProducer producer = new RocketmqProducer("Test-Group",
            "120.79.235.83:9876");

        producer.start();

        RocketmqMessage message = new RocketmqMessage();
        message.setTopic("t-alligator-reg");
        message.setContent("hello rocketmq".getBytes());

        producer.send(message);



    }
}

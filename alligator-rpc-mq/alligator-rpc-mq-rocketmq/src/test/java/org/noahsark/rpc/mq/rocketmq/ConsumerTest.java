package org.noahsark.rpc.mq.rocketmq;

import org.apache.rocketmq.client.log.ClientLogger;
import org.junit.Test;
import org.noahsark.rpc.mq.common.DefaultmqMessageListener;
import org.noahsark.rpc.mq.common.MessageListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by hadoop on 2021/5/2.
 */
public class ConsumerTest {

    @Test
    public void testConsumer() throws Exception{

        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J,"true");

        RocketmqConsumer consumer = new RocketmqConsumer("test-consumer",
            "120.79.235.83:9876");

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic("t-alligator-reg");
        topic.setTag("*");

        consumer.subscribe(topic);

        consumer.registerMessageListener(new MessageListener(){

            @Override
            public boolean consumeMessage(byte[] message) {

                System.out.println("message = " + new String(message));

                return true;
            }
        });

        consumer.start();

        TimeUnit.SECONDS.sleep(60);
    }

}

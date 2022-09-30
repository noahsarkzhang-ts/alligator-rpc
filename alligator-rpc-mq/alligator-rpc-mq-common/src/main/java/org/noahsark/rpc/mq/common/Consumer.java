package org.noahsark.rpc.mq.common;

import java.util.List;

public interface Consumer<T extends Topic> {

    void registerMessageListener(MessageListener listener);

    void subscribe(T topic);

    void subscribe(List<T> topics);

    void start();

    void shutdown();


}
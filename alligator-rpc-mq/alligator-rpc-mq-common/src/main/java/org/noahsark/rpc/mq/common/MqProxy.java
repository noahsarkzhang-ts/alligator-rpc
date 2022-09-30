package org.noahsark.rpc.mq.common;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.PromisHolder;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RpcPromise;

/**
 * MQ 代理接口
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public interface MqProxy {

    PromisHolder getPromiseHolder();

    ChannelHolder getChannelHolder();

    RpcPromise sendAsync(Topic topic, Request request, CommandCallback commandCallback, int timeoutMillis);

    Object sendSync(Topic topic, Request request, int timeoutMillis);

    void sendOneway(Topic topic, Request request);

    void start();

    void shutdown();
}

package org.noahsark.rpc.mq.common;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.PromisHolder;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RpcPromise;

/**
 * MQ代理抽象类
 *
 * @author zhangxt
 * @date 22021/5/3
 */
public abstract class AbstractMqProxy implements MqProxy {

    protected PromisHolder promiseHolder;

    protected ChannelHolder channelHolder;

    protected abstract void initHolder();

    @Override
    public RpcPromise sendAsync(Topic topic, Request request,
                                CommandCallback commandCallback,
                                int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);
        promise.invoke(this.promiseHolder, request, commandCallback, timeoutMillis);

        return promise;
    }

    @Override
    public Object sendSync(Topic topic, Request request, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);
        Object result = promise.invokeSync(this.promiseHolder, request, timeoutMillis);

        return result;
    }

    @Override
    public PromisHolder getPromiseHolder() {
        return this.promiseHolder;
    }

    @Override
    public ChannelHolder getChannelHolder() {
        return this.channelHolder;
    }
}

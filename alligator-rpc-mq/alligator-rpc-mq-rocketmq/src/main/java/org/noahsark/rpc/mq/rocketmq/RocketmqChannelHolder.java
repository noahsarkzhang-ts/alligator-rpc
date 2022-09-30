package org.noahsark.rpc.mq.rocketmq;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.PromisHolder;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.Subject;

/**
 * RocketMQ 通道容器
 *
 * @author zhangxt
 * @date 2021/5/4
 */
public class RocketmqChannelHolder implements ChannelHolder {

    private RocketmqProducer producer;

    private PromisHolder promisHolder;

    public RocketmqChannelHolder() {
    }

    public RocketmqChannelHolder(RocketmqProducer producer, PromisHolder promisHolder) {
        this.producer = producer;
        this.promisHolder = promisHolder;
    }

    @Override
    public void write(RpcCommand response) {

        this.promisHolder.write(response);

    }

    @Override
    public PromisHolder getPromisHolder() {
        return this.promisHolder;
    }

    @Override
    public Subject getSubject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubject(Subject subject) {
        throw new UnsupportedOperationException();
    }
}

package org.noahsark.rpc.mq.common;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.PromisHolder;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.Subject;

/**
 * MQ 通道管理
 * @author zhangxt
 * @date 2021/4/29
 */
public class MqChannelHolder implements ChannelHolder {

    private Producer producer;

    private PromisHolder promisHolder;

    public MqChannelHolder() {
    }

    public MqChannelHolder(Producer producer, PromisHolder promisHolder) {
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

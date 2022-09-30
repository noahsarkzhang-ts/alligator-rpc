package org.noahsark.rpc.socket.event;

import org.noahsark.rpc.socket.eventbus.ApplicationEvent;

/**
 * 提供给上层业务订阅的客户端下线事件
 * @author zhangxt
 * @date 2021/6/30
 */
public class ClientDisconnectEvent extends ApplicationEvent {

    public ClientDisconnectEvent(Object source) {
        super(source);
    }
}

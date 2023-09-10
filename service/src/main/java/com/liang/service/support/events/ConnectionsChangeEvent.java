package com.liang.service.support.events;

import org.springframework.context.ApplicationEvent;

/**
 * @since 2023/9/9 21:09
 * @author by liangzj
 */
public class ConnectionsChangeEvent extends ApplicationEvent {
    public ConnectionsChangeEvent(Object source) {
        super(source);
    }
}

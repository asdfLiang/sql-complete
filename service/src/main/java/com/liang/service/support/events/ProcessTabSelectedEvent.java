package com.liang.service.support.events;

import org.springframework.context.ApplicationEvent;

/**
 * @since 2023/10/5 23:28
 * @author by liangzj
 */
public class ProcessTabSelectedEvent extends ApplicationEvent {
    public ProcessTabSelectedEvent(Object source) {
        super(source);
    }
}

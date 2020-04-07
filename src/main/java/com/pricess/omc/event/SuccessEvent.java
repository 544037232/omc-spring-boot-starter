package com.pricess.omc.event;

import com.pricess.omc.ResultToken;
import org.springframework.context.ApplicationEvent;

public class SuccessEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param resultToken the object on which the event initially occurred (never {@code null})
     */
    public SuccessEvent(ResultToken resultToken) {
        super(resultToken);
    }
}

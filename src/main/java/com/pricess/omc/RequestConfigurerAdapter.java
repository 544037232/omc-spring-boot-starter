package com.pricess.omc;

import com.pricess.omc.core.ObjectPostProcessor;
import com.pricess.omc.serve.ServeAction;
import com.pricess.omc.serve.ServeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public class RequestConfigurerAdapter implements RequestConfigurer<ServeRequest>, ApplicationEventPublisherAware {

    private ServeAction serveAction;

    private ApplicationContext context;

    private ApplicationEventPublisher eventPublisher;

    private ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
        public <T> T postProcess(T object) {
            throw new IllegalStateException(
                    ObjectPostProcessor.class.getName()
                            + " is a required bean. Ensure you have used @EnableAppRequest and @Configuration");
        }
    };

    @Override
    public void init(ServeRequest builder) throws Exception {
        ServeAction serveAction = getServeAction();

        builder.addFilterChainBuilder(serveAction);
    }

    @Override
    public void configure(ServeRequest builder) throws Exception {

    }

    private ServeAction getServeAction() throws Exception {

        if (serveAction != null) {
            return serveAction;
        }
        Map<Class<?>, Object> sharedObjects = createSharedObjects();

        serveAction = new ServeAction(objectPostProcessor, sharedObjects);

        serveAction
                .paramsCheck()
                .and()
                .requestMatcher()
                .and()
                .service()
                .and()
                .store();

        configure(serveAction);

        return serveAction;
    }

    protected void configure(ServeAction builder) throws Exception {

    }

    private Map<Class<?>, Object> createSharedObjects() {
        Map<Class<?>, Object> sharedObjects = new HashMap<>();
        sharedObjects.put(ApplicationContext.class, context);
        sharedObjects.put(ApplicationEventPublisher.class,eventPublisher);
        return sharedObjects;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

}

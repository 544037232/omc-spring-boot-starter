package com.pricess.omc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 注解Bean构造器
 *
 * @author 王晟权
 */
final class ObjectAutowireBeanFactoryObjectPostProcessor
        implements ObjectPostProcessor<Object>, DisposableBean, SmartInitializingSingleton {
    private final Log logger = LogFactory.getLog(getClass());
    private final AutowireCapableBeanFactory autowireBeanFactory;
    private final List<DisposableBean> disposableBeans = new ArrayList<>();
    private final List<SmartInitializingSingleton> smartSingletons = new ArrayList<>();

    public ObjectAutowireBeanFactoryObjectPostProcessor(
            AutowireCapableBeanFactory autowireBeanFactory) {
        Assert.notNull(autowireBeanFactory, "autowireBeanFactory cannot be null");
        this.autowireBeanFactory = autowireBeanFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T postProcess(T object) {
        if (object == null) {
            return null;
        }
        T result;
        try {
            result = (T) this.autowireBeanFactory.initializeBean(object,
                    object.toString());
        } catch (RuntimeException e) {
            Class<?> type = object.getClass();
            throw new RuntimeException(
                    "Could not postProcess " + object + " of type " + type, e);
        }
        this.autowireBeanFactory.autowireBean(object);
        if (result instanceof DisposableBean) {
            this.disposableBeans.add((DisposableBean) result);
        }
        if (result instanceof SmartInitializingSingleton) {
            this.smartSingletons.add((SmartInitializingSingleton) result);
        }
        return result;
    }

    @Override
    public void afterSingletonsInstantiated() {
        for (SmartInitializingSingleton singleton : smartSingletons) {
            singleton.afterSingletonsInstantiated();
        }
    }

    @Override
    public void destroy() throws Exception {
        for (DisposableBean disposable : this.disposableBeans) {
            try {
                disposable.destroy();
            } catch (Exception error) {
                this.logger.error(error);
            }
        }
    }

}

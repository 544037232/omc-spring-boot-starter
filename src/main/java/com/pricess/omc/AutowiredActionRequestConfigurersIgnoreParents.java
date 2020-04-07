package com.pricess.omc;

import com.pricess.omc.core.ObjectConfigurer;
import com.pricess.omc.serve.ServeRequest;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.Assert;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pricess.wang
 * @date 2019/12/12 16:43
 */
final class AutowiredActionRequestConfigurersIgnoreParents {

    private final ConfigurableListableBeanFactory beanFactory;

    public AutowiredActionRequestConfigurersIgnoreParents(
            ConfigurableListableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "beanFactory cannot be null");
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<ObjectConfigurer<Filter, ServeRequest>> getRequestConfigurers() {

        List<ObjectConfigurer<Filter, ServeRequest>> requestAppConfigurers = new ArrayList<>();
        Map<String, RequestConfigurer> beansOfType = beanFactory
                .getBeansOfType(RequestConfigurer.class);

        for (Map.Entry<String, RequestConfigurer> entry : beansOfType.entrySet()) {
            requestAppConfigurers.add(entry.getValue());
        }
        return requestAppConfigurers;
    }
}

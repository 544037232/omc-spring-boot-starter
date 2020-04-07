package com.pricess.omc;

import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.core.ObjectBuilder;
import com.pricess.omc.core.ObjectConfigurer;

import javax.servlet.Filter;

/**
 * 应用请求构建接口
 *
 * @author pricess.wang
 * @date 2019/12/13 18:48
 */
public interface ActionBuilder<H extends ActionBuilder<H>> extends
        ObjectBuilder<ProviderManager> {

    <C extends ObjectConfigurer<ProviderManager, H>> C getConfigurer(
            Class<C> clazz);

    <C extends ObjectConfigurer<ProviderManager, H>> C removeConfigurer(
            Class<C> clazz);

    <C> void setSharedObject(Class<C> sharedType, C object);

    <C> C getSharedObject(Class<C> sharedType);

    H addFilterAfter(Filter filter, Class<? extends Filter> afterFilter);

    H addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter);

    H addFilter(Filter filter);

    H actionMatcher(ActionMatcher actionMatcher);

}

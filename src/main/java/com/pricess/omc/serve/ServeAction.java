package com.pricess.omc.serve;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.ActionMatcher;
import com.pricess.omc.api.Filter;
import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.configurer.ParamsAdapterConfigurer;
import com.pricess.omc.configurer.RequestMatcherConfigurer;
import com.pricess.omc.configurer.ServiceProviderConfigurer;
import com.pricess.omc.configurer.StoreProviderConfigurer;
import com.pricess.omc.core.AbstractConfiguredObjectBuilder;
import com.pricess.omc.core.ObjectBuilder;
import com.pricess.omc.core.ObjectPostProcessor;
import com.pricess.omc.filter.DebugFilter;
import com.pricess.omc.filter.FilterComparator;
import com.pricess.omc.handler.FailureHandler;
import com.pricess.omc.handler.NullFailureHandler;
import com.pricess.omc.handler.NullSuccessHandler;
import com.pricess.omc.handler.SuccessHandler;
import com.pricess.omc.manager.ActionProviderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 请求构建对象
 *
 * @author pricess.wang
 * @date 2019/12/13 18:31
 */
@Slf4j
public class ServeAction extends AbstractConfiguredObjectBuilder<ProviderManager, ServeAction>
        implements ObjectBuilder<ProviderManager>, ActionBuilder<ServeAction> {

    private final List<Filter> filters = new ArrayList<>();

    private SuccessHandler successHandler = new NullSuccessHandler();

    private FailureHandler failureHandler = new NullFailureHandler();

    private final FilterComparator comparator = new FilterComparator();

    private boolean debugEnabled;

    private ActionMatcher actionMatcher;

    private boolean continueChainBeforeSuccessfulFilter = false;

    @SuppressWarnings("unchecked")
    public ServeAction(ObjectPostProcessor<Object> objectPostProcessor, Map<Class<?>, Object> sharedObjects) {

        super(objectPostProcessor);

        for (Map.Entry<Class<?>, Object> entry : sharedObjects.entrySet()) {
            setSharedObject((Class<Object>) entry.getKey(), entry.getValue());
        }

    }

    @Override
    public ServeAction addFilterAfter(Filter filter, Class<? extends Filter> afterFilter) {
        comparator.registerAfter(filter.getClass(), afterFilter);
        return addFilter(filter);
    }

    @Override
    public ServeAction addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter) {
        comparator.registerBefore(filter.getClass(), beforeFilter);
        return addFilter(filter);
    }

    @Override
    public ServeAction addFilter(Filter filter) {
        Class<? extends Filter> filterClass = filter.getClass();
        if (!comparator.isRegistered(filterClass)) {
            throw new IllegalArgumentException(
                    "The Filter class "
                            + filterClass.getName()
                            + " does not have a registered order and cannot be added without a specified order. Consider using addFilterBefore or addFilterAfter instead.");
        }

        this.filters.add(filter);
        return this;
    }

    @Override
    public ServeAction actionMatcher(ActionMatcher actionMatcher) {
        this.actionMatcher = actionMatcher;
        return this;
    }

    @Override
    protected ProviderManager performBuild() throws Exception {

        ActionProviderManager actionProviderManager = new ActionProviderManager();
        actionProviderManager.setActionMatcher(actionMatcher);

        if (debugEnabled) {
            addFilter(new DebugFilter(actionProviderManager));
            log.warn("********** action:" + actionMatcher.getActionName() + " debugging is enabled.***********");
        }

        filters.sort(comparator);

        actionProviderManager.setFilters(filters);
        actionProviderManager.setSuccessHandler(successHandler);
        actionProviderManager.setFailureHandler(failureHandler);
        actionProviderManager.setContinueChainBeforeSuccessfulFilter(continueChainBeforeSuccessfulFilter);

        ApplicationEventPublisher eventPublisher = getSharedObject(ApplicationEventPublisher.class);
        if (eventPublisher != null) {
            actionProviderManager.setEventPublisher(eventPublisher);
        }

        return actionProviderManager;
    }

    /**
     * 参数
     */
    public ParamsAdapterConfigurer<ServeAction> params() throws Exception {
        return getOrApply(new ParamsAdapterConfigurer<>());
    }

    /**
     * 请求方法和URL
     */
    public RequestMatcherConfigurer<ServeAction> requestMatcher() throws Exception {
        return getOrApply(new RequestMatcherConfigurer<>());
    }

    /**
     * service服务相关设置
     */
    public ServiceProviderConfigurer<ServeAction> service() throws Exception {
        return getOrApply(new ServiceProviderConfigurer<>());
    }

    /**
     * 存储配置，包括持久化执行，事务管理
     */
    public StoreProviderConfigurer<ServeAction> store() throws Exception {
        return getOrApply(new StoreProviderConfigurer<>(this.getSharedObject(ApplicationContext.class)));
    }

    public ServeAction successHandler(SuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public ServeAction failureHandler(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public ServeAction continueChainBeforeSuccessfulFilter(boolean continueChainBeforeSuccessfulFilter) {
        this.continueChainBeforeSuccessfulFilter = continueChainBeforeSuccessfulFilter;
        return this;
    }

    public ServeAction debug(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

}

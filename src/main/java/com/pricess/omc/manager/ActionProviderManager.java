package com.pricess.omc.manager;

import com.pricess.omc.ActionMatcher;
import com.pricess.omc.ResultToken;
import com.pricess.omc.api.Filter;
import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.context.ActionContextHolder;
import com.pricess.omc.endpoint.ActionEndpoint;
import com.pricess.omc.event.SuccessEvent;
import com.pricess.omc.filter.VirtualFilterChain;
import com.pricess.omc.handler.FailureHandler;
import com.pricess.omc.handler.SuccessHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActionProviderManager implements ProviderManager {

    private List<Filter> filters;

    private ActionMatcher actionMatcher;

    private ApplicationEventPublisher eventPublisher;

    private SuccessHandler successHandler;

    private FailureHandler failureHandler;

    /**
     * 过滤器执行成功后继续执行过滤器
     */
    private boolean continueChainBeforeSuccessfulFilter;

    /**
     * @since 1.0.4
     * 执行端点，保证执行过程不会中断，比如多线程锁
     */
    private ActionEndpoint endpoint = new ActionEndpoint() {
    };

    public void setSuccessHandler(SuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    public void setFailureHandler(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void setActionMatcher(ActionMatcher actionMatcher) {
        this.actionMatcher = actionMatcher;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }

    @Override
    public ActionMatcher getActionMatcher() {
        return actionMatcher;
    }

    public void setContinueChainBeforeSuccessfulFilter(boolean continueChainBeforeSuccessfulFilter) {
        this.continueChainBeforeSuccessfulFilter = continueChainBeforeSuccessfulFilter;
    }

    @Override
    public void attemptExecutor(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) rep;

        try {

            if (!endpoint.preHandle(request, response)){
                return;
            }

            VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filters);

            virtualFilterChain.doFilter(req, rep);

            if (!virtualFilterChain.hasFinished()) {
                return;
            }

            if (continueChainBeforeSuccessfulFilter) {
                // 如果允许执行，则将响应传递交由spring的controller
                filterChain.doFilter(req, rep);
            } else {
                ResultToken result = ActionContextHolder.getContext().getResult();

                successfulExecutor(request, response, result);
            }

            endpoint.postHandle(request, response);

        } catch (Exception e) {

            endpoint.exceptionHandle(e, request, response);

            unsuccessfulExecutor(request, response, e);

        } finally {

            try {
                endpoint.finallyHandle(request, response);

            } catch (Exception ignored) {
                //不处理finally异常
            }
        }


    }

    private void successfulExecutor(HttpServletRequest request, HttpServletResponse response, ResultToken resultToken) throws IOException, ServletException {

        if (eventPublisher != null) {
            eventPublisher.publishEvent(new SuccessEvent(resultToken));
        }

        if (successHandler != null) {
            successHandler.onSuccessContext(request, response, resultToken);
        }
    }

    private void unsuccessfulExecutor(HttpServletRequest request, HttpServletResponse response, Exception failed) throws IOException, ServletException {

        if (failureHandler != null) {
            failureHandler.onFailureContext(request, response, failed);
        }
    }

    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setEndpoint(ActionEndpoint endpoint) {
        Assert.notNull(endpoint,"this endpoint can not be null");
        this.endpoint = endpoint;
    }
}

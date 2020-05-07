package com.pricess.omc.manager;

import com.pricess.omc.ActionMatcher;
import com.pricess.omc.ResultToken;
import com.pricess.omc.api.Filter;
import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.context.ActionContextHolder;
import com.pricess.omc.event.SuccessEvent;
import com.pricess.omc.filter.VirtualFilterChain;
import com.pricess.omc.handler.FailureHandler;
import com.pricess.omc.handler.SuccessHandler;
import org.springframework.context.ApplicationEventPublisher;

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

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filters);

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) rep;

        try {

            virtualFilterChain.doFilter(req, rep);

            if (!virtualFilterChain.hasFinished()) {
                return;
            }

        } catch (Exception e) {
            unsuccessfulExecutor(request, response, e);
            return;
        }

        if (continueChainBeforeSuccessfulFilter) {
            // 如果允许执行，则将响应传递交由spring的controller
            filterChain.doFilter(req, rep);
        } else {
            ResultToken result = ActionContextHolder.getContext().getResult();

            successfulExecutor(request, response, result);
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

}

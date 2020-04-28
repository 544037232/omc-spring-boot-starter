package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.ActionRequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

public class   RequestMatcherConfigurer<B extends ActionBuilder<B>>
        extends AbstractActionConfigurer<RequestMatcherConfigurer<B>, B> {

    private String url;

    private String actionName;

    private HttpMethod method = HttpMethod.GET;

    @Override
    public void init(B builder) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("the url can not be null");
        }

        if (StringUtils.isEmpty(actionName)) {
            this.actionName = url;
        }
    }

    @Override
    public void configure(B builder) throws Exception {
        builder.actionMatcher(new ActionRequestMatcher(url, actionName, method));
    }

    public RequestMatcherConfigurer<B> actionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public RequestMatcherConfigurer<B> url(String url) {
        this.url = url;
        return this;
    }

    public RequestMatcherConfigurer<B> method(HttpMethod method) {
        this.method = method;
        return this;
    }
}

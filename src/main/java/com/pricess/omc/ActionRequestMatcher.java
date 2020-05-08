package com.pricess.omc;

import org.springframework.http.HttpMethod;

public class ActionRequestMatcher implements ActionMatcher {

    private final String url;

    private final String actionName;

    private final HttpMethod httpMethod;

    public ActionRequestMatcher (String url,String actionName,HttpMethod httpMethod){
        this.url = url;
        this.actionName = actionName;
        this.httpMethod = httpMethod;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}

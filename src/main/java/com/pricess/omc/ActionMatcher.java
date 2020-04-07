package com.pricess.omc;

import org.springframework.http.HttpMethod;

public interface ActionMatcher {

    String getUrl();

    String getActionName();

    HttpMethod getHttpMethod();
}

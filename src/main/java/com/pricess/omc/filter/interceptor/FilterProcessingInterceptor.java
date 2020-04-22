package com.pricess.omc.filter.interceptor;

import com.pricess.omc.context.ActionContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FilterProcessingInterceptor implements InvocationHandler {

    private final Object targetFilter;

    private static final String FILTER_ATTR = "filterAttr";

    public FilterProcessingInterceptor(Object targetFilter) {
        this.targetFilter = targetFilter;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {

        ActionContextHolder.getContext().getAttribute(FILTER_ATTR);

        Object result= method.invoke(targetFilter, args);

        return result;
    }
}

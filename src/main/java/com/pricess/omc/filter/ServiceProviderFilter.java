package com.pricess.omc.filter;

import com.pricess.omc.ResultToken;
import com.pricess.omc.api.Filter;
import com.pricess.omc.api.ServiceProvider;
import com.pricess.omc.context.ActionContextHolder;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务执行类过滤器
 */
public class ServiceProviderFilter implements Filter {

    private final List<ServiceProvider> serviceProviders = new ArrayList<>();

    public ServiceProviderFilter(List<ServiceProvider> serviceProviders) {
        Assert.notNull(serviceProviders, "this service providers can not be null");
        this.serviceProviders.addAll(serviceProviders);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (serviceProviders.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        ResultToken result = ActionContextHolder.getContext().getResult();

        for (ServiceProvider serviceProvider : serviceProviders) {

            result = serviceProvider.provider(result);
        }

        ActionContextHolder.getContext().setResult(result);

        chain.doFilter(request, response);
    }
}

package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.api.ServiceProvider;
import com.pricess.omc.filter.ServiceProviderFilter;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderConfigurer<B extends ActionBuilder<B>>
        extends AbstractActionConfigurer<ServiceProviderConfigurer<B>, B> {

    private final List<ServiceProvider> serviceProviders = new ArrayList<>();

    @Override
    public void configure(B builder) throws Exception {
        ServiceProviderFilter filterServiceInterceptor = new ServiceProviderFilter(serviceProviders);

        builder.addFilter(filterServiceInterceptor);
    }

    public ServiceProviderConfigurer<B> addServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProviders.add(serviceProvider);
        return this;
    }

}

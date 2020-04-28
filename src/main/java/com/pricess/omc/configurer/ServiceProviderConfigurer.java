package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.api.ServiceProvider;
import com.pricess.omc.filter.DefaultResultProcessing;
import com.pricess.omc.filter.ResultBuilderFilter;
import com.pricess.omc.filter.ResultProcessing;
import com.pricess.omc.filter.ServiceProviderFilter;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderConfigurer<B extends ActionBuilder<B>>
        extends AbstractActionConfigurer<ServiceProviderConfigurer<B>, B> {

    private final List<ServiceProvider> serviceProviders = new ArrayList<>();

    private ResultProcessing resultProcessing;

    @Override
    public void init(B builder) throws Exception {
        if (resultProcessing == null){
            resultProcessing = new DefaultResultProcessing();
        }
    }

    @Override
    public void configure(B builder) throws Exception {
        ServiceProviderFilter filterServiceInterceptor = new ServiceProviderFilter(serviceProviders);

        ResultBuilderFilter filterResultInterceptor = new ResultBuilderFilter(resultProcessing);

        builder.addFilter(filterServiceInterceptor);
        builder.addFilter(filterResultInterceptor);
    }

    public ServiceProviderConfigurer<B> addServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProviders.add(serviceProvider);
        return this;
    }

    public ServiceProviderConfigurer<B> resultProcessing(ResultProcessing resultProcessing) {
        this.resultProcessing = resultProcessing;
        return this;
    }
}

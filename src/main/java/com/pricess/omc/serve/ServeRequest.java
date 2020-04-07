package com.pricess.omc.serve;

import com.pricess.omc.ProviderManagerProxy;
import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.core.AbstractConfiguredObjectBuilder;
import com.pricess.omc.core.ObjectBuilder;
import com.pricess.omc.core.ObjectPostProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author pricess.wang
 * @date 2019/12/13 18:24
 */
@Slf4j
public class ServeRequest extends AbstractConfiguredObjectBuilder<Filter, ServeRequest> implements
        ObjectBuilder<Filter> {

    private final List<ObjectBuilder<? extends ProviderManager>> actionFilterChainBuilders = new ArrayList<>();

    public ServeRequest(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    private Collection<String> urls = new ArrayList<>();

    @Override
    protected Filter performBuild() throws Exception {

        List<ProviderManager> providerManagers = new ArrayList<>(actionFilterChainBuilders.size());

        for (ObjectBuilder<? extends ProviderManager> providerManagerBuilder : actionFilterChainBuilders) {
            ProviderManager providerManager = providerManagerBuilder.build();
            providerManagers.add(providerManager);
            this.urls.add(providerManager.getActionMatcher().getUrl());
        }

        return new ProviderManagerProxy(providerManagers);
    }


    public ServeRequest addFilterChainBuilder(
            ObjectBuilder<? extends ProviderManager> actionFilterChainBuilder) {
        this.actionFilterChainBuilders.add(actionFilterChainBuilder);
        return this;
    }

    public Collection<String> getUrls() {
        return urls;
    }
}

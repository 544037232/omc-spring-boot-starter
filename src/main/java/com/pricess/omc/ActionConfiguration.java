package com.pricess.omc;

import com.pricess.omc.constant.ActionConstant;
import com.pricess.omc.core.ObjectConfigurer;
import com.pricess.omc.core.ObjectPostProcessor;
import com.pricess.omc.serve.ServeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.List;

/**
 * <p></p>
 *
 * @author pricess.wang
 * @date 2020/2/1 18:38
 */
@Configuration
public class ActionConfiguration {

    private ServeRequest serveRequest;

    private List<ObjectConfigurer<Filter, ServeRequest>> requestConfigurers;

    private ObjectPostProcessor<Object> objectPostProcessor;

    @Bean(ActionConstant.ACTION_FILTER_CHAIN_NAME)
    public FilterRegistrationBean<Filter> actionFilterChain() throws Exception {
        boolean hasConfigurers = requestConfigurers != null
                && !requestConfigurers.isEmpty();

        if (!hasConfigurers) {
            RequestConfigurerAdapter adapter = objectPostProcessor
                    .postProcess(new RequestConfigurerAdapter() {
                    });
            if (serveRequest == null) {
                serveRequest = this.objectPostProcessor
                        .postProcess(new ServeRequest(objectPostProcessor));
            }
            serveRequest.apply(adapter);
        }

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(serveRequest.build());
        registrationBean.setOrder(Integer.MAX_VALUE);
        registrationBean.setName(ActionConstant.ACTION_FILTER_CHAIN_NAME);
        registrationBean.setUrlPatterns(serveRequest.getUrls());
        return registrationBean;
    }

    @Autowired(required = false)
    public void setFilterChainProxyConfigurer(
            ObjectPostProcessor<Object> objectPostProcessor,
            @Value("#{@autowiredRequestConfigurersIgnoreParents.getRequestConfigurers()}") List<ObjectConfigurer<Filter, ServeRequest>> requestConfigurers)
            throws Exception {
        this.objectPostProcessor = objectPostProcessor;

        serveRequest = this.objectPostProcessor
                .postProcess(new ServeRequest(objectPostProcessor));

        for (ObjectConfigurer<Filter, ServeRequest> objectConfigurer : requestConfigurers) {
            serveRequest.apply(objectConfigurer);
        }
        this.requestConfigurers = requestConfigurers;
    }

    @Bean
    public AutowiredActionRequestConfigurersIgnoreParents autowiredRequestConfigurersIgnoreParents(
            ConfigurableListableBeanFactory beanFactory) {
        return new AutowiredActionRequestConfigurersIgnoreParents(beanFactory);
    }

}

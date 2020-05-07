package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.filter.ParamsAdapterFilter;
import com.pricess.omc.param.HandlerObject;
import com.pricess.omc.param.HandlerObjectArgumentResolver;
import com.pricess.omc.param.resolver.RequestNoneObjectResolver;
import com.pricess.omc.param.resolver.RequestParamObjectArgumentResolver;
import com.pricess.omc.param.resolver.RequestResponseBodyObjectResolver;
import com.pricess.omc.validator.ParamAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author pricess.wang
 * @date 2019/12/13 18:50
 */
public final class ParamsAdapterConfigurer<B extends ActionBuilder<B>>
        extends AbstractActionConfigurer<ParamsAdapterConfigurer<B>, B> {

    private final ParamsAdapterFilter paramsCheckFilter = new ParamsAdapterFilter();

    private Class<? extends ParamAdapter> adapterClass;

    private final List<HandlerObjectArgumentResolver> argumentResolvers = new LinkedList<>();

    @Override
    public final void init(B builder) throws Exception {
        initDefaultArgumentResolvers();
        builder.addFilter(paramsCheckFilter);
    }

    @Override
    public void configure(B builder) throws Exception {
        if (adapterClass != null) {
            HandlerObject handlerObject = new HandlerObject(adapterClass,argumentResolvers);

            paramsCheckFilter.setHandlerObject(handlerObject);
        }
    }

    /**
     * 可自定义参数校验解析器，用于对不同的包装参数对象进行统一校验
     *
     * @param adapterClass 参数适配器
     */
    public ParamsAdapterConfigurer<B> adapter(Class<? extends ParamAdapter> adapterClass) {
        this.adapterClass = adapterClass;
        return this;
    }

    private void initDefaultArgumentResolvers() {
        List<HandlerObjectArgumentResolver> resolvers = new ArrayList<>();

        resolvers.add(new RequestParamObjectArgumentResolver());
        resolvers.add(new RequestResponseBodyObjectResolver());
        resolvers.add(new RequestNoneObjectResolver());

        argumentResolvers.addAll(resolvers);
    }
}

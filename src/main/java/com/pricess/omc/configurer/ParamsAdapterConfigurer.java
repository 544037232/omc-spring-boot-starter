package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.filter.ParamsAdapterFilter;
import com.pricess.omc.validator.ParamAdapter;
import com.pricess.omc.validator.ParamParser;

/**
 * @author pricess.wang
 * @date 2019/12/13 18:50
 */
public final class ParamsAdapterConfigurer<B extends ActionBuilder<B>>
        extends AbstractActionConfigurer<ParamsAdapterConfigurer<B>, B> {

    private final ParamsAdapterFilter paramsCheckFilter = new ParamsAdapterFilter();

    private ParamParser paramParser;

    @Override
    public final void init(B builder) throws Exception {
        builder.addFilter(paramsCheckFilter);
    }

    @Override
    public void configure(B builder) throws Exception {
        paramsCheckFilter.setActionParamParser(paramParser);
    }

    public ParamParser getParamParser() {
        return paramParser;
    }

    /**
     * 可自定义参数校验解析器，用于对不同的包装参数对象进行统一校验
     *
     * @param paramParser 参数解析器
     */
    public ParamsAdapterConfigurer<B> actionParamParser(ParamParser paramParser) {
        this.paramParser = paramParser;
        return this;
    }
}

package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.core.ObjectConfigurerAdapter;

/**
 * @author pricess.wang
 * @date 2019/12/13 18:47
 */
public class AbstractActionConfigurer<T extends AbstractActionConfigurer<T, B>, B extends ActionBuilder<B>>
        extends ObjectConfigurerAdapter<ProviderManager, B> {

}

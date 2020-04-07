package com.pricess.omc;

import com.pricess.omc.core.ObjectBuilder;
import com.pricess.omc.core.ObjectConfigurer;

import javax.servlet.Filter;

/**
 * @author pricess.wang
 * @date 2019/12/13 18:23
 */
public interface RequestConfigurer<T extends ObjectBuilder<Filter>> extends
        ObjectConfigurer<Filter, T> {

}

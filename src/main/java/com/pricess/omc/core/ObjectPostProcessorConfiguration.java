package com.pricess.omc.core;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;

/**
 * 初始化一个默认的 {@link ObjectPostProcessor} 用于初始化对象使用
 *
 * @author 王晟权
 * @since 2.2
 */
public class ObjectPostProcessorConfiguration {

	@Bean
	public ObjectPostProcessor<Object> objectBuildPostProcessor(
			AutowireCapableBeanFactory beanFactory) {
		return new ObjectAutowireBeanFactoryObjectPostProcessor(beanFactory);
	}
}

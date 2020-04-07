package com.pricess.omc.component.registrar;

import com.pricess.omc.component.lock.SegmentLockComponent;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class SegmentLockRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder aspect = BeanDefinitionBuilder
                .rootBeanDefinition(SegmentLockComponent.class);

        registry.registerBeanDefinition("lock$SegmentLockComponent",
                aspect.getBeanDefinition());
    }
}

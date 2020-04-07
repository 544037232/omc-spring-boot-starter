package com.pricess.omc.component.registrar;

import com.pricess.omc.component.lock.ZkLockComponent;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ZooKeeperLockRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .rootBeanDefinition(ZkLockComponent.class);

        builder.addPropertyValue("connectionString","192.168.3.72:2181,192.168.3.72:2182,192.168.3.72:2183");
        builder.addPropertyValue("prefix","lock&zk");

        registry.registerBeanDefinition("lock$ZkLockComponent",
                builder.getBeanDefinition());
    }

}

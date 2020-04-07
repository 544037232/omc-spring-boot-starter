package com.pricess.omc.component;

import com.pricess.omc.annotation.EnableGlobalComponent;
import com.pricess.omc.component.mode.LockMode;
import com.pricess.omc.component.registrar.RedisLockRegistrar;
import com.pricess.omc.component.registrar.SegmentLockRegistrar;
import com.pricess.omc.component.registrar.ZooKeeperLockRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公共功能组件注入
 */
public class ComponentImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        Class<EnableGlobalComponent> annoType = EnableGlobalComponent.class;
        Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(annoType.getName(), false);
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(annotationAttributes);

        Assert.notNull(attributes, () -> String.format(
                "@%s is not present on importing class '%s' as expected",
                annoType.getSimpleName(), importingClassMetadata.getClassName()));

        List<String> classNames = new ArrayList<>(4);

        LockMode lockMode = attributes.getEnum("lock");

        switch (lockMode) {
            case ZK:
                classNames.add(ZooKeeperLockRegistrar.class.getName());
                break;
            case REDIS:
                classNames.add(RedisLockRegistrar.class.getName());
                break;
            default:
                classNames.add(SegmentLockRegistrar.class.getName());

        }

        return classNames.toArray(new String[0]);
    }
}

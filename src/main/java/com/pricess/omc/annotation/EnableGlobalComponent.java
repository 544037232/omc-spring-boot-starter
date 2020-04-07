package com.pricess.omc.annotation;

import com.pricess.omc.component.ComponentImportSelector;
import com.pricess.omc.component.mode.LockMode;
import com.pricess.omc.component.mode.ProfilerMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 启用组件
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import(ComponentImportSelector.class)
@Configuration
public @interface EnableGlobalComponent {

    LockMode lock() default LockMode.SEGMENT;

    ProfilerMode profiler() default ProfilerMode.NONE;
}

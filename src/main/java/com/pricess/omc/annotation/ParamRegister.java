package com.pricess.omc.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 参数注册
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@Documented
@Configuration
public @interface ParamRegister {

    /**
     * 请求内容类型
     *
     * @return MediaType
     */
    String contentType() default MediaType.ALL_VALUE;
}

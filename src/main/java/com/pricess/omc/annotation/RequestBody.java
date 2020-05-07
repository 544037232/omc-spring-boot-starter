package com.pricess.omc.annotation;

import java.lang.annotation.*;

/**
 * 请求body，实现 {@link com.pricess.omc.validator.ParamAdapter} 并在实现对象的属性中，
 * 如包装对象或集合，即可自动解析
 *
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestBody {

    boolean required() default true;
}

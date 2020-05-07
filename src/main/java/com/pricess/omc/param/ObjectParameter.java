package com.pricess.omc.param;

import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/6
 */
public class ObjectParameter {

    /**
     * 参数解析器
     */
    private HandlerObjectArgumentResolver resolver;

    private final Type genericParameterType;

    private final Annotation[] parameterAnnotations;

    private final int parameterIndex;

    private final Class<?> parameterType;

    private final String parameterName;

    public ObjectParameter(Field field, int parameterIndex) {
        this.parameterIndex = parameterIndex;
        this.parameterType = field.getType();
        this.genericParameterType = field.getGenericType();
        this.parameterAnnotations = field.getDeclaredAnnotations();
        this.parameterName = field.getName();
    }

    public void setResolver(HandlerObjectArgumentResolver resolver) {
        this.resolver = resolver;
    }

    public Type getGenericParameterType() {
        return genericParameterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
        Annotation[] anns = getParameterAnnotations();
        for (Annotation ann : anns) {
            if (annotationType.isInstance(ann)) {
                return (A) ann;
            }
        }
        return null;
    }

    public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
        return (getParameterAnnotation(annotationType) != null);
    }

    public String getParameterName() {
        return parameterName;
    }

    public HandlerObjectArgumentResolver getResolver() {
        return resolver;
    }
}

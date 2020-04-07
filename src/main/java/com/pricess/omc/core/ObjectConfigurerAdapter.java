package com.pricess.omc.core;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象构造器适配器
 * 提供基本的配置链流程
 *
 * @param <O> 由{@link B} 构建出来的对象
 * @param <B> 用于生成{@link O} 的配置生成器
 * @author : 王晟权
 * @date : 2019/7/8 10:03
 */
public abstract class ObjectConfigurerAdapter<O, B extends ObjectBuilder<O>>
        implements ObjectConfigurer<O, B> {
    private B objectBuilder;

    private CompositeObjectPostProcessor objectPostProcessor = new CompositeObjectPostProcessor();

    @Override
    public void init(B builder) throws Exception {
    }

    @Override
    public void configure(B builder) throws Exception {
    }

    /**
     * 使用{@link ObjectConfigurer} 配置出 {@link ObjectBuilder}
     * 用于连续配置使用
     *
     * @return  继续定义配置 {@link ObjectBuilder}
     */
    public B and() {
        return getBuilder();
    }

    /**
     * 获取 {@link ObjectBuilder}
     *
     * @return {@link ObjectBuilder}
     * @throws IllegalStateException if {@link ObjectBuilder} is null
     */
    protected final B getBuilder() {
        if (objectBuilder == null) {
            throw new IllegalStateException("objectBuilder cannot be null");
        }
        return objectBuilder;
    }

    /**
     * 对对象执行后处理。默认值是委托给 {@link ObjectPostProcessor}.
     *
     * @param object 要后期处理的对象
     * @return 要使用的可能已修改的对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T postProcess(T object) {
        return (T) this.objectPostProcessor.postProcess(object);
    }

    public void addObjectPostProcessor(ObjectPostProcessor<?> objectPostProcessor) {
        this.objectPostProcessor.addObjectPostProcessor(objectPostProcessor);
    }

    public void setBuilder(B builder) {
        this.objectBuilder = builder;
    }

    private static final class CompositeObjectPostProcessor implements
            ObjectPostProcessor<Object> {
        private List<ObjectPostProcessor<?>> postProcessors = new ArrayList<>();

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public Object postProcess(Object object) {
            for (ObjectPostProcessor opp : postProcessors) {
                Class<?> oppClass = opp.getClass();
                Class<?> oppType = GenericTypeResolver.resolveTypeArgument(oppClass,
                        ObjectPostProcessor.class);
                if (oppType == null || oppType.isAssignableFrom(object.getClass())) {
                    object = opp.postProcess(object);
                }
            }
            return object;
        }

        private void addObjectPostProcessor(
                ObjectPostProcessor<?> objectPostProcessor) {
            this.postProcessors.add(objectPostProcessor);
            postProcessors.sort(AnnotationAwareOrderComparator.INSTANCE);
        }
    }
}

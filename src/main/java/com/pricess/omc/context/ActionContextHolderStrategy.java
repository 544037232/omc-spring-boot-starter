package com.pricess.omc.context;

/**
 * 应用上下文执行器策略，当前只实现了ThreadLocal
 *
 * @author pricess.wang
 * @date 2019/12/19 15:25
 */
public interface ActionContextHolderStrategy<T> {

    void clearContext();

    T getContext();

    void setContext(T context);

    T createEmptyContext();
}

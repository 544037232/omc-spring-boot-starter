package com.pricess.omc.core;

/**
 * 对象构建器
 *
 * @author : 王晟权
 * @date : 2019/7/8 9:52
 */
public interface ObjectPostProcessor<T> {

    /**
     * 初始化对象并返回
     *
     * @param object 要初始化的对象
     * @return 对象的初始化版本
     */
    <O extends T> O postProcess(O object);
}

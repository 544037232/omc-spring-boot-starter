package com.pricess.omc.core;

/**
 * 对象配置
 *
 * @param <O> 需要构建的目标对象，可以是一个容器，也可以是一个具体的业务对象
 * @param <B> 用于构建的容器
 * @author : 王晟权
 * @date : 2019/7/8 10:02
 */
public interface ObjectConfigurer<O, B extends ObjectBuilder<O>> {

    /**
     * 初始化{@link B} 只应创建共享状态以及用于构建{@link B} 上的已经修改但不是属性的对象,
     * 在configure中即可生成正确的共享对象
     *
     * @param builder 构建的容器对象
     * @throws Exception exception
     */
    void init(B builder) throws Exception;

    /**
     * 可在容器{@link B}中定义一些业务属性,用于必要参数的设定
     * @param builder 构建的容器对象
     * @throws Exception exception
     */
    void configure(B builder) throws Exception;
}

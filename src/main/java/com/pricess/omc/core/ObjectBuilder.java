package com.pricess.omc.core;

/**
 * 对象构建超类,用于构建执行流程的BEAN
 *
 * @author : 王晟权
 * @date : 2019/7/8 10:01
 */
public interface ObjectBuilder <O> {

    /**
     * 构建一个用于业务使用的对象
     *
     * @return 要生成的对象，如果业务允许，可以为空
     * @throws Exception exception
     */
    O build() throws Exception;
}

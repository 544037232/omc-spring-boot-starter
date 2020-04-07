package com.pricess.omc.exception;

/**
 * 对象构建异常
 * @author : 王晟权
 * @date : 2019/7/3 17:32
 */
public class ObjectBuiltException extends RuntimeException {

    /**
     * Constructs an {@code CalculateException} with the specified message and root
     * cause.
     *
     * @param msg the detail message
     * @param t the root cause
     */
    public ObjectBuiltException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs an {@code CalculateException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public ObjectBuiltException(String msg) {
        super(msg);
    }
}

package com.pricess.omc.validator;


/**
 * 请求校验
 *
 * @author pricess.wang
 * @date 2019/12/31 19:00
 */
public interface ActionValidator {

    <T> ValidatorResult validate(T t);
}

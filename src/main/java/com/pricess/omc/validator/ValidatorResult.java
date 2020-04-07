package com.pricess.omc.validator;

import lombok.Getter;
import lombok.ToString;

/**
 * @author pricess.wang
 * @date 2019/12/31 19:07
 */
@ToString
public class ValidatorResult {

    @Getter
    private String errorMsg;

    public ValidatorResult(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}

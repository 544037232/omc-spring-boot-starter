package com.pricess.omc.validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author pricess.wang
 * @date 2019/12/31 19:41
 */
public interface ParamAdapter extends Serializable {

    default <T extends ParamAdapter> void preValidate(T t, HttpServletRequest request, HttpServletResponse response) throws Exception {
    }


    default <T extends ParamAdapter> void afterValidate(T t, HttpServletRequest request, HttpServletResponse response) throws Exception {
    }
}

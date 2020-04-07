package com.pricess.omc.handler;

import com.pricess.omc.ResultToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 应用接口过滤器执行成功处理
 *
 * @author pricess.wang
 * @date 2019/12/17 18:01
 */
public class NullSuccessHandler implements SuccessHandler {

    @Override
    public void onSuccessContext(HttpServletRequest request, HttpServletResponse response, ResultToken resultToken) throws IOException, ServletException {

    }
}

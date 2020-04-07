package com.pricess.omc.handler;

import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 应用接口过滤器失败处理器
 *
 * @author pricess.wang
 * @date 2019/12/17 18:01
 */
public class NullFailureHandler implements FailureHandler {

    @Override
    public void onFailureContext(HttpServletRequest request, HttpServletResponse response, Exception exp) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(exp.getMessage());
    }
}

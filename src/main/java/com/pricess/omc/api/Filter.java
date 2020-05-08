package com.pricess.omc.api;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author pricess.wang
 */
public interface Filter {

    /**
     * 过滤器，与servlet的过滤器分离，防止与spring自身的过滤器链注入冲突
     *
     * @param request  请求
     * @param response 相应
     * @param chain    原servlet过滤器链
     * @throws IOException      io error
     * @throws ServletException servlet error
     * @see 1.0.2
     */
    void doFilter(ServletRequest request, ServletResponse response,
                  FilterChain chain)
            throws IOException, ServletException;

}

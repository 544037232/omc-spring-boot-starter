package com.pricess.omc.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求执行端点，类似于拦截器
 *
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.4
 * @since 2020/5/7
 */
public interface ActionEndpoint {

    /**
     * 请求执行之前，是否继续执行业务，如果返回false，则不在向下执行，但一定会执行finally，如果此方法抛出异常，则执行exceptionHandle
     *
     * @param request  请求
     * @param response 响应
     * @return boolean
     * @since 1.0.4
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return true;
    }

    /**
     * 请求执行之后执行该方法
     *
     * @param request  请求
     * @param response 响应
     * @since 1.0.4
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }


    /**
     * 业务异常时执行的方法
     *
     * @param exp      异常
     * @param request  请求
     * @param response 响应
     * @since 1.0.4
     */
    default void exceptionHandle(Exception exp, HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 请求业务执行之后始终执行的方法
     *
     * @param request  请求
     * @param response 响应
     * @since 1.0.4
     */
    default void finallyHandle(HttpServletRequest request, HttpServletResponse response) {
    }
}

package com.pricess.omc.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抽象过滤器，开发者无需在每个过滤器中写chain.doFilter()，防止忘记写这句话而不在继续执行过滤器链
 * @author pricess.wang
 * @date 2020/1/19 15:40
 */
public abstract class AbstractFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) rep;

        doFilter(request, response);

        chain.doFilter(request, response);
    }

    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response);

}

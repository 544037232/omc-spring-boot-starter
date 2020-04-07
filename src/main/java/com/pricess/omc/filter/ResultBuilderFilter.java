package com.pricess.omc.filter;

import com.pricess.omc.ResultToken;
import com.pricess.omc.context.ActionContextHolder;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResultBuilderFilter implements Filter {

    private ResultProcessing resultProcessing;

    public ResultBuilderFilter(ResultProcessing resultProcessing) {
        Assert.notNull(resultProcessing, "this result processing can not be null");
        this.resultProcessing = resultProcessing;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        ResultToken result = resultProcessing.builder(request, response);

        if (result != null) {
            ActionContextHolder.getContext().setResult(result);
        }

        chain.doFilter(request,response);
    }
}

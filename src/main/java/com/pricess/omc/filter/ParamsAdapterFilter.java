package com.pricess.omc.filter;

import com.pricess.omc.api.Filter;
import com.pricess.omc.context.ActionContextHolder;
import com.pricess.omc.validator.*;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author pricess.wang
 * @date 2019/12/13 19:07
 */
public class ParamsAdapterFilter implements Filter {

    private final ActionValidator actionValidator = new DefaultActionValidator();

    private ParamParser paramParser;

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {

        if (paramParser == null) {
            chain.doFilter(req, rep);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) rep;
        // 准备参数对象

        ParamAdapter paramAdapter = paramParser.parser(request, response);

        ValidatorResult validatorResult = actionValidator.validate(paramAdapter);

        if (validatorResult != null) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(validatorResult.getErrorMsg());
            return;
        }

        ActionContextHolder.getContext().setParamAdapter(paramAdapter);

        chain.doFilter(req, rep);
    }

    public void setActionParamParser(ParamParser paramParser) {
        this.paramParser = paramParser;
    }
}

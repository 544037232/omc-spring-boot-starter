package com.pricess.omc.filter;

import com.pricess.omc.api.Filter;
import com.pricess.omc.context.ActionContextHolder;
import com.pricess.omc.param.HandlerObject;
import com.pricess.omc.param.InvocableHandlerObject;
import com.pricess.omc.validator.*;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;

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

    private HandlerObject handlerObject;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {

        if (handlerObject == null) {
            chain.doFilter(req, rep);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) rep;

        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        InvocableHandlerObject invocableHandlerObject = createInvocableHandlerObject(handlerObject);

        ParamAdapter paramAdapter = invocableHandlerObject.invokeAndHandle(webRequest);

        ValidatorResult validatorResult = actionValidator.validate(paramAdapter);

        if (validatorResult != null) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(validatorResult.getErrorMsg());
            return;
        }

        ActionContextHolder.getContext().setParamAdapter(paramAdapter);

        chain.doFilter(req, rep);
    }

    private InvocableHandlerObject createInvocableHandlerObject(HandlerObject handlerObject) {
        return new InvocableHandlerObject(handlerObject);
    }

    public void setHandlerObject(HandlerObject handlerObject) {
        this.handlerObject = handlerObject;
    }
}

package com.pricess.omc.param.resolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.pricess.omc.annotation.RequestBody;
import com.pricess.omc.param.HandlerObjectArgumentResolver;
import com.pricess.omc.param.ObjectParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

/**
 * 请求body解析器
 *
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/6
 */
public class RequestResponseBodyObjectResolver implements HandlerObjectArgumentResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RequestResponseBodyObjectResolver(){
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public boolean supportsParameter(ObjectParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(ObjectParameter parameter, NativeWebRequest webRequest) throws Exception {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);

        Object arg = objectMapper.readValue(inputMessage.getBody(),getJavaType(parameter.getGenericParameterType()));

        if (arg == null && checkRequired(parameter)) {
            throw new HttpMessageNotReadableException("Required request body is missing: " +
                    parameter.getParameterType().toGenericString(), inputMessage);
        }

        return arg;
    }

    protected boolean checkRequired(ObjectParameter parameter) {
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        return (requestBody != null && requestBody.required());
    }

    protected JavaType getJavaType(Type type) {
        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
        return typeFactory.constructType(type);
    }

}

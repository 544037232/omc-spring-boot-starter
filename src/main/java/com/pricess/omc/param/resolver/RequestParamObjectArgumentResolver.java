package com.pricess.omc.param.resolver;

import com.pricess.omc.param.HandlerObjectArgumentResolver;
import com.pricess.omc.param.ObjectParameter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 基本数据类型解析器，暂时只支持String Integer
 *
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/6
 */
public class RequestParamObjectArgumentResolver implements HandlerObjectArgumentResolver {

    @Override
    public boolean supportsParameter(ObjectParameter parameter) {
        return BeanUtils.isSimpleProperty(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(ObjectParameter parameter, NativeWebRequest webRequest) throws Exception {
        String value = webRequest.getParameter(parameter.getParameterName());

        if (value == null) {
            return null;
        }

        if (parameter.getParameterType() == Integer.class) {
            return Integer.valueOf(value);
        }

        if (parameter.getParameterType() == String.class) {
            return value;
        }
        return null;
    }

}

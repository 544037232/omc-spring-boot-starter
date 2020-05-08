package com.pricess.omc.param.resolver;

import com.pricess.omc.param.HandlerObjectArgumentResolver;
import com.pricess.omc.param.ObjectParameter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.NativeWebRequest;


/**
 * 空数据解析器
 *
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/6
 */
public class RequestNoneObjectResolver implements HandlerObjectArgumentResolver {

    @Override
    public boolean supportsParameter(ObjectParameter parameter) {
        return (parameter.getParameterAnnotations() == null || parameter.getParameterAnnotations().length == 0) &&
                !BeanUtils.isSimpleProperty(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(ObjectParameter parameter, NativeWebRequest webRequest) throws Exception {
        return null;
    }


}

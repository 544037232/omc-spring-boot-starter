package com.pricess.omc.param;

import com.pricess.omc.validator.ParamAdapter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Field;

/**
 * 对象转换代理类
 *
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/6
 */
public class InvocableHandlerObject extends HandlerObject {

    public InvocableHandlerObject(HandlerObject handlerObject) {
        super(handlerObject);
    }

    public ParamAdapter invokeAndHandle(ServletWebRequest webRequest) throws Exception {

        ParamAdapter paramAdapter = this.createNewInstance();

        setMethodArgumentValues(webRequest, paramAdapter);

        return paramAdapter;
    }

    protected void setMethodArgumentValues(NativeWebRequest request, ParamAdapter paramAdapter) throws Exception {

        ObjectParameter[] parameters = getParameters();

        if (ObjectUtils.isEmpty(parameters)) {
            return;
        }

        for (ObjectParameter parameter : parameters) {

            Object value = parameter.getResolver().resolveArgument(parameter, request);

            Field field = paramAdapter.getClass().getDeclaredField(parameter.getParameterName());

            field.setAccessible(true);

            field.set(paramAdapter, value);

        }
    }
}

package com.pricess.omc.param;

import com.pricess.omc.validator.ParamAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see 1.0.3
 * @since 2020/5/6
 */
public class HandlerObject {
    protected final Log logger = LogFactory.getLog(getClass());

    private final Class<? extends ParamAdapter> beanType;

    private final ObjectParameter[] parameters;

    private final List<HandlerObjectArgumentResolver> resolvers = new LinkedList<>();

    public HandlerObject(HandlerObject handlerObject) {
        Assert.notNull(handlerObject, "handlerObject is required");
        this.beanType = handlerObject.beanType;
        this.parameters = handlerObject.parameters;
        this.resolvers.addAll(handlerObject.resolvers);
    }

    public HandlerObject(Class<? extends ParamAdapter> beanType, List<HandlerObjectArgumentResolver> argumentResolvers) {
        this.beanType = beanType;
        this.parameters = initMethodParameters(argumentResolvers);
    }

    private ObjectParameter[] initMethodParameters(List<HandlerObjectArgumentResolver> argumentResolvers) {

        if (!CollectionUtils.isEmpty(argumentResolvers)) {
            addResolvers(argumentResolvers);
        }

        Field[] fields = beanType.getDeclaredFields();

        int count = fields.length;

        ObjectParameter[] result = new ObjectParameter[count];

        for (int i = 0; i < count; i++) {
            ObjectParameter parameter = new ObjectParameter(fields[i], i);
            parameter.setResolver(getArgumentResolver(parameter));
            result[i] = parameter;
        }
        return result;
    }

    private HandlerObjectArgumentResolver getArgumentResolver(ObjectParameter parameter) {

        HandlerObjectArgumentResolver result = null;

        for (HandlerObjectArgumentResolver resolver : this.resolvers) {
            if (resolver.supportsParameter(parameter)) {
                result = resolver;
                break;
            }
        }
        return result;
    }

    public Class<? extends ParamAdapter> getBeanType() {
        return beanType;
    }

    public ParamAdapter createNewInstance() {
        try {
            return beanType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public ObjectParameter[] getParameters() {
        return parameters;
    }

    protected static String formatArgumentError(ObjectParameter param, String message) {
        return "Could not resolve parameter [" + param.getParameterIndex() + "] in " +
                param.getParameterType().toGenericString() + (StringUtils.hasText(message) ? ": " + message : "");
    }

    public void addResolvers(List<HandlerObjectArgumentResolver> argumentResolvers) {
        this.resolvers.addAll(argumentResolvers);
    }
}

package com.pricess.omc.param;

import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author <a href="mailto:544037232@qq.com">pricess.wang</a>
 * @see
 * @since 2020/5/6
 */
public interface HandlerObjectArgumentResolver {

    /**
     * Whether the given {@linkplain ObjectParameter method parameter} is
     * supported by this resolver.
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    boolean supportsParameter(ObjectParameter parameter);

    Object resolveArgument(ObjectParameter parameter, NativeWebRequest request) throws Exception;

}

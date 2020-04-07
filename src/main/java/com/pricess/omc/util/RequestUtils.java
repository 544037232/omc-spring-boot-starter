package com.pricess.omc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pricess.wang
 * @date 2019/12/13 20:30
 */
public class RequestUtils {

    public static String buildRequestUrl(HttpServletRequest r) {
        return buildRequestUrl(r.getServletPath(), r.getRequestURI(), r.getContextPath(),
                r.getPathInfo(), r.getQueryString());
    }

    /**
     * Obtains the web application-specific fragment of the URL.
     */
    private static String buildRequestUrl(String servletPath, String requestURI,
                                   String contextPath, String pathInfo, String queryString) {

        StringBuilder url = new StringBuilder();

        if (servletPath != null) {
            url.append(servletPath);
            if (pathInfo != null) {
                url.append(pathInfo);
            }
        }
        else {
            url.append(requestURI.substring(contextPath.length()));
        }

        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

}

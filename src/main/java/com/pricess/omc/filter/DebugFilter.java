package com.pricess.omc.filter;

import com.pricess.omc.api.Filter;
import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

/**
 * 应用过滤器debug，用于测试调试用，可查看具体执行的filter和顺序已经provider
 *
 * @author pricess.wang
 * @date 2019/12/13 18:30
 */
public class DebugFilter implements Filter {
    static final String ALREADY_FILTERED_ATTR_NAME = DebugFilter.class.getName()
            .concat(".FILTERED");

    private final ProviderManager providerManager;

    private final Log logger = LogFactory.getLog(getClass());

    public DebugFilter(ProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    public final void doFilter(ServletRequest srvltRequest,
                               ServletResponse srvltResponse, FilterChain filterChain)
            throws ServletException, IOException {

        if (!(srvltRequest instanceof HttpServletRequest)
                || !(srvltResponse instanceof HttpServletResponse)) {
            throw new ServletException("DebugFilter just supports HTTP requests");
        }
        HttpServletRequest request = (HttpServletRequest) srvltRequest;
        HttpServletResponse response = (HttpServletResponse) srvltResponse;

        logger.info("\nRequest received for " + request.getMethod() + " '"
                + RequestUtils.buildRequestUrl(request) + "': \n\n"
                + "servletPath:" + request.getServletPath() + "\n" + "pathInfo:"
                + request.getPathInfo() + "\n" + "headers: \n" + formatHeaders(request)
                + "\n\n" + formatChains(providerManager));

        if (request.getAttribute(ALREADY_FILTERED_ATTR_NAME) == null) {
            invokeWithWrappedRequest(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }


    private void invokeWithWrappedRequest(HttpServletRequest request,
                                          HttpServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
        request.setAttribute(ALREADY_FILTERED_ATTR_NAME, Boolean.TRUE);
        request = new DebugRequestWrapper(request);
        try {
            filterChain.doFilter(request, response);
        } finally {
            request.removeAttribute(ALREADY_FILTERED_ATTR_NAME);
        }
    }

    private String formatHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> eHeaderNames = request.getHeaderNames();
        while (eHeaderNames.hasMoreElements()) {
            sb.append(" ");
            String headerName = eHeaderNames.nextElement();
            sb.append(headerName);
            sb.append(": ");
            Enumeration<String> eHeaderValues = request.getHeaders(headerName);
            while (eHeaderValues.hasMoreElements()) {
                sb.append(eHeaderValues.nextElement());
                if (eHeaderValues.hasMoreElements()) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String formatChains(ProviderManager providerManager) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action chain: ");
        if (providerManager == null) {
            sb.append("no match");
        } else {
            sb.append("{\n filter chain: ").append(chainAppend(providerManager.getFilters())).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    private <T> String chainAppend(List<T> list) {
        StringBuilder sb = new StringBuilder();

        if (list == null || list.isEmpty()) {
            sb.append("[] empty (bypassed by app='none') ");
        } else {
            sb.append("[\n");
            for (T t : list) {
                sb.append("   ").append(t.toString()).append("\n");
            }
            sb.append("]");
        }

        return sb.toString();
    }

}

class DebugRequestWrapper extends HttpServletRequestWrapper {
    private final Log logger = LogFactory.getLog(getClass());

    public DebugRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public HttpSession getSession() {
        boolean sessionExists = super.getSession(false) != null;
        HttpSession session = super.getSession();

        if (!sessionExists) {
            logger.info("New HTTP session created: " + session.getId());
        }

        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (!create) {
            return super.getSession(create);
        }
        return getSession();
    }
}

package com.pricess.omc;

import com.pricess.omc.api.ProviderManager;
import com.pricess.omc.context.ActionContextHolder;
import com.pricess.omc.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ProviderManagerProxy implements Filter {

    private final static String FILTER_APPLIED = ProviderManagerProxy.class.getName().concat(
            ".APPLIED");

    private final List<ProviderManager> providerManagers;

    public ProviderManagerProxy(List<ProviderManager> providerManagers) {
        this.providerManagers = providerManagers;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        boolean clearContext = request.getAttribute(FILTER_APPLIED) == null;
        if (clearContext) {
            try {
                request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                doFilterInternal(request, response, chain);
            } finally {
                ActionContextHolder.clearContext();
                request.removeAttribute(FILTER_APPLIED);
            }
        } else {
            doFilterInternal(request, response, chain);
        }
    }

    private void doFilterInternal(ServletRequest req, ServletResponse rep,
                                  FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        ProviderManager provider = getProvider(request);

        if (provider == null) {
            if (log.isDebugEnabled()) {
                log.debug(RequestUtils.buildRequestUrl(request) + ("has no matching filters"));
            }

            chain.doFilter(request, rep);

            return;
        }

        provider.attemptExecutor(req, rep, chain);
    }

    private ProviderManager getProvider(HttpServletRequest request) {

        for (ProviderManager providerManager : providerManagers) {
            ActionMatcher actionMatcher = providerManager.getActionMatcher();

            if (actionMatcher.getUrl().equals(request.getRequestURI())
                    && actionMatcher.getHttpMethod().name().equals(request.getMethod())) {
                return providerManager;
            }
        }
        return null;
    }

}

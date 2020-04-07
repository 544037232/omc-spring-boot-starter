package com.pricess.omc.filter;

import com.pricess.omc.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
public class VirtualFilterChain implements FilterChain {

    private final int size;

    private int currentPosition = 0;

    private final List<Filter> filters;


    public VirtualFilterChain(List<Filter> filters) {
        this.size = filters.size();
        this.filters = filters;
    }

    public boolean hasFinished() {
        return currentPosition == size;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response) throws IOException, ServletException {
        if (currentPosition != size) {

            currentPosition++;

            Filter nextFilter = filters.get(currentPosition - 1);

            if (log.isDebugEnabled()) {
                HttpServletRequest request = (HttpServletRequest) req;
                log.debug(RequestUtils.buildRequestUrl(request)
                        + " at position " + currentPosition + " of " + size
                        + " in additional filter chain; firing Filter: '"
                        + nextFilter.getClass().getSimpleName() + "'");
            }

            nextFilter.doFilter(req, response, this);

        }
    }
}

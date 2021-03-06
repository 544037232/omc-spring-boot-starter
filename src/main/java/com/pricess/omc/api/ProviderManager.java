package com.pricess.omc.api;

import com.pricess.omc.ActionMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;

public interface ProviderManager {

    List<Filter> getFilters();

    ActionMatcher getActionMatcher();

    void attemptExecutor(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException;
}

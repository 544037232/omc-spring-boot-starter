package com.pricess.omc.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FailureHandler {

    void onFailureContext(HttpServletRequest request, HttpServletResponse response, Exception exp) throws IOException, ServletException;

}

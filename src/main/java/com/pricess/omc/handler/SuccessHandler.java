package com.pricess.omc.handler;

import com.pricess.omc.ResultToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SuccessHandler {

    void onSuccessContext(HttpServletRequest request, HttpServletResponse response, ResultToken resultToken) throws IOException, ServletException;

}

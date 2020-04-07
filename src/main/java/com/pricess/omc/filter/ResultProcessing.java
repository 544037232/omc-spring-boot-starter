package com.pricess.omc.filter;

import com.pricess.omc.ResultToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 返回对象创建
 */
public interface ResultProcessing {

    ResultToken builder(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;
}

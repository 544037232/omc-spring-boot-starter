package com.pricess.omc.api;

import com.pricess.omc.ResultToken;

/**
 * 服务执行器
 */
public interface ServiceProvider {

    ResultToken provider(ResultToken token);

}

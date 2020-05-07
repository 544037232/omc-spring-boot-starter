package com.pricess.omc.context;

import com.pricess.omc.ResultToken;
import com.pricess.omc.validator.ParamAdapter;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求处理上下文，在请求执行完成后自动清空
 *
 * @author pricess.wang
 * @date 2019/12/19 15:24
 */
public interface ActionContext extends Serializable {

    /**
     * 从上下文中获取参数适配器
     *
     * @param <P> 参数集
     * @return ParamAdapter
     * @see 1.0.0
     */
    <P extends ParamAdapter> P getParamAdapter();

    /**
     * 将参数适配器设置到上下文中
     *
     * @param paramBean 参数适配器
     * @see 1.0.0
     */
    void setParamAdapter(ParamAdapter paramBean);

    /**
     * 设置一个临时变量或对象在上下文中
     *
     * @param name  key
     * @param value value
     * @see 1.0.1
     */
    void setAttribute(String name, Object value);

    /**
     * 获取上下文中的变量值
     *
     * @param name key
     * @param <T>  返回对象
     * @return value
     * @see 1.0.2
     */
    <T> T getAttribute(String name);

    /**
     * 获取所有变量及变量值
     *
     * @return all attrs
     * @see 1.0.1
     */
    Map<String, Object> getAttributes();

    /**
     * 处理的结果对象设置到上下文中
     *
     * @param result 处理结果
     * @see 1.0.0
     */
    void setResult(ResultToken result);

    /**
     * 获取当前线程处理的结果对象
     *
     * @param <R> 结果集
     * @return 处理结果
     * @see 1.0.3
     */
    <R extends ResultToken> R getResult();
}

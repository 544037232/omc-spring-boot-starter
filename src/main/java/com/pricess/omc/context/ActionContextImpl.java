package com.pricess.omc.context;

import com.pricess.omc.ResultToken;
import com.pricess.omc.validator.ParamAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActionContextImpl implements ActionContext {

    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ================================================================================================

    private ParamAdapter paramAdapter;

    private final Map<String,Object> attr = new ConcurrentHashMap<>();

    private ResultToken resultToken;

    // ~ Methods
    // ========================================================================================================

    @Override
    public ParamAdapter getParamAdapter() {
        return paramAdapter;
    }

    @Override
    public void setParamAdapter(ParamAdapter paramAdapter) {
        this.paramAdapter = paramAdapter;
    }

    @Override
    public void setAttribute(String key, Object val) {
        synchronized (attr){
            attr.put(key,val);
        }
    }

    @Override
    public Object getAttribute(String key) {
        return attr.get(key);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attr;
    }

    @Override
    public void setResult(ResultToken result) {
        this.resultToken = result;
    }

    @Override
    public ResultToken getResult() {
        return resultToken;
    }

}

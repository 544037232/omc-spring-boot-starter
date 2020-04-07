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

    private Map<Object,Object> process = new ConcurrentHashMap<>();

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
    public void setProcessObj(Object key, Object val) {
        process.put(key,val);
    }

    @Override
    public Object getProcessObj(Object key) {
        return process.get(key);
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

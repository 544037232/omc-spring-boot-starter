package com.pricess.omc.context;

import com.pricess.omc.ResultToken;
import com.pricess.omc.validator.ParamAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ActionContextImpl implements ActionContext {

    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ================================================================================================

    private ParamAdapter paramAdapter;

    private final Map<String, Object> attr = new ConcurrentSkipListMap<>();

    private ResultToken resultToken;

    // ~ Methods
    // ========================================================================================================

    @SuppressWarnings("unchecked")
    @Override
    public <P extends ParamAdapter> P getParamAdapter() {
        return (P) paramAdapter;
    }

    @Override
    public void setParamAdapter(ParamAdapter paramAdapter) {
        this.paramAdapter = paramAdapter;
    }

    @Override
    public void setAttribute(String key, Object val) {
        synchronized (attr) {
            attr.put(key, val);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String key) {
        return (T) attr.get(key);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attr;
    }

    @Override
    public void setResult(ResultToken result) {
        this.resultToken = result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends ResultToken> R getResult() {
        return (R) resultToken;
    }

}

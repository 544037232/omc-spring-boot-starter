package com.pricess.omc.context;

import org.springframework.util.Assert;

/**
 * 本地线程上下文执行策略
 *
 * @author pricess.wang
 * @date 2019/12/19 15:28
 */
public class ThreadLocalActionContextHolderStrategy implements ActionContextHolderStrategy<ActionContext> {

    private static final ThreadLocal<ActionContext> contextHolder = new ThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public ActionContext getContext() {
        ActionContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    @Override
    public void setContext(ActionContext context) {
        Assert.notNull(context, "Only non-null AppContext instances are permitted");
        contextHolder.set(context);
    }

    @Override
    public ActionContext createEmptyContext() {
        return new ActionContextImpl();
    }
}

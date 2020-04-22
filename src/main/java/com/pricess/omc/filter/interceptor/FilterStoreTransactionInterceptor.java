package com.pricess.omc.filter.interceptor;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FilterStoreTransactionInterceptor implements InvocationHandler {

    private final Object storeProxy;

    /**
     * 事务管理器
     */
    private final PlatformTransactionManager transactionManager;

    /**
     * 事务传播机制
     */
    private final TransactionDefinition transDefinition;


    public FilterStoreTransactionInterceptor(Object storeProxy, PlatformTransactionManager transactionManager, TransactionDefinition transDefinition) {
        this.storeProxy = storeProxy;
        this.transactionManager = transactionManager;
        this.transDefinition = transDefinition;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionStatus transStatus = transactionManager.getTransaction(transDefinition);

        Object result;
        try {
            result = method.invoke(storeProxy, args);

            transactionManager.commit(transStatus);

        } catch (Exception e) {
            transactionManager.rollback(transStatus);

            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            }

            throw e;
        }
        return result;
    }

    @Override
    public String toString() {
        return storeProxy.getClass().getSimpleName();
    }
}

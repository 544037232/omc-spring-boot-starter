package com.pricess.omc.configurer;

import com.pricess.omc.ActionBuilder;
import com.pricess.omc.api.StoreProvider;
import com.pricess.omc.filter.StoreProviderFilter;
import com.pricess.omc.filter.interceptor.FilterStoreTransactionInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.LinkedList;

public class StoreProviderConfigurer<B extends ActionBuilder<B>>
        extends AbstractActionConfigurer<StoreProviderConfigurer<B>, B> {

    private LinkedList<StoreProvider> storeProviders = new LinkedList<>();

    private final ApplicationContext context;

    private TransactionConfig transactionConfig;

    public StoreProviderConfigurer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init(B builder) throws Exception {
        if (transactionConfig == null) {
            transactionConfig = new TransactionConfig(context, this);
        }
    }

    @Override
    public void configure(B builder) throws Exception {
        transactionConfig.builder(builder);
    }

    public StoreProviderConfigurer<B> addStoreProvider(StoreProvider storeProvider) {
        this.storeProviders.add(storeProvider);
        return this;
    }

    public TransactionConfig transaction() {
        TransactionConfig config = new TransactionConfig(context, this);
        this.transactionConfig = config;
        return config;
    }

    private interface TransactionBuilder<B> {
        void builder(B builder);
    }

    private interface Transaction<B> {
        void builder(B builder);
    }

    public class TransactionConfig implements TransactionBuilder<B> {

        private ApplicationContext context;

        private StoreProviderConfigurer<B> configurer;

        private Transaction<B> transaction = new NoneTransaction();

        public TransactionConfig(ApplicationContext context, StoreProviderConfigurer<B> configurer) {
            this.context = context;
            this.configurer = configurer;
        }

        public JdbcTransaction jdbc() {
            JdbcTransaction jdbcTransaction = new JdbcTransaction(context, configurer);
            this.transaction = jdbcTransaction;
            return jdbcTransaction;
        }

        @Override
        public void builder(B builder) {
            transaction.builder(builder);
        }

        public class NoneTransaction implements Transaction<B> {

            @Override
            public void builder(B builder) {
                builder.addFilter(new StoreProviderFilter(storeProviders));
            }
        }

        public class JdbcTransaction implements Transaction<B> {

            private StoreProviderConfigurer<B> configurer;

            /**
             * 事务管理器
             */
            private final PlatformTransactionManager transactionManager;

            /**
             * 事务传播机制
             */
            private DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);

            public JdbcTransaction(ApplicationContext context, StoreProviderConfigurer<B> configurer) {
                DataSource dataSource = context.getBean(DataSource.class);
                this.transactionManager = new DataSourceTransactionManager(dataSource);
                this.configurer = configurer;
            }

            public JdbcTransaction definition(int propagationBehavior) {
                transDefinition.setPropagationBehavior(propagationBehavior);
                return this;
            }

            public StoreProviderConfigurer<B> and() {
                return configurer;
            }

            @Override
            public void builder(B builder) {
                Filter storeProviderFilter = new StoreProviderFilter(storeProviders);

                FilterStoreTransactionInterceptor filterStoreInterceptor =
                        new FilterStoreTransactionInterceptor(storeProviderFilter, transactionManager, transDefinition);

                Filter proxy = (Filter) Proxy.newProxyInstance(storeProviderFilter.getClass().getClassLoader(),
                        storeProviderFilter.getClass().getInterfaces(), filterStoreInterceptor);

                builder.addFilterAfter(proxy, StoreProviderFilter.class);
            }
        }
    }
}

package com.pricess.omc.component.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * zookeeper锁组件
 */
public class ZkLockComponent implements InitializingBean {

    /**
     * 锁标识前缀
     */
    private String prefix;

    /**
     * 默认锁定时间单位
     */
    private final TimeUnit DEFAULT_LOCK_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 锁定时间单位
     */
    private TimeUnit timeUnit = DEFAULT_LOCK_TIME_UNIT;

    /**
     * 默认锁等待时间
     */
    private final int DEFAULT_LOCK_WAIT_TIME = 0;

    /**
     * 锁等待时间
     */
    private int lockWaitTime = DEFAULT_LOCK_WAIT_TIME;

    /**
     * zookeeper连接，127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
     */
    private String connectionString;

    /**
     * 连接心态间隔
     */
    private int sleepTimeMs = 3000;

    /**
     * 客户端重新次数
     */
    private int maxRetries = 10;

    private CuratorFramework client;

    public boolean tryLock(String key) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, prefix + key);

        return lock.acquire(lockWaitTime, timeUnit);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (StringUtils.isEmpty(connectionString)) {
            throw new IllegalArgumentException("this zookeeper connectionString is null");
        }

        RetryPolicy policy = new ExponentialBackoffRetry(sleepTimeMs, maxRetries);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectionString, policy);

        curatorFramework.start();

        this.client = curatorFramework;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setSleepTimeMs(int sleepTimeMs) {
        this.sleepTimeMs = sleepTimeMs;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setLockWaitTime(int lockWaitTime) {
        this.lockWaitTime = lockWaitTime;
    }
}

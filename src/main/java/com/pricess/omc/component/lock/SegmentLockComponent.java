package com.pricess.omc.component.lock;

import com.pricess.omc.exception.LockBusyException;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SegmentLockComponent<T> {
    private Integer segments = 30;//默认分段数量

    private final HashMap<Integer, ReentrantLock> lockMap = new HashMap<>();

    public SegmentLockComponent() {
        init(null, false);
    }

    public SegmentLockComponent(Integer counts, boolean fair) {
        init(counts, fair);
    }

    private void init(Integer counts, boolean fair) {
        if (counts != null) {
            segments = counts;
        }
        for (int i = 0; i < segments; i++) {
            lockMap.put(i, new ReentrantLock(fair));
        }
    }

    public void lock(T key) {
        ReentrantLock lock = lockMap.get((key.hashCode() >>> 1) % segments);

        if (lock.isLocked()) {
            throw new LockBusyException("业务繁忙,请稍后重试");
        }

        lock.lock();
    }

    public void unlock(T key) {
        ReentrantLock lock = lockMap.get((key.hashCode() >>> 1) % segments);
        lock.unlock();
    }

}

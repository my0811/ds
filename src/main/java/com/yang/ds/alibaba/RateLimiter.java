package com.yang.ds.alibaba;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 笔试题目2，基于令牌桶实现限流
 *
 * @author: zhongkui.yang
 * @Date: 20-4-14
 */
public class RateLimiter {
    private final static Semaphore SEMAPHORE_LIMITER = new Semaphore(10);

    public void visit() throws InterruptedException {

        SEMAPHORE_LIMITER.acquire();
    }

    public void release() {
        SEMAPHORE_LIMITER.release();
    }
}

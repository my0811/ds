package com.yang.ds.algorithm.window.timewindow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟固定窗口的限流
 * 1秒一个次数限制的窗口，但是窗口的临界值就存在问题了，1秒到2秒之间存在很多数据进来
 * */
public class FixedWindowRateLimiter implements RateLimiter, Runnable {
    private static final int DEFAULT_ALLOWED_VISIT_PER_SECOND = 5;

    private final int maxVisitPerSecond;

    private AtomicInteger count;
    private int window = 1;

    FixedWindowRateLimiter() {
        this.maxVisitPerSecond = DEFAULT_ALLOWED_VISIT_PER_SECOND;
        this.count = new AtomicInteger();
    }

    FixedWindowRateLimiter(int maxVisitPerSecond) {
        this.maxVisitPerSecond = maxVisitPerSecond;
        this.count = new AtomicInteger();
    }

    @Override
    public boolean isOverLimit() {
        return currentQPS() > maxVisitPerSecond;
    }

    @Override
    public int currentQPS() {
        return count.get();
    }

    @Override
    public boolean visit() {
        count.incrementAndGet();
        System.out.printf("%s ", isOverLimit());
        return isOverLimit();
    }

    @Override
    public void run() {
        System.out.println(currentQPS());
        count.set(0);
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter();
        scheduledExecutorService.scheduleAtFixedRate(rateLimiter, 0, 1, TimeUnit.SECONDS);

        // 定义两个恶意攻击的，上一个时间窗口1秒中5个以上的请求拒绝掉了，但是到下一个一秒的瞬间所有情况求又过来了
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    rateLimiter.visit();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    rateLimiter.visit();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}

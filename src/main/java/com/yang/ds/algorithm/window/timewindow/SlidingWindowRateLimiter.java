package com.yang.ds.algorithm.window.timewindow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 时间的滑动窗口
 *
 *
 *
 * 1. 将1秒拆分成10个窗口，用数组来保存，数组每个窗口上维护一个计数器
 * 2. 然后每100ms向下一个窗口滑动一次，也就是把索引index指向数组下一个位置
 * 3. 然后把下一个位置上窗口计数器，刨除，因为窗口是循环的，到了下一个周期，就释放前面
 * 曾经请求的数量
 *
 * 1.即使恶意攻击，上次请求全部集中到了最后一个窗口，那么前面哪些窗口肯定没有请求，则窗口全部
 * 滑动完，然后循环回去，也是在总量上释放上次请求的数量，直到释放的数量达到了限制条件，才会有请求
 * 被进来，期间其他请求还是都会被拒绝掉
 *
 *
 *
 * */
public class SlidingWindowRateLimiter implements RateLimiter, Runnable {
    // 默认每秒的请求数量
    private static final int DEFAULT_ALLOWED_VISIT_PER_SECOND = 1000;

    // 最大请求限量
    private final long maxVisitPerSecond;

    // 默认时间窗口数量
    private static final int DEFAULT_BLOCK = 10;

    // 窗口数量
    private final int block;

    // 每个时间窗口的情况数量
    private final AtomicInteger[] countPerBlock;

    // 总请求量
    private AtomicInteger count;

    // 窗口索引
    private volatile int index;

    public SlidingWindowRateLimiter(int block, long maxVisitPerSecond) {
        this.block = block;
        this.maxVisitPerSecond = maxVisitPerSecond;
        countPerBlock = new AtomicInteger[block];
        for (int i = 0; i < block; i++) {
            countPerBlock[i] = new AtomicInteger();
        }
        count = new AtomicInteger(0);
    }

    public SlidingWindowRateLimiter() {
        this(DEFAULT_BLOCK, DEFAULT_ALLOWED_VISIT_PER_SECOND);
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
        countPerBlock[index].incrementAndGet();
        count.incrementAndGet();
        return isOverLimit();
    }

    @Override
    public void run() {
        // 滑动窗口的核心逻辑
        System.out.println(isOverLimit());
        System.out.println(currentQPS());
        System.out.println("index:" + index);
        // 向下滑动一个窗口
        int next = (index + 1) % block;
        int val = countPerBlock[next].getAndSet(0);
        // 移动到下一个窗口，总的请求量-val
        count.addAndGet(-val);
        index = next;
    }

    public static void main(String[] args) {
        SlidingWindowRateLimiter slidingWindowRateLimiter = new SlidingWindowRateLimiter(10, 5);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(slidingWindowRateLimiter, 100, 100, TimeUnit.MILLISECONDS);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean limit = slidingWindowRateLimiter.visit();
                    try {
                        if (limit) {
                            Thread.sleep(50);
                        }
                        Thread.sleep(10);
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
                    boolean limit = slidingWindowRateLimiter.visit();
                    try {
                        if (limit) {
                            Thread.sleep(50);
                        }
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

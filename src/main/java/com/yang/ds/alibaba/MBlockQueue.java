package com.yang.ds.alibaba;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 笔试题目3，实现条件队列
 *
 * @author: zhongkui.yang
 * @Date: 20-4-14
 */
public class MBlockQueue {

    // 本地队列
    private static final Queue<String> LOCAL_QUEUE = new LinkedList<>();

    // 队列最大16个元素
    private static final int MAX_SIZE = 1 << 4;

    // 元素计数器
    private AtomicInteger count = new AtomicInteger();

    // 单锁，链表也可以实现锁分裂，两端，两把锁，降低锁冲突
    private ReentrantLock lock = new ReentrantLock();

    // 队列不能空，条件队列
    private Condition notEmpty = lock.newCondition();

    // 队列不能满，条件队列
    private Condition notFull = lock.newCondition();

    public void put(String msg) throws InterruptedException {
        lock.lock();
        try {
            while (count.get() - MAX_SIZE >= 0) {
                notFull.await();
            }
            LOCAL_QUEUE.offer(msg);
            count.getAndDecrement();
            notEmpty.signal();
        } finally {
            lock.unlock();
        }

    }

    public String take() throws InterruptedException {
        lock.lock();
        String msg = "";
        try {
            while (count.get()==0){
                notEmpty.await();
            }
            msg = LOCAL_QUEUE.poll();
            count.decrementAndGet();
            notFull.signal();
        } finally {
            lock.unlock();
        }
        return msg;
    }
}

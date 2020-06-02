package com.yang.ds.alibaba;

/**
 * 笔试题目2，基于令牌桶实现限流
 *
 * @author: zhongkui.yang
 * @Date: 20-4-14
 */
public class DeadLock {
    private Object leftLock = new Object();
    private Object rightLock = new Object();

    public void leftPut() {
        synchronized (leftLock) {
            synchronized (rightLock) {

            }
        }
    }

    public void rightPut() {
        synchronized (rightLock) {
            synchronized (leftLock) {

            }
        }
    }
}

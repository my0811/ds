package com.yang.ds.algorithm.stack;

import java.util.Arrays;

/**
 * 栈结构升级版，
 * 自动扩容
 */
public class HighStack {
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final int MAX_SIZE = 300;
    transient Object[] elementData;
    private int size;
    private int top;

    public HighStack() {
        top = -1;
        elementData = EMPTY_ELEMENTDATA;
        size = 0;
    }

    /**
     * push
     */
    public void push(Object data) {
        ensureCapacityInternal(size + 1);
        // 注意因为top设置的初始值为-1，所以小执行++top先赋值再操作
        elementData[++top] = data;
        size++;
    }

    public int size() {
        return size;
    }

    public int curStackLen() {
        return elementData.length;
    }

    /**
     * pop
     */
    public Object pop() {
        if (isEmpty()) {
            throw new IllegalArgumentException(" stack is empty");
        }
        size--;
        return elementData[top--];
    }

    /**
     * peek
     */
    public Object peek() {
        if (isEmpty()) {
            throw new IllegalArgumentException(" stack is empty");
        }
        return elementData[top];
    }

    public boolean isFull() {
        return (top + 1) == elementData.length;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * 判断是否需要扩容，默认初始化大小为10
     */
    public void ensureCapacityInternal(int minCapacity) {
        if (this.elementData == EMPTY_ELEMENTDATA) {

            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        grow(minCapacity);

    }

    /**
     * 数组扩容，2倍扩容
     */
    public void grow(int minCapacity) {
        // 注意这里不能比较size，size是当前有效长度，但是扩容后真是的数组长度会比size大
        if (minCapacity - elementData.length > 0) {// 扩容
            int oldCapacity = elementData.length;
            // 扩种两倍的容量
            int newCapacity = oldCapacity << 1;
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            if (newCapacity - MAX_SIZE > 0) {
                throw new IllegalArgumentException("over stack max is: " + MAX_SIZE);
            }
            this.elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    public static void main(String[] args) {
        HighStack hightStack = new HighStack();
        for (int i = 0; i < 11; i++) {
            hightStack.push("y" + 1);
        }
        System.out.println(hightStack.size());
        System.out.println(hightStack.curStackLen());
        while (!hightStack.isEmpty()) {
            System.out.println(hightStack.peek() + "---" + hightStack.pop());
        }
    }

}

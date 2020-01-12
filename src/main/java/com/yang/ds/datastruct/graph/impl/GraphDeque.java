package com.yang.ds.datastruct.graph.impl;

import com.yang.ds.algorithm.utils.YUtils;

/**
 * 图，广度优先，使用的队列
 * */
public class GraphDeque<V> {

    /**默认queue大小为16*/
    private static final int DEQUE_DEFAULT_CAPACITY = 1 << 4;

    /**双端队列的最大size大小*/
    private static final int DEQUE_MAX_CAPACITY = 1 << 30;

    /**双端队列头指针*/
    private int head;

    /**双端队列尾指针*/
    private int tail;

    /**存储元素数组*/
    private Object[] elements;

    /**当前元素个数*/
    private int size;


    public GraphDeque(int initCapacity) {
        if (initCapacity <= 0) {
            throw new IllegalArgumentException("illegal capacity :" + initCapacity);
        }
        int cap = YUtils.sizeForBit32(initCapacity, DEQUE_DEFAULT_CAPACITY, DEQUE_MAX_CAPACITY);
        elements = new Object[cap];
        head = tail = 0;
        size = 0;
    }

    public GraphDeque() {
        this(DEQUE_DEFAULT_CAPACITY);
    }

    public void addFirst(V value) {
        // 判断是否需要扩容
        if (isFull()) {
            grow();
        }
        // 这里-1也就是是head的位置不插入数据，向前计算一个在插入，0就插入数组的尾部（循环数组）
        head = (head - 1) & (elements.length - 1);
        elements[head] = value;
        // 添加
        size++;
    }

    private void grow() {
        // 扩容后大小,扩容一倍
        int oldCap = elements.length;
        int nCap = oldCap << 1;
        if (nCap <= 0 || nCap > DEQUE_MAX_CAPACITY) {
            throw new IllegalArgumentException("deque size is over flow, max val:" + nCap);
        }

        // size -index 是包含index到数组结束的所有元素
        int hc = oldCap - head;
        // 尾指针指向下一个位置，重合的时候，刚好是尾指针+1，索引位置+1代表从0到索引位置的所有元素
        int tc = head;

        // 复制
        Object[] newElements = new Object[nCap];
        System.arraycopy(elements, head, newElements, 0, hc);
        System.arraycopy(elements, 0, newElements, hc, tc);

        // 指针位置更改，数组更换成扩容后的数组
        head = 0;
        tail = oldCap;
        elements = newElements;
    }

    public V removeFirst() {
        if (!isEmpty()) {
            V removeValue = (V) elements[head];
            head = (head + 1) & (elements.length - 1);
            elements[head] = null;
            size--;
            return removeValue;
        }
        return null;
    }

    public V peekFirst() {
        if (!isEmpty()) {
            return (V) elements[head];
        }
        return null;
    }

    public void addLast(V value) {
        if (isFull()) {
            grow();
        }
        // 相对于插入调整位置，是因为尾部插入，指针是指向下一个插入的位置指向的是null
        elements[tail] = value;
        tail = (tail + 1) & (elements.length - 1);
        size++;
    }

    public V removeLast() {
        if (!isEmpty()) {
            // tail 指向下一个插入位置，所以需要先计算正确索引 TODO 为啥出现了个null?
            tail = (tail - 1) & (elements.length - 1);
            V removeValue = (V) elements[tail];
            elements[tail] = null;
            size--;
            return removeValue;
        }
        return null;
    }

    public V peekLast() {
        if (!isEmpty()) {
            // 对索引的加减，防止越界，做成取模循环
            return (V) elements[(tail - 1) & (elements.length - 1)];
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isFull() {
        return size == elements.length;
    }

    public int size() {
        return size;
    }

    public int cap() {
        return elements.length;
    }
}

package com.yang.ds.datastruct.graph.impl;

import java.util.Arrays;

/**
 *
 * 图，深度优先使用的栈结构
 * */
public class GraphStack<V> {

    /**栈默认最大大小*/
    private static final int STACK_MAX_CAPACITY = 1 << 30; // max 2^30

    /**栈默认最大大小*/
    private static final int STACK_DEFAULT_CAPACITY = 1 << 4; // default 16

    /**数据存储*/
    private Object[] elements;

    /**栈顶指针*/
    private int top;

    /**元素个数*/
    private int size;

    public GraphStack() {
        this(STACK_DEFAULT_CAPACITY);
    }

    public GraphStack(int initCapacity) {
        if (initCapacity <= 0) {
            throw new IllegalArgumentException("illegal initCapacity " + initCapacity);
        }
        this.elements = new Object[initCapacity];
        size = 0;
        top = -1;
    }

    /**
     * 压栈
     * @param value
     * */
    public void push(V value) {
        // 扩容
        grow(size + 1);
        elements[++top] = value;
        size++;
    }

    /**
     * 出栈
     * @return V
     * */
    public V pop() {
        if (!isEmpty()) {
            size--;
            return (V) elements[top--];
        }
        return null;
    }

    public V peek() {
        if (!isEmpty()) {
            return (V) elements[top];
        }
        return null;
    }

    /**
     * 数组扩容
     * @param minCapacity 当前所需的最小容量
     * */
    private void grow(int minCapacity) {
        // 扩容
        if (minCapacity - elements.length > 0) {
            int newCapacity = elements.length << 1;
            if (newCapacity < 0 || newCapacity - STACK_MAX_CAPACITY > 0) {
                throw new IllegalArgumentException("stack over flow" + newCapacity);
            }
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

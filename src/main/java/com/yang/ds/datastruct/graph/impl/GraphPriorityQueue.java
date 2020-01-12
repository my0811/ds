package com.yang.ds.datastruct.graph.impl;

import com.yang.ds.algorithm.heap.Heap;

/**
 * 权重图的优先级队列,采用堆数据结构实现
 * */
public class GraphPriorityQueue<E extends Comparable<? super E>> {

    // 优先级堆
    Heap<E, E> heap;

    public GraphPriorityQueue() {
        heap = new Heap<>(5);
    }

    /**
     * 添加元素，如果堆满了，则抛出异常，相对的offer没有实现
     * @param e
     * */
    public void add(E e) {
        heap.add(e, e);
    }

    /**
     * 队头数据出队，也就是移除堆顶的元素
     *
     * */
    public E remove() {
        return heap.remove();
    }

    /**
     * 获取队头元素,返回堆顶元素
     *
     * */
    public E peek() {
        return heap.top();
    }

    /**
     * 重置队列，但是不删除堆中元素，复用原有内存空间，增加元素则覆盖，减少频繁清除堆数据，带来的复杂度
     *
     * */
    public void reset() {
        heap.reset();
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.size() == 0;
    }
}

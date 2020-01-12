package com.yang.ds.algorithm.linkedlist.iterator;

/**
 * 模拟一个抽象ADT
 */
public interface LinkedListInterface<T> extends LinkedListIterator<T>  {

    //返回当前链表的大小
    int getSize();

    // 链表头部添加新节点
    void addHead(T data);

    // 链表尾部添加新节点
    void addTail(T data);

}

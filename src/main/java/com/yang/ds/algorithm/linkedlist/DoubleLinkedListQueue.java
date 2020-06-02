package com.yang.ds.algorithm.linkedlist;

/**
 * 基于双端链表，实现队列
 * 链表尾部插入，头部删除，（为啥尾部插入？双端链表还是一个单向的链表，指针链只有一个方向,只有next,尾部删除不能获取上一个节点）
 */
public class DoubleLinkedListQueue {

    private DoubleEndedLinkedList doubleLinkedList;


    public DoubleLinkedListQueue() {
        this.doubleLinkedList = new DoubleEndedLinkedList();
    }


    /**
     * 添加
     * 1.尾部添加，头部删除
     */
    public void insert(Object data) {
        doubleLinkedList.addTail(data);
    }

    /**
     * 删除，出队
     */
    public Object pop() {
        return doubleLinkedList.deleteHead();
    }

    /**
     * 队列是否为空
     */
    public boolean isEmpty() {
        return doubleLinkedList.isEmpty();
    }

    /**
     * 获取队列的大小
     */
    public int getSize() {
        return doubleLinkedList.size();
    }

    public void display() {
        while (!isEmpty()) {
            System.out.println(pop());
        }
    }

    public static void main(String[] args) {
        DoubleLinkedListQueue queue = new DoubleLinkedListQueue();
        queue.insert(1);
        queue.insert(2);
        queue.insert(3);
        queue.display();
    }
}

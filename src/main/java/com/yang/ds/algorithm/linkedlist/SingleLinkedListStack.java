package com.yang.ds.algorithm.linkedlist;


/***
 *
 * 单向链表-栈实现(FILO)
 * 1.直接用单链表就可以实现栈了
 *--------------
 * head
 * |
 * ∨
 * A->B->C->D
 *---------------
 * */
public class SingleLinkedListStack {
    // 单链表
    private SingleWayLinkedList link;

    public SingleLinkedListStack() {
        this.link = new SingleWayLinkedList();
    }

    /**
     * 压栈
     */
    public void push(Object data) {
        link.addHead(data);
    }

    /**
     * 弹出栈顶元素
     */
    public Object pop() {
        return link.delHead();
    }

    /**
     * 是否为空栈
     */
    public boolean isEmpty() {
        return link.size() == 0;
    }

    public void printStack() {
        link.print();
    }

    public static void main(String[] args) {
        SingleLinkedListStack stackSingleLinkedList = new SingleLinkedListStack();
        stackSingleLinkedList.push(1);
        stackSingleLinkedList.push(2);
        stackSingleLinkedList.push(3);
        System.out.println(stackSingleLinkedList.pop());
    }
}

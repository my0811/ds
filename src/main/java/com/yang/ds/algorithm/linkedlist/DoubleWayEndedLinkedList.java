package com.yang.ds.algorithm.linkedlist;

/**
 * 双端双向链表(双向链表，node节点指向两个方向，双端则是两端都可以操作)
 * 双向链表就是一个队列，可以直接当队列用,头部添加尾部删除,尾部添加头部删除,一端添加，另一端删除，就可以实现队列
 */
public class DoubleWayEndedLinkedList {
    private Node head;
    private Node tail;
    private int size;

    public DoubleWayEndedLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * 数据节点
     */
    class Node {

        // 数据
        private Object data;

        // 下一个节点引用
        private Node next;

        // 上一个节点引用
        private Node prev;

        public Node(Object data) {
            this.data = data;
        }
    }

    public void addHead(Object data) {
        Node newNode = new Node(data);
        if (size == 0) {
            head = tail = newNode;
            size++;
            return;
        }
        newNode.next = head;
        head.prev = newNode;
        // 更改头结点指针
        head = newNode;
        size++;
    }

    public void addTail(Object data) {
        Node newNode = new Node(data);
        if (size == 0) {
            head = tail = newNode;
            size++;
            return;
        }
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
    }

    public Object deleteHead() {
        Object removeOjb = null;
        if (size == 0) { //空链表
            return removeOjb;
        } else if (size == 1) { // 只有一个元素
            removeOjb = head.data;
            head = tail = null;
        } else { // 多个元素
            removeOjb = head.data;
            head = head.next;
            head.prev = null;
        }
        size--;
        return removeOjb;
    }

    public Object delteTail() {
        Object removeObj = null;
        if (size == 0) {
            return removeObj;
        } else if (size == 1) {
            removeObj = head.data;
            head = tail = null;
        } else {
            removeObj = tail.data;
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return removeObj;
    }

    public boolean delete(Object data) {
        for (Node x = head; x != null; x = x.next) {
            if (x.data.equals(data)) {// 找到删除元素
                Object removeObj = x.data;
                // x的引用还在局部变量表中，不会被gc掉
                x.prev.next = x.next;
                x.next.prev = x.prev;
                // 两步操作猛如虎，直接就把这个元素到头部root节点head 和到尾部root节点的指针引用链切断，没人搭理x这个元素了，等jvm小哥哥GC掉就ok了
                size--;
                return true;
            }
        }
        return false;
    }

    public void print() {
        if (size == 0) {
            System.out.println("[]");
        } else if (size == 1) {
            System.out.println("[ " + head.data + " ]");
        }
        System.out.print("[");
        for (Node x = head; x != null; x = x.next) {
            System.out.print(x.data + " ");
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args) {
        DoubleWayEndedLinkedList doubleWayLinkedList = new DoubleWayEndedLinkedList();
        doubleWayLinkedList.addHead(1);
        doubleWayLinkedList.addHead(2);
        doubleWayLinkedList.addTail(3);
        doubleWayLinkedList.addTail(4);
        doubleWayLinkedList.addTail(5);
        doubleWayLinkedList.print();
        doubleWayLinkedList.delteTail();
        doubleWayLinkedList.deleteHead();
        doubleWayLinkedList.delete(3);
        doubleWayLinkedList.print();
    }
}

package com.yang.ds.algorithm.linkedlist;

/**
 * 双端链表，就很积累
 * 双端链表(无序)
 * 插入O(1)
 * 查找O(N)
 * 双端链表只是两端都可以添加数据，但是删除只能一端，但是遍历还是只能一个方向，不同于双向链表，双端链表指针的方向只有一个方向
 */
public class DoubleEndedLinkedList {

    // 链表大小
    private int size;

    // 头结点的指针引用
    private Node head;

    // 尾节点的指针引用
    private Node tail;

    class Node {

        private Object data;
        private Node next;

        public Node(Object data) {
            this.data = data;
        }
    }

    public DoubleEndedLinkedList() {
        this.size = 0;
        head = null;
        tail = null;
    }

    /**
     * 添加头结点
     */
    public Object addHead(Object data) {
        Node newNode = new Node(data);
        if (size == 0) {
            head = tail = newNode;
        } else {
            // ?是否会被GC呢？,注意引用关系，如果更改一个对象的引用关系的时候要考虑到这个对象的引用链是否失效
            // 否则会带来gc的问题
            System.gc();
            newNode.next = head;
            head = newNode;
        }
        size++;
        return newNode;
    }

    /**
     * 添加尾节点
     */
    public Object addTail(Object data) {
        Node newNode = new Node(data);
        if (size == 0) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return newNode;
    }

    /**
     * 删除头节点
     */
    public Object deleteHead() {
        Object removeObj = null;
        if (size == 0) {
            return removeObj;
        } else if (size == 1) { //双端链表，一定要考虑到边界，影响尾和头部的两个指针
            removeObj = head.data;
            head = tail = null;
        } else { // 正常的头结点删除,更改头结点的指向,当前的head数据对象没有了到root节点的引用路径会被GC
            removeObj = head.data;
            head = head.next;
        }
        size--;
        return removeObj;
    }


    /**
     * 查找数据
     */
    private Object findNode(Object data) {
        for (Node x = head; x != null; x = x.next) {
            if (x.data.equals(data)) {
                return x;
            }
        }
        return null;
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 删除指定元素
     */
    public boolean delete(Object data) {
        if (size == 0) {// 特殊情况，链表为空
            return false;
        } else if (size == 1) {// 只有一个节点的情况
            head = tail = null;
            size--;
            return true;
        }
        Node previous = head;
        for (Node x = head; x != null; x = x.next) {
            if (x.data.equals(data)) {//找到了需要删除的元素
                if (x == head) { //头元素
                    head = head.next;// 头元素指向，当前头元素的下一个元素,当前的头元素没有root引用会被gc
                } else if (x == tail) { // 尾元素
                    previous.next = null;//尾元素的next指针为null，数据结构定义要求
                    tail = previous;
                } else {// 中间元素
                    previous.next = x.next;
                }
            }
            previous = x;
        }
        size--;
        return true;
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

    public int size() {
        return size;
    }


    public static void main(String[] args) {
        DoubleEndedLinkedList doubleLinkedList = new DoubleEndedLinkedList();
        doubleLinkedList.addHead(1);
        doubleLinkedList.addTail(2);
        doubleLinkedList.addTail(3);
        //doubleLinkedList.deleteHead();
        doubleLinkedList.delete(3);
        doubleLinkedList.print();
    }
}

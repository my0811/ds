package com.yang.ds.algorithm.linkedlist;


/***
 *
 * 单向链表(无序)(单向链表一直都是在头部添加元素) 插入O(1),查找O(N)
 * 1. 链表的删除增加都是O(1)操作，常数操作，查找为O(N),因为查找，要从链上不断向下找
 * 2. 链表需要保存下一个节点 上一个节点(双向链表)的指针，所以空间上的占用会增加
 * 3. 插入从右向左，因为head一直在左移，所以打印的顺序正好是添加的顺序相反的
 *--------------
 * head
 * |
 * ∨
 * A->B->C->D
 *---------------
 *
 * */
public class SingleLinkedList {

    // 链表的节点数
    private int size;
    // 头节点
    private Node head;

    /**
     * 初始化，没有数据，头节点是null
     */
    public SingleLinkedList() {
        this.size = 0;
        head = null;
    }

    /**
     * 头部添加，新节点变成新的头节点,head指针移动
     */
    public Object addHead(Object data) {
        // 空链表的情况，直接就是一个node，就是头节点直接返回头就可以
        if (size == 0) {
            head = new Node(data);
        } else {
            Node newNode = new Node(data);
            newNode.next = head;
            head = newNode;
        }
        size++;
        return head;
    }

    public Object delHead() {
        if (head == null) {
            return null;
        }
        Object data = head.getData();
        head = head.next;
        size--;
        return data;
    }

    /**
     * 根据数据内容查找节点
     */
    private Node findNode(Object data) {
        for (Node x = head; x != null; x = x.next) {
            if (x.getData().equals(data)) {
                return x;
            }
        }
        return null;
    }

    public boolean delete(Object data) {
        if (size == 0) {
            return false;
        }
        Node previous = head;
        for (Node x = head; x != null; x = x.next) {
            if (x.getData().equals(data)) {
                //是否为头结点
                if (x == head) {
                    delHead();
                    break;
                } else {
                    // 直接指针更换,上一个的节点直接指向了当前节点的下一个节点，那么这个节点就没有引用链了，会被GC
                    previous.next = x.next;
                    size--;
                    break;
                }
            }
            previous = x;

        }
        return true;
    }

    public void print() {
        if (size == 0) {
            System.out.println("[]");
        } else if (size == 1) {
            System.out.println("[ " + head.getData() + " ]");
        }
        System.out.print("[");
        for (Node x = head; x != null; x = x.next) {
            System.out.print(x.getData() + " ");
        }
        System.out.print("]");
        System.out.println();
    }

    /**
     * 当前链表大小
     */

    public int size() {
        return size;
    }


    /**
     * node节点
     */
    class Node {

        //节点中的数据
        private Object data;

        // 指针，指向下一个节点
        private Node next;

        public Node(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "BinaryNode{" +
                    "data=" + data +
                    ", next=" + next +
                    '}';
        }
    }

    public static void main(String[] args) {
        SingleLinkedList singleLinkedList = new SingleLinkedList();
        singleLinkedList.addHead("A");
        singleLinkedList.addHead("B");
        singleLinkedList.addHead("C");
        singleLinkedList.addHead("D");
        System.out.println(singleLinkedList.findNode("D"));
        System.out.println(singleLinkedList.size());
        singleLinkedList.delete("B");
        singleLinkedList.delHead();
        singleLinkedList.delHead();
        singleLinkedList.delHead();
        singleLinkedList.print();
        System.out.println(singleLinkedList.size());
    }
}

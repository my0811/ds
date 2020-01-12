package com.yang.ds.algorithm.linkedlist.iterator;

import java.util.Iterator;

public class LinkedListWithIterator<T> implements LinkedListInterface<T> {
    private Node head;
    private Node tail;
    private int size;

    @Override
    public Iterator<T> iterator() {
        return new Iterator4LinkedList();
    }

    /**
     * node 节点
     */
    class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "BinaryNode{" +
                    "data=" + data +
                    ", next=" + next +
                    '}';
        }
    }

    private class Iterator4LinkedList implements Iterator<T> {

        private Node nextNode;

        public Iterator4LinkedList() {
            this.nextNode = head;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }
            Node rs = nextNode;
            nextNode = nextNode.next;
            return rs.data;
        }

        @Override
        public void remove() {

        }

    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void addHead(T data) {
        Node newNode = new Node(data);
        if (size == 0) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addTail(T data) {
        Node newNode = new Node(data);
        if (size == 0) {
            tail = head = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }
}

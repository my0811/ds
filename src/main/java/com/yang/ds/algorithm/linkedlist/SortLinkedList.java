package com.yang.ds.algorithm.linkedlist;

/**
 * 有序链表插入和删除都是O(N) 平均O(N/2) 去掉常数项，其实就是一个O(N)的复杂度，可以理解为O(N)
 * 1.因为必须要遍历链表才能找到插入和删除的位置
 */
public class SortLinkedList {
    private int size;
    private Node head;

    /**
     * 为了方便，定义数据类型为int比较方便比较，如果非要弄复杂了，可以定义对象实现Comparable接口
     * 这里就不啰嗦了
     * 数据排序方向，从head到链表尾部，从小到大
     */
    class Node {
        private Integer data;
        private Node next;

        public Node(Integer data) {
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

    public SortLinkedList() {
        size = 0;
        head = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int deleteHead() {
        if (isEmpty()) {
            throw new IllegalArgumentException("empty exception");
        }
        int data = head.data;
        head = head.next;
        size--;
        return data;
    }

    public void insert(Integer data) {
        Node newNode = new Node(data);
        if (isEmpty()) {// 空链表
            head = newNode;
            size++;
            return;
        }
        // 链表不为空，遍历整个链 复杂度O(N)
        Node previous = head;
        Node curNode;
        for (curNode = head; curNode != null; curNode = curNode.next) {
            if (curNode.data > data) { //找到了插入位置,执行插入跳出循环
                // 头结点判断，需要移动头结点指针
                if (curNode == head) {
                    newNode.next = head;
                    head = newNode;
                } else {
                    previous.next = newNode; /**1->2->3->[3]->4(3为需要插入的元素) ***/
                    newNode.next = curNode;
                }
                break;
            }
            previous = curNode;
        }
        // 尾节点判断
        if (curNode == null) {// 链表全部循环一遍结束之后，所有元素都比插入的元素小,那插入的元素就是最后一个
            previous.next = newNode;
        }
        size++;
    }

    public void print() {
        if (size == 0) {
            System.out.println("[ ]");
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
        SortLinkedList sortLinkedList = new SortLinkedList();
        sortLinkedList.insert(1);
        sortLinkedList.insert(9);
        sortLinkedList.insert(7);
        sortLinkedList.insert(5);
        sortLinkedList.print();
    }
}

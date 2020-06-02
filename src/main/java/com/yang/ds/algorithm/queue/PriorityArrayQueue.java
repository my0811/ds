package com.yang.ds.algorithm.queue;

/**
 * 优先级队列，数组实现，
 * 1.首先就是一个有序数组
 * 2.插入的时候和有序数组是一样的
 * 3.出队的时候，就是一直取第一个元素位置
 * 4.插入元素复杂度为O(N)
 * 5.出队复杂度为常数O(1)
 * 6.数组最后的元素就是最大、或者最小的元素
 * []  6
 * []  5
 * [1] 4  |
 * [2] 3  |
 * [5] 2  |
 * [6] 1  |
 * [7] 0  ∨
 * <p>
 * 插入 "3"
 * []  6
 * [1] 5
 * [2] 4
 * []  3---------->3会插入到这个位置，比3小的一直往后挪
 * [5] 2
 * [6] 1
 * [7] 0
 */
public class PriorityArrayQueue {

    // 用int来代替object数组，方便排序，否则对象实现compareble接口，实现比较逻辑
    private int[] elements;

    // 队列中的元素个数
    private int size;

    // 数组最大长度
    private int maxSize;

    public PriorityArrayQueue(int size) {
        this.elements = new int[size];
        this.maxSize = size;
        this.size = 0;
    }

    /**
     * 插入数据
     */

    public void insert(int data) {
        // 判断队列是否已经满了
        if (size == maxSize) {
            throw new RuntimeException("queue is full");
        }
        if (size == 0) {// 队列为空
            elements[size++] = data;
        } else {
            // 数组最后面的位置的数据是最小的
            int p = size - 1;

            while (p >= 0 && data > elements[p]) {
                elements[p + 1] = elements[p];
                p--;
            }
            // 因为不断的把数据向后移动，所以判断到最后一个不比自己小的数据，上一个位置已经是空的了，也就是要存放的数据的位置
            elements[p + 1] = data;
            size++;
        }
    }

    /**
     * 查看元素
     */

    private int peek() {
        if (size == 0) {
            throw new RuntimeException("queue is empty");
        }
        return elements[size - 1];
    }

    /**
     * 出队，删除元素
     */

    private int remove() {
        if (size == 0) {
            throw new RuntimeException("queue is empty");
        }
        int removeData = elements[size - 1];
        elements[size - 1] = -1;
        size--;
        return removeData;
    }

    public static void main(String[] args) {
        PriorityArrayQueue priorityArrayQueue = new PriorityArrayQueue(5);
        priorityArrayQueue.insert(100);
        priorityArrayQueue.insert(1);
        priorityArrayQueue.insert(200);
        priorityArrayQueue.insert(50);
        while (!priorityArrayQueue.isEmpty()) {
            System.out.println(priorityArrayQueue.remove());
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

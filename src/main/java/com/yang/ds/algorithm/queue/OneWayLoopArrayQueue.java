package com.yang.ds.algorithm.queue;

/**
 *
 *
 *
 * 基于数组实现，单向循环队列
 * 1. 针对于数组实现
 * 2. 循环队列,指针到达数组的最后一个下标，重新返回数组第一个坐标
 * 复杂度:
 * 入队:,出队都是O(1),因为删除队列的元素，不需要将数组元素移动，并且对应的位置元素可以服用
 *
 * 对头指针，和队尾指针要一直根据数据的添加和删除，变化，一直指向
 */
public class OneWayLoopArrayQueue {

    // 队列存储元素数组
    private Object[] elementData;

    //数组最大长度，队列最大长度
    private int maxSize;

    //当前队列中的数据个数
    private int nElements;

    // 队列后端指针
    private int rear;

    // 队列前端指针
    private int front;

    public OneWayLoopArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        this.elementData = new Object[maxSize];
        this.rear = -1;
        this.front = 0;
        this.nElements = 0;
    }

    /**
     * 循环队列，如果插入到数组最后一个位置，让队列插入端指针，重新指向数组最开始的下标
     * 入队，队尾入队
     */

    public boolean insert(Object data) {
        if (isFull()) {
            System.out.println("queue is full now size is " + size() + "maxSize is " + maxSize);
            return false;
        }
        // 循环队列,已经到了数组最后一个元素位置，则指标循环指向数组开始
        if (rear == maxSize - 1) {
            rear = -1;
        }
        elementData[++rear] = data;
        nElements++;
        return true;
    }

    /**
     * peek 查看
     */

    private Object peek() {
        if (isEmpty()) {
            System.out.println("queue is empty !");
            return null;
        }
        return elementData[front];
    }

    /**
     * 出队,队头出队，删除元素
     */
    private Object remove() {
        if (isEmpty()) {
            System.out.println("queue is empty !");
            return null;
        }
        Object removeObj = elementData[front];
        elementData[front] = null;// help GC
        nElements--;
        if (front == maxSize - 1) {
            front = 0;
        } else {
            front++;
        }
        return removeObj;
    }

    /**
     * 队列是否为空
     */
    private boolean isEmpty() {
        return nElements == 0;
    }

    /**
     * 队列是否已经满
     */
    private boolean isFull() {
        return nElements == maxSize;
    }

    /**
     * 队列当前大小
     */
    private int size() {
        return nElements;
    }

    public static void main(String[] args) {
        OneWayLoopArrayQueue wayLoopArrayQueue = new OneWayLoopArrayQueue(3);
        System.out.println("remove....");
        wayLoopArrayQueue.remove();
        System.out.println("insert....");
        wayLoopArrayQueue.insert("1");
        wayLoopArrayQueue.insert("2");
        wayLoopArrayQueue.insert("3");
        wayLoopArrayQueue.insert("4");
        System.out.println("peek....");
        System.out.println(wayLoopArrayQueue.peek());
        System.out.println("remove....");
        System.out.println(wayLoopArrayQueue.remove());
        System.out.println("insert 4");
        wayLoopArrayQueue.insert("4");
        System.out.println("while queue");
        while (!wayLoopArrayQueue.isEmpty()) {
            System.out.println(wayLoopArrayQueue.remove());
        }
    }
}

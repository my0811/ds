package com.yang.ds.algorithm.window;

/**
 * 滑动窗口最大值
 *
 * 剑指offer算法题
 * easy 的球一样，浪费老子时间，本来想研究一下窗口算法干毛用的
 *
 * */

public class SlideWindowMaxValue {
    public static void main(String[] args) {
        int wLen = 3;
        // 一共六个窗口2,3,4 3,4,2,4,2,6 .....窗口大小是3，每次滑动一个元素,相当于一个元素出队，然后一个元素入队
        int[] arr = new int[]{2, 3, 4, 2, 6, 2, 5, 1};
        Dequeue dequeue = new Dequeue(3);
        StringBuilder sb = new StringBuilder();
        // 擦，普通队列就能搞定啊，搞什么双端队列，浪费，擦
        for (int i = 0; i < arr.length; i++) {
            if (i < wLen) {
                dequeue.addLast(arr[i]);
                continue;
            }
            // 求窗口最大值
            sb.append(dequeue.max()).append(" ");
            dequeue.removeFirst();
            dequeue.addLast(arr[i]);
        }
        // 最后一次退出循环少了一次计算
        sb.append(dequeue.max());
        System.out.println(sb.toString());
    }

    /**
     * 定长数组，双端队列
     * */
    static class Dequeue {
        private int head;
        private int tail;
        private int size;
        int[] elements;

        public Dequeue(int cap) {
            if (cap <= 0) {
                throw new IllegalArgumentException("queue cap is error " + cap);
            }
            head = tail = (cap - 1) >>> 1;
            elements = new int[cap];
        }

        public void addFirst(int e) {
            if (size > elements.length) {
                throw new IllegalArgumentException("queue size is over max size");
            }
            head = getMod(head - 1);
            elements[getMod(head)] = e;
            size++;
        }

        public void addLast(int e) {
            if (size > elements.length) {
                throw new IllegalArgumentException("queue size is over max size");
            }
            elements[tail] = e;
            // 尾指针指向下一次插入的位置
            tail = getMod(tail + 1);
            size++;
        }

        public int removeFirst() {
            int e = elements[head];
            head = getMod(head++);
            size--;
            return e;
        }

        public int removeLast() {
            tail = getMod(tail - 1);
            int e = elements[tail];
            size--;
            return e;
        }

        public int max() {
            return Math.max(Math.max(elements[0], elements[1]), elements[2]);
        }

        private int getMod(int idx) {
            if (idx < 0) {
                return idx + elements.length;
            } else if (idx >= elements.length) {
                return idx - elements.length;
            } else {
                return idx;
            }
        }
    }
}

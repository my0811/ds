package com.yang.ds.algorithm.stack;

/**
 * 简单认识一下栈数据结构
 * 存在问题:
 * 1. 不能自动扩容
 * 2. 数组实现，类型只能是一种，除非声明数组类型为object
 * 3. 数组大小必须要指定，可以用链表来达到无限增长
 */

public class SimpleStack {

    // 栈保存元素数组
    private long[] elemData;

    // 栈最大深度
    private int maxSize;

    // 栈顶指针，始终指向栈顶第一个数据元素,栈为空的话默认值为-1
    private int top = -1;

    public SimpleStack(int maxSize) {
        this.elemData = new long[maxSize];
        this.maxSize = maxSize;
        this.top = top;
    }

    /***
     * 压栈，进战
     * */
    public void push(long data) {
        // 判断栈是否已经满了
        if (isFull()) {
            throw new IllegalArgumentException("over stack");
        }
        elemData[++top] = data;
    }

    public long pop() {
        if (isEmpty()) {
            throw new IllegalArgumentException(" stack is empty");
        }
        return elemData[top--];
    }

    public long peek() {
        return elemData[top];
    }

    public boolean isFull() {
        return ((maxSize - 1) == top);
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public static void main(String[] args) {
        SimpleStack simpleStack = new SimpleStack(3);
        simpleStack.push(1);
        simpleStack.push(2);
        simpleStack.push(3);
        while (!simpleStack.isEmpty()){
            System.out.println(simpleStack.pop());

        }
    }
}

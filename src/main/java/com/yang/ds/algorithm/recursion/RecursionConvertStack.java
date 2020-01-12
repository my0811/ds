package com.yang.ds.algorithm.recursion;

import java.util.Stack;

/**
 * 递归的执行效率不是太高，而且对于方法的执行，虚拟机都会不断的开辟栈的空间
 * 用循环和栈的数据结构存储来消除递归带来的影响
 * 程序需求：
 * 求1+2+3+...n-1+n
 */
public class RecursionConvertStack {
    public static void main(String[] args) {
        System.out.println(addNRecursion(100));
        System.out.println("stack-----------start");
        System.out.println(addNWhioutRecursion(100));
        System.out.println("stack-----------end");
    }

    // 递归实现1+...n
    private static int addNRecursion(int n) {
        if (n == 1) {// 边界条件
            return n;
        }
        return n + addNRecursion(n - 1);
    }

    // 消除递归实现1+...n,消除递归是依靠栈来消除，因为递归实际上就是方法栈的执行，符合栈数据结构
    public static int addNWhioutRecursion(int n) {

        // 栈数据结构代替，方法递归的方法栈
        Stack<Params> stack = new Stack<>();

        // 当前返回的值
        int curRetrunVal = 0;

        // 当前的返回值地址
        int curRetrunAdr = 1;

        // 参数
        Params params = null;

        boolean flag = true;
        while (flag) {
            // 返回地址判断
            switch (curRetrunAdr) {

                // 初始化参数封装为对象压入栈 push,设置下一步走的分支地址第-->2
                case Params.ADR_INIT:
                    params = new Params(n, 6);
                    stack.push(params);
                    // 指定为第二个分支
                    curRetrunAdr = Params.ADR_BORDER;
                    break;

                // 模拟递归算法的边界条件,判断递归的边界条件
                case Params.ADR_BORDER:
                    params = stack.peek();
                    if (params.getN() == 1) {// 如果已经到了递归边界值，则直接执行出栈
                        curRetrunVal = params.getN();
                        curRetrunAdr = Params.ADR_POP;
                    } else {// 没有到达边界值继续执行递归的前进阶段
                        curRetrunAdr = Params.ADR_FORWARD_STAGE;
                    }
                    break;

                // 模拟递归前进段,n=n-1作为新的参数，递归执行，也就是压栈 --->ADR_BORDER
                case Params.ADR_FORWARD_STAGE:
                    params = stack.peek();
                    // 前进段为每个入栈元素设定返回地址Params.ADR_BACKWARD_STAGE,模拟方法返回地址
                    params = new Params(params.getN() - 1, Params.ADR_BACKWARD_STAGE);
                    stack.push(params);
                    curRetrunAdr = Params.ADR_BORDER;
                    break;

                // 递归后近段的模拟,//后近段，后近段就要执行n+(n-1)
                case Params.ADR_BACKWARD_STAGE:
                    params = stack.peek();
                    curRetrunVal += params.getN();
                    // 执行完计算之后，出栈
                    curRetrunAdr = Params.ADR_POP;
                    break;

                // 执行出栈，后进段上一次递归的运算已经结束
                case Params.ADR_POP:
                    params = stack.pop();
                    // 因为入队元素已经保存了出栈之后回到的调用位置，模拟方法完成调用返回地址
                    curRetrunAdr = params.getRetrunAddr();
                    break;

                // 完成处理，结束循环
                case Params.ADR_END:
                    flag = false;
                    break;
                default:
                    throw new IllegalArgumentException("unknow error case ");
            }
        }
        return curRetrunVal;
    }

    /**
     * 封装一个类似于方法调用的栈帧入栈的一个对象封装，方便完成代码逻辑的实现
     */
    static class Params {

        // 初始化
        public static final int ADR_INIT = 1;

        // 边界值判断
        public static final int ADR_BORDER = 2;

        // 递归前进段
        public static final int ADR_FORWARD_STAGE = 3;

        //递归后进段
        public static final int ADR_BACKWARD_STAGE = 4;

        //递归结束退出调用栈
        public static final int ADR_POP = 5;

        // 递归调用完成，结束
        public static final int ADR_END = 6;

        // 返回值
        private int n;

        // 返回地址
        private int retrunAddr;

        public Params(int n, int retrunAddr) {
            this.n = n;
            this.retrunAddr = retrunAddr;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public int getRetrunAddr() {
            return retrunAddr;
        }

        public void setRetrunAddr(int retrunAddr) {
            this.retrunAddr = retrunAddr;
        }
    }
}

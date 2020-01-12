package com.yang.ds.algorithm.recursion;

import java.util.Scanner;

/**
 * 阶乘
 * 递归还有个操蛋的情况就是
 */
public class Factorial {
    public static void main(String[] args) {
        System.out.println("请输入一个整数");
        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            int n = scanner.nextInt();
            // for 循环
            //factorialByFor(n);
            // 递归的一个操蛋情况就是不能递归次数太多，超出jvm栈的深度，每个方法都需要开辟栈,比如输入99999999，直接gg了
            System.out.println(n + "! = " + factorialByrecursion(n));
        }

    }

    private static void factorialByFor(int num) {
        int rs = 1;
        if (num > 0) {
            for (int i = 1; i <= num; i++) {
                rs *= i;
            }
        }
        System.out.println("for 循环方式获取[" + num + "]的阶乘为: " + rs);
    }

    private static int factorialByrecursion(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("负数没有阶乘");
        }
        //边界值
        if (num == 0) {
            return 1;
        }
        // 运算规则，需要用递归的返回值怎么进行计算
        num *= (factorialByrecursion(--num));// 这个操作就是让条件无限的接近边界值 num=0
        // 进阶段
        return num;
    }

}

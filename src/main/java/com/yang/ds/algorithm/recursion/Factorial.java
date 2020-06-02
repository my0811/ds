package com.yang.ds.algorithm.recursion;

/**
 * 阶乘
 * 分治思想，递归求阶乘，每个方法依赖于后面的更小的数计算结果，最后合并
 *
 * 只是用递归来练习，这种简单的处理最好不要用递归，因为如果非常大的数，栈的深度是有限的
 *
 * 二叉树这种很适合高度，2^64能存储很多数据，但是树的高度才是64,64次递归搞定
 *
 */
public class Factorial {
    private int factorial(int num) {
        if (num <= 0) {
            return -1;
        }
        int rs = 1;
        for (int i = 1; i <= num; i++) {
            rs *= i;
        }
        return rs;
    }

    private int factorialRec(int num) {
        // 递归边界
        if (num <= 0) {
            return 1;
        }
        // 分治思想，每次此行代码定位在这里，等待其他方法返回，最先入栈，最后出栈
        return num * factorialRec(num - 1);
    }
}

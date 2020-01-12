package com.yang.ds.algorithm;

/**
 * 算法-递归
 * 随便给一个数
 * 1.就是取最后一位的余数，当做是最末尾的值，对10取模，肯定是小于10的(0-9)的数字，也就是最后一位
 * 2.然后就是把数字逐渐的去除10，不断的向上取上一位的值
 */
public class Recursion {
    // 单元测试
    public static void main(String[] args) {
        numPrint(1024);
    }

    /**
     * 递归打印
     */
    private static void numPrint(int num) {
        if (num >= 10) {
            numPrint(num / 10);
        }
        System.out.println(num % 10);
    }
}

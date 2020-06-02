package com.yang.ds.algorithm.alibaba;

/**
 * int 类型反转，不能借助于java 工具类，不能用string
 *
 * 思想：就是与位相加正向的逆运算，给定数据不断/10,然后新生成的数据不断*10,然后加上每一位的有效值
 *
 * */

public class IntegerRevert {
    public static void main(String[] args) {
        System.out.println(revert(54321));
    }


    private static int revert(int num) { // 5,4,3,2,1
        long result = 0;
        int tmp = num;
        for (; tmp != 0; tmp = tmp / 10)
            result = result * 10 + tmp % 10;
        if (result > Integer.MAX_VALUE)
            throw new IllegalArgumentException("value is over Integer.MAX_VALUE");
        return (int) result;
    }
}

package com.yang.ds.algorithm.recursion;


/**
 * 递归解决汉诺塔问题,要求从大到小的盘子进行移动，并且移动过程中，每个盘子不能放到比自己小的盘子上面,把A中的
 * 所有盘子移动到C上面
 * 这个是递归的小入门，双递归函数，分治算法的核心思想，后面的二叉树都会根据这个算法来进行处理
 * 参考一个博客说的很明白
 * https://baijiahao.baidu.com/s?id=1630034809552470725&wfr=spider&for=pc
 * **___
 * *______
 * _________
 * A          B      C
 */
public class HanoiTower {

    private static int sc = 0;

    public static void main(String[] args) {
        /**
         * -   1 |   |
         * --  2 |   |
         * --- 3 |   |
         * A     B   C
         * */
        hanoi(3, "A", "B", "C");
    }

    /**
     * 递归实现汉诺塔问题，
     * 1.其实把汉诺塔的问题，看做成是一个两个盘子的移动的问题，然后不断的递归
     * 2.换图剖析代码的执行流程
     */
    public static void hanoi(int dish, String A, String B, String C) {
        // 递归边界值，如果为1的时候，就是一个盘子的问题，直接从A到C
        if (dish == 1) {
            move(dish, A, C);
        } else {
            hanoi(dish - 1, A, C, B);//N-1个盘子由A经过C到达B
            move(dish, A, C);
            hanoi(dish - 1, B, A, C);//N-1个盘子由B经过A到达C
        }
    }

    private static void move(int dish, String from, String to) {
        sc++;
        System.out.println("step" + sc + " move(" + dish + ") " + from + "--->" + to);
    }
}

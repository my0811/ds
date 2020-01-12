package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 希尔排序-插入排序的基础优化
 * TODO 算法复杂度
 */
public class ArrayShellSort {


    /**
     * 插入排序
     */

    private static void insertSort(int[] arr) {
        if (null == arr || arr.length <= 0) {
            throw new IllegalArgumentException("error array params！");
        }
        if (arr.length == 1) {
            return;
        }
        // 实现插入排序
        int inner;
        int outer;
        int tmp;
        for (outer = 1; outer < arr.length; outer++) {
            tmp = arr[outer];
            inner = outer;
            for (; inner > 0 && arr[inner - 1] > tmp; ) {
                arr[inner] = arr[--inner];
            }
            arr[inner] = tmp;
        }
    }

    /**
     * 希尔排序
     * ps:当step等于1的时候其实不就是插入排序吗？只不过这个时候插入排序已经不需要移动那么多次数了，因为答题的顺序已经差不多了，就是微调一下就行了
     * 不会出现那种极端的情况，一个元素前面的元素全部都比自己大，出现O(N)的情况
     */

    public static void shellSort(int[] arr) {
        // 边界异常处理
        if (null == arr || arr.length <= 0) {
            throw new IllegalArgumentException("error array params！");
        }
        if (arr.length == 1) {
            return;
        }
        // 希尔排序
        int outer;
        int inner;
        int tmp;
        int len = arr.length;
        // 最外层循环，控制每次插入移动的间隔,最后一一次就是一个插入排序，间隔为1
        // 最好的间隔是所有的step间隔互为质数，（没有公约数，除了1和本身）,step/2.2是一种采取的方式
        for (int step = len / 2; step > 0; step = step / 2) {
            // 插入排序的最外层循环
            for (outer = step; outer < len; outer++) {
                inner = outer;
                tmp = arr[outer];
                // 插入排序的位置移动
                for (; inner - step >= 0 && tmp < arr[inner - step]; ) {
                    arr[inner] = arr[inner - step];
                    inner = inner - step;
                }
                // 插入排序目标位置插入
                arr[inner] = tmp;
            }
            System.out.println("间隔:[" + step + "] :" + Arrays.toString(arr));
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{4, 2, 1, 6};
        System.out.println(Arrays.toString(arr));
        insertSort(arr);
        System.out.println(Arrays.toString(arr));
        System.out.println("shell--------------------------");
        int[] arr2 = new int[]{4, 2, 1, 6, 9, 2, 3, 5, 1, 1};
        System.out.println("排序前: " + Arrays.toString(arr2));
        shellSort(arr2);
        System.out.println("排序后：" + Arrays.toString(arr2));
    }

}

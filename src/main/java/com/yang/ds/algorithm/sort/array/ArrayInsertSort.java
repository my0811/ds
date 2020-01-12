package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 插入排序算法
 */
public class ArrayInsertSort {
    public static void main(String[] args) {
        Integer[] arr = new Integer[]{3, 1, 4, 6};
        System.out.println(Arrays.toString(arr));
        insertSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 插入排序
     * I.头脑要想象出，插入排序的动态效果图
     */

    private static void insertSort(Integer[] arr) {
        int outer;
        int inner;
        int tmp;
        // 内边界和外边界在数组的第二个位置开始的
        for (outer = 1; outer < arr.length; outer++) {
            inner = outer;
            tmp = arr[outer];
            while (inner > 0 && arr[inner - 1] > tmp) {
                // 大的向后移动
                arr[inner] = arr[inner - 1];
                --inner;
            }
            // 移动完成，插入空位
            arr[inner] = tmp;
        }
    }
}

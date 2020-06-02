package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 数组选择排序
 * 算法复杂度O(N2)
 * 选择排序与冒泡比较，就是数组位置交换的次数减少了，变成N-1次,而冒泡排序则需要最差(n-1)*(n-2)次交换
 */
public class ArraySelectSort {
    public static void main(String[] args) {
        Integer[] arr = new Integer[]{3, 1, 4, 1};
        System.out.println(Arrays.toString(arr));
        selectSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * I.外层循环
     * 1. 外层循环的次数为数组长度-1 N-1次，每一次处理都会出现一个最小值，所以和冒泡排序是一样的最后一次能确定两个元素的最终位置
     * II.内层循环
     * 1. 每次都处理第一个元素之后的剩余元素的比较
     *
     * 选择排序，每次内层循环迭代一次，则选出一个最小的，用一个变量维护，减少了数组位置的交换，外层循环一次
     * 交换一次数组，用N-1次数组交换，而冒泡则是N^2级别的数组交换次数,相比冒泡要优化一下
     * 但是排序复杂度还是在O(N^2),数组的交换也是非常快，几乎没啥大区别
     *
     */
    private static void selectSort(Integer[] arr) {
        int minIndex;
        for (int outer = 0; outer < (arr.length - 1); outer++) {
            minIndex = outer;
            for (int inner = outer + 1; inner < arr.length; inner++) {
                if (arr[minIndex] > arr[inner]) {
                    minIndex = inner;
                }
            }
            int tmp = arr[outer];
            arr[outer] = arr[minIndex];
            arr[minIndex] = tmp;
        }
    }
}

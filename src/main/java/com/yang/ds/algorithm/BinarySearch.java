package com.yang.ds.algorithm;

/***
 *
 * 算法，二分查找
 * 1. 数据越多算法越体现出优势，对数的增长，函数曲线比较平稳
 * 2. 数据必须是有序的
 * log2N=log10N*3.322
 * 10 ----4
 * 100----7
 * 1000 ---10
 * 10000---14
 * 1000000000---30
 *
 */

public class BinarySearch {


    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        int value=binarySearch(9, arr);
        System.out.println("value: "+value);

    }

    private static int binarySearch(int num, Integer[] arr) {
        int low = 0, high = arr.length-1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid].compareTo(num) > 0) { // 折中，大于中间值
                high = mid - 1;
             } else if (arr[mid].compareTo(num) < 0) {
                low = mid + 1;
            } else return mid;
        }
        return -1;


    }
}

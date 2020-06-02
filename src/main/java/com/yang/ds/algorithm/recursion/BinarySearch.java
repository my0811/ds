package com.yang.ds.algorithm.recursion;

/**
 * 递归实现二分查找 O(logN)
 * 递归有时候想不明白就想一想分治算法的思想，大的问题觉得复杂，就先想最后一个小问题，然后把小问题递归处理
 */
public class BinarySearch {


    /**
     * 二分查找，循环处理思想
     * */
    private int binarySearch(int key, int[] arr) {
        if (null == arr || arr.length <= 0) {
            return -1;
        }
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (key < arr[mid])
                high = mid - 1;
            else if (key > arr[mid])
                low = mid + 1;
            else
                return mid;
        }
        // -0的情况排除，可能low的值没变，后者比low还小,所以要+1在取反，用的时候自行取反
        return -(low + 1);
    }

    /**
     * 递归处理二分查找，分治算法思想
     *
     * */
    private int bsRec(int low, int high, int key, int[] arr) {
        // 校验
        if (null == arr || arr.length <= 0) {
            return -1;
        }
        // 递归边界值
        if (low > high) {
            return -(low + 1);
        }
        int mid = (low + high) >>> 1;
        if (key > arr[mid]) {
            // 分治，左半部分数组
            return bsRec(mid + 1, high, key, arr);
        } else if (key < arr[mid]) {
            // 分治，有半部分数组
            return bsRec(low, mid - 1, key, arr);
        } else {
            // 命中
            return mid;
        }
    }
}

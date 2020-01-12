package com.yang.ds.algorithm.recursion;

/**
 * 递归实现二分查找
 * 递归有时候想不明白就想一想分治算法的思想，大的问题觉得复杂，就先想最后一个小问题，然后把小问题递归处理
 */
public class BinarySearch {


    public static int binarySearchFor(int[] arr, int val) {
        int low = 0;
        int high = arr.length - 1;
        int mid;
        for (; low <= high; ) {
            mid = (low + high) / 2;
            //查找方向在上边界
            if (arr[mid] < val) {
                low = mid + 1;
            } else if (arr[mid] > val) {//查找方向在下边界
                high = mid - 1;
            } else {
                return mid;
            }
        }
        // 没有查到
        return -1;
    }

    /**
     * 递归实现二分查找
     */

    public static int binarySearchRecursion(int[] arr, int val, int low, int high) {
        /**
         * 中间值
         * */
        int mid = (low + high) / 2;
        // 边界条件处理,必须要第一个考虑，递归什么时候结束
        if (arr[mid] == val) {
            return mid;
        } else if (low > high) {
            return -1;
        }
        /**
         * 没有到达边界值
         * */
        if (arr[mid] > val) { //在数组的下边界
            return binarySearchRecursion(arr, val, low, mid - 1); //无论调用多少次，都会不断向上返回，返回之后就直接return
        } else if (arr[mid] < val) {//在数组上边界
            return binarySearchRecursion(arr, val, mid + 1, high);
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 6};
        System.out.println("binarySearchFor index: " + binarySearchFor(arr, 4));
        System.out.println("binarySearchRecursion index:" + binarySearchRecursion(arr, 4, 0, arr.length - 1));
    }
}

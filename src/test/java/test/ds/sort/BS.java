package test.ds.sort;

import org.junit.Test;

/**
 * 二分查找实现
 * */
public class BS {


    @Test

    public void binarySearchTest() {
        int[] arr = new int[]{1, 5, 7, 9, 10};
        int idx = binarySearch(7, arr);
        System.out.println("search idx:" + idx);
        System.out.println("-0:" + -0);
    }

    @Test

    public void bsRecTest() {
        int[] arr = new int[]{1, 5, 7, 9, 10};
        int idx = bsRec(0, arr.length - 1, 6, arr);
        System.out.println("bsRec search idx:" + idx);
    }

    /**
     * 二分查找，循环处理思想
     * */
    private int binarySearch(int key, int[] arr) {
        int low = 0;
        int high = arr.length - 1;
        for (; low <= high; ) {
            int mid = (low + high) >>> 1;
            if (key < arr[mid]) {
                high = mid - 1;
            } else if (key > arr[mid]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    /**
     * 递归处理二分查找，分治算法思想
     *
     * */

    private int bsRec(int low, int high, int key, int[] arr) {
        // 递归边界
        if (low > high) {
            return -(low + 1);
        }
        int mid = (low + high) >>> 1;
        if (key < arr[mid]) {
            return bsRec(low, mid - 1, key, arr);
        } else if (key > arr[mid]) {
            return bsRec(mid + 1, high, key, arr);
        } else {
            return mid;
        }
    }
}

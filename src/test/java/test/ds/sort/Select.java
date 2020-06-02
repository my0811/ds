package test.ds.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 选择排序，相比冒泡排序减少了交换的次数,比较次数没有减少，但是减少了数组的交换
 * <p>
 * N-1此外城循环，只需要N-1次交换 而冒泡的数组交换次次数在N^2级别
 * <p>
 * 复杂度O(N^2)
 */

public class Select {

    @Test

    public void selectTest() {
        int[] arr = new int[]{8, 7, 2, 4, 10, 11, 3, 15};
        selectSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 外层需要N-1次数，即可，因为最后一个元素不需要再比较
     */
    private static void selectSort(int[] arr) {
        for (int o = 0; o < arr.length - 1; o++) {
            int min = o;
            for (int i = o + 1; i < arr.length; i++) {
                if (arr[i] < arr[min]) {
                    min = i;
                }
            }
            if (min != o) {
                int tmp = arr[min];
                arr[min] = arr[o];
                arr[o] = tmp;
            }
        }
    }
}

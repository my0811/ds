package test.ds.sort;


import org.junit.Test;

import java.util.Arrays;

/**
 * 无序数组的冒泡排序
 * <p>
 * 算法复杂度O(N^2)
 */
public class Bubbling {

    @Test
    public void bubblingSortTest() {
        int[] arr = new int[]{5, 4, 3, 9, 1, 8};
        bubblingSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    // 冒个小泡
    private static void bubblingSort(int[] arr) {
        for (int o = arr.length - 1; o > 0; o--) {
            for (int i = 0; i < o; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                }
            }
        }
    }
}

/*
package test.ds.sort;

import org.junit.Test;

import java.util.Arrays;

*/
/**
 * 希尔排序
 * *//*

public class Shell {

    public static void main(String[] args) {


    }

    @Test
    public void shellSortTest() {
        int[] arr = new int[]{6, 5, 4, 3, 2, 1, 8, 9, 7};
        shellSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void shell3StepSortTest() {
        int[] arr = new int[]{6, 5, 4, 3, 2, 1, 8, 9, 7};
        shell3StepSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void shellSort(int[] arr) {
        int step;
        int len = arr.length;
        for (step = len / 2; step > 0; step = (step / 2)) {
            for (int o = step; o < len; o++) {
                int i = o;
                int tmp = arr[i];
                for (; i - step >= 0 && arr[i - step] > tmp; i -= step) {
                    arr[i] = arr[i - step];
                }
                arr[i] = tmp;
            }
        }
    }

    private static void shell3StepSort(int[] arr) {

        int step = 1;
        while (step <= arr.length / 3) {
            step = step * 3 + 1;
        }
        for (; step > 0; step = (step - 1) / 3) {
            for (int o = step; o < arr.length; o++) {
                int i = o;
                int tmp = arr[o];
                for (; i - step >= 0 && arr[i - step] > tmp; i -= step) {
                    arr[i] = arr[i - step];
                }
                arr[i] = tmp;
            }
        }
    }
}
*/

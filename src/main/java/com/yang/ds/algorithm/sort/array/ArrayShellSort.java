package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 希尔排序-插入排序的基础优化
 * 复杂度O(N^3/2)
 * 证明希尔排序的算法复杂度在O(N^3/2),是一个平方阶范围，要比O(N^2)优化一些，但是还不能与
 * 归并、快排、堆排序相提并论，归并、快排、堆，都是在NlogN的范围的算法复杂度
 *
 * 希尔排序也是一个贪婪算法，首先插入排序就算法是贪婪算法，但是插入排序的问题就是如果数据不是按照，顺序大体有序，只是部分
 * 乱序，比如初夏6,5,4,3,2,1的情况，其复杂度和冒泡没有区别,所以希尔的思想是，每次插入排序移动的步数太小了，可以先移动步数
 * 很大，然后步数逐步减小，最后一次减小到1，也就是插入排序，这样可以保证最后一次的插入排序，的时候，基本大体有序，只需要部分
 * 微调即可，也就解决了插入排序的6,5,4,3,2,1的问题
 *
 *
 *
 *
 */
public class ArrayShellSort {

    public static void main(String[] args) {
        int[] arr = new int[]{6, 5, 4, 3, 2, 1, 8, 9, 7};
        shell3StepSort(arr);
        System.out.println(Arrays.toString(arr));
    }


    private static void shellSort(int[] arr) {
        int step;
        int len = arr.length;
        for (step = len / 2; step > 0; step = (step / 2)) {
            // 默认初次访问位置再step上，所以这个索引-step一定是0
            for (int o = step; o < len; o++) {
                int tmp = arr[o];
                int i = o;
                // 这个需要=0临界值判断，普通插入因为step是1，所以>0就判断出来了,=0说明最后一个已经处理完了
                for (; (i - step) >= 0 && arr[i - step] > tmp; ) {
                    arr[i] = arr[i = i - step];
                }
                if (i != o) {
                    arr[i] = tmp;
                }
            }
        }
    }

    /**
     * 希尔排序步长，优化，step 与数组长度互为质数的的情况下，经过试验证明是比较好的
     * 也就是说step和数组长度，没有其他公因式，只有1
     * 1. 在step 不小于len/3,则step=step*3+1
     * 2. step递减为 (step-1)/3
     *
     * */
    private static void shell3StepSort(int[] arr) {
        int len = arr.length;
        int step = 1;
        while (step <= len / 3) {
            step = step * 3 + 1;
        }
        for (; step > 0; step = (step - 1) / 3) {// 步长控制，互质

            for (int o = step; o < len; o++) {//插入从默认从1开始现在改成从指定的step开始
                int i = o;
                int tmp = arr[i];
                while (i - step >= 0 && arr[i - step] > tmp) {
                    arr[i] = arr[i = (i - step)];
                }
                arr[i] = tmp;
            }
        }
    }
}

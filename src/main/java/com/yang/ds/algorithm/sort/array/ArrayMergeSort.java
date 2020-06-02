package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 归并排序(复杂度:NlogN),内存空间使用时原有数组的空间的两倍
 * 数组的归并排序 A，B,C
 * 1.前提是两个有序的数组
 * 2.两个数组然后依次从头到尾比较，比较值小的一个元素插入到第三个数组,同时比较小的数组指针后移
 * 3.结束条件为其中一个数组所有元素都取完，并且插入到第三个数组
 * 4.然后再处理多出一部分数据的数组(数组总有剩下的一个，或者比较长的一个数组)，原则就是把多出来的数组的剩余元素，直接复制到第三个数组
 */
public class ArrayMergeSort {


    public static int[] mergeFor(int[] a, int[] b) {
        if (null == a || a.length <= 0 || null == b || b.length <= 0) {
            throw new IllegalArgumentException("error params");
        }
        int[] c = new int[a.length + b.length];
        int aIdx = 0;// a数组下标
        int bIdx = 0;// b数组下标
        int cIdx = 0;// c数组下标
        for (; (aIdx < a.length && bIdx < b.length); ) {
            if (a[aIdx] < b[bIdx]) { // a数组元素比b数组小，a数组元素先排入到c数组
                c[cIdx++] = a[aIdx++];
            } else {
                c[cIdx++] = b[bIdx++];// a数组元素比b数组中数组大，b数组中元素先排入c数组
            }
        }
        // 处理数组比对之后剩余的情况
        // 1. a多余b
        for (; aIdx < a.length; ) {
            c[cIdx++] = a[aIdx++];
        }
        // 2.b多余a
        for (; bIdx < b.length; ) {
            c[cIdx++] = b[bIdx++];
        }
        return c;
    }

    /**
     * 归并排序，递归的方式实现
     * 1. 不断的把数组分成两份
     * 2. 边界值为数组索引下边界l值和数组索引上边界h重合
     * 3. 切分成之后一个元素的数组可以看做就是一个有序的数组
     * 4. 不断的重复递归，然后显示子数组的两个两个的进行归并排序
     * 最终递归完成，所有的子问题都解决之后，就是连个大数组的进行归并，完成一个大的乱序的数组排序
     */

    public static int[] mergeRecursion(int[] c, int l, int h) {
        if (l >= h) {
            // 递归返回不进行处理
        } else {
            int mid = (l + h) / 2;
            mergeRecursion(c, l, mid); // 左半部分拆分递归，解决每一个左部分的问题
            mergeRecursion(c, mid + 1, h);// 有半部分查分递归，解决每一个右半边数组的排序问题
            //两个部分都完成之后进行数组进行合并，执行两个有序数组进行排序，合并成一个新的有序数组
            merge(c, l, h, mid);
        }
        return c;
    }

    /**
     * 执行两个有序数组合并成一个数组的过程
     */

    private static void merge(int[] c, int l, int h, int mid) {
        int i = l;
        int j = mid + 1;
        int[] tmp = new int[h - l + 1];
        int k = 0;

        // 实现合并
        while (i <= mid && j <= h) {
            if (c[i] < c[j]) {
                tmp[k++] = c[i++];
            } else {
                tmp[k++] = c[j++];
            }
        }

        // 情况1：左半部分数组,处理其中一个有序数组剩余的数据
        while (i <= mid && j > h) {
            tmp[k++] = c[i++];
        }

        // 情况2：右半部分数组,处理其中一个有序数组剩余的数据
        while (i > mid && j <= h) {
            tmp[k++] = c[j++];
        }

        // 临时数组拷贝到原始数组的里面
        for (int n = 0; n < tmp.length; n++) {
            c[l + n] = tmp[n];
        }
    }

    public static void main(String[] args) {
        /*int[] c = mergeFor(new int[]{1, 3, 5, 7}, new int[]{2, 4, 6, 8, 9});
        System.out.println("for循环实现归并排序:" + Arrays.toString(c));*/

        int[] arr = new int[]{4, 5, 1, 3, 8};
        int[] d = mergeRecursion(arr, 0, arr.length - 1);
        System.out.println("递归实现归并排序:" + Arrays.toString(d));
    }
}

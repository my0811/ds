package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 数组的冒泡排序
 * 算法复杂度 O(N^2) bad
 */
public class ArrayBubblingSort {

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{4, 1, 2};
        System.out.println(Arrays.toString(arr));
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void bubbleSort(Integer[] arr) {
        if (null == arr || arr.length <= 0) {
            return;
        }
        /**
         * 还是这个写法比较清晰，方便理解
         * I.有N个元素，那么最外层的循环就会执行N-1次
         * eg:1,2,3,4,5
         * 第一次 5
         * 第二次 4
         * 第三次 3
         * 第四次 2
         * 结束 1,2,3,4,5排序完成
         * II. 内层循环
         * 1. 要跳出我们经常用的数组遍历循环思维,i从0开始小于数组长度，因为少了个1，也可以这样遍历:for(int i=0i<=arr.len-1)（绝对的根据下标遍历）
         * 2. i的初始值恰好就是数组的最后一个元素的下标，所以内层循环是针对下标进行
         * 3. 因为是前一个元素和后一个元素相比较，所以第二层循环只需要（i-1）次就够了，否则数组越界
         * 4. 所以每次就是针对数组真是的下标开始，从0到最大元素的前一个元素下标，比较就可以了
         * III.
         * 特殊情况的话，只有一个元素，那么之后走最外层循环的一个假判断，但是第二层的for循环判断测试不通过,因为j<i (0<0)不成立
         * 数组长度是0第一层循环测试条件i>=0也不成立
         * 健壮性，和已读性，个人觉得都不错
         * */
        for (int i = arr.length - 1; i >= 0; i--) {// 用i--方便理解，以为每次排序一次都会找出一个最大，下一次排序就需要踢出掉这个元素，数组实际的遍历长度-1
            for (int j = 0; j < i; j++) { // 对于数组之后一个元素也成立
                if (arr[j] > arr[j + 1]) { //小于倒叙，大于正序
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }
}

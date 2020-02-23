package com.yang.ds.algorithm;

import java.util.*;

/**
 * 算法练习-最大子序列和递归
 */
public class MaxSubSumPractise {

    public static void main(String[] args) {
        int[] arr = new int[]{-1, -2, -3, -4, -5};
        int sum=maxSumRecursion(arr, 0, 4);
        System.out.println("sum is "+sum);
    }

    /**
     * 方法需要递归，所以参数需要注意，通用，因为方法内部还会传参调用
     */
    private static int maxSumRecursion(int[] arr, int left, int right) {
        // 边界值判断，递归什么时候结束，要不栈内存溢出了啊，一层层的调用，栈上的方法，
        // 然后再逐层的向上返回，就像查字典，先进后出，典型的栈数据结构，递归第一层方法
        // 会在栈的最底层，栈上线程最优先执行的是栈顶的方法
        if (left == right) {// 说明无限的递归，只有一个元素了 [a]此时a就是最大子序列的和
            return arr[left];// 递归结束的标志条件
        }
        /**
         * 截取数组，让数组无限的靠近左边，直到剩下一个元素结束
         * ,不断的折中，最后center的值会等于left值，获取到最左边，截取到数组只有一个元素
         * */
        int center = (left + right) / 2;
        // 递归不断的获取到只有一个元素，开始返回计算,先把数组左边的元素一直递归，到极限，最左边元素
        int maxLeftSum = maxSumRecursion(arr, left, center);

        // 右边最大

        int maxRightSum = maxSumRecursion(arr, center + 1, right);

        // 左边最小序列和
        int leftMaxBorderSum = 0, leftBorderSum = 0;
        for (int i = center; i >= left; i--) {
            leftBorderSum += arr[i];
            if (leftBorderSum > leftMaxBorderSum) {
                leftMaxBorderSum = leftBorderSum;
            }
        }

        // 右边
        int rightMaxBorderSum = 0, rightBorderSum = 0;
        for (int i = center + 1; i <= right; i++) {
            rightBorderSum += arr[i];
            if (rightMaxBorderSum < rightBorderSum) {
                rightMaxBorderSum = rightBorderSum;
            }
        }
        // 中间最大
        int maxCenterSum = leftMaxBorderSum + rightMaxBorderSum;
        // 返回计算左，和右的最大值
        List<Integer> maxSumList = new ArrayList<>();
        maxSumList.add(maxLeftSum);
        maxSumList.add(maxRightSum);
        maxSumList.add(maxCenterSum);
        Collections.sort(maxSumList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 == o2) {
                    return 0;
                }
                return o1 > o2 ? -1 : 1;
            }
        });
        // 返回左边，中间、右边，最大的值
        return maxSumList.get(0);
    }
}

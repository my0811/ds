package com.yang.ds.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * --------数据结构与算法----------
 * 1. 最大子序列和的算法问题
 * 2. 了解算法的时间复杂度估算，大O 记法
 * 3. 比较不同的算法
 * 4. 算法的理解就是，在数组中先一个个元素的去掉，找出子序列，然后再遍历子序列，按个相加
 * 最终找出最大的子序列
 */
public class MaxSubSum {

    /**
     * 找出数组中的最大子序列的和
     * 步骤:
     * 1. 遍历数组
     * 2. 从头到尾相加
     * 3. 然后从数组的最左面去掉一个元素继续相加
     * 4. 一直加到所有的子序列都相加完成
     * eg:
     * arr[]{1,2,3,4}
     * step1:
     * 1,1+2,1+2+3,1+2+3+4
     * step2:
     * 2,2+3,2+3+4
     * step3:
     * 3,3+4
     * return maxSum
     * <p>
     * 算法复杂度，最差的情况是，三个for循环都是N*N*N
     * 算法估算，最重要的就是不要少估算，多个N相加，去最大，比如这里最大的应该是N^3
     * O(N^3)
     */
    public static int maxSubSum1(int[] a) {
        int maxSum = 0;
        for (int i = 0; i < a.length; i++) {//
            for (int j = i; j < a.length; j++) { //N
                int thisSum = 0;
                // 这一步循环，其实没有太大用处，两层循环就搞定了，把算法复杂度搞成了O(N^3)
                for (int k = i; k <= j; k++) { //N
                    thisSum += a[k];
                }
                if (thisSum > maxSum) {// N^2
                    maxSum = thisSum;
                }
            }
        }
        return maxSum;
    }

    /***
     * 相比maxSubSum1 少了一层循环
     * O(N^2)
     * */
    public static int maxSubSum2(int[] a) {
        int maxSum = 0;
        for (int i = 0; i < a.length; i++) {//
            for (int j = i; j < a.length; j++) { //N
                int thisSum = 0;
                // 这一步循环，其实没有太大用处，两层循环就搞定了，把算法复杂度搞成了O(N^3)
                /*for (int k = i; k <= j; k++) { //N
                    thisSum += a[k];
                }*/
                if (thisSum > maxSum) {// N^2
                    maxSum = thisSum;
                }
            }
        }
        return maxSum;
    }


    /***
     *
     *  重点递归算法
     *  O(NlogN)>O(N)
     * */

    private static int maxSumRec(int[] a, int left, int right) {
        /**
         * 先判断便捷，因为递归在无限的靠近，最终会数组的左坐标等于右坐标，这个时候就会出现
         * 截取的只有一个元素了
         *
         * // 为什么这个一定要是0呢？因为下面的我们的计算逻辑是初始化最大子序列的和为0，比0大的才计算最大值，比0小的就不计算了，所以这个就是为什么第一个元素要大于0，
         // 否则，就会出现全是负数的最大子序列，也是0，是因为"int maxRightBorderSum = 0, rightBorderSum = 0;"
         *
         *
         * */
        if (left == right) {
            // 如果数组都是负数，这个判断岂不是很尴尬吗?
            if (a[left] > 0) {
                return a[left];//只要只剩下一个元素，不为负数就返回
            } else {
                return 0;
            }
        }
        int center = (left + right) / 2;
        // 这个递归要执行完成之后，才能走下一个递归啊
        int maxLeftSum = maxSumRec(a, left, center);
        int maxRightSum = maxSumRec(a, center + 1, right);
        // 计算左半部分最大子序列和
        int maxLeftBorderSum = 0, leftBorderSum = 0;
        for (int i = center; i >= left; i--) {
            leftBorderSum += a[i];
            if (leftBorderSum > maxLeftBorderSum) {
                maxLeftBorderSum = leftBorderSum;
            }
        }
        // 计算右半部分最大子序列和
        int maxRightBorderSum = 0, rightBorderSum = 0;
        for (int i = center + 1; i <= right; i++) {
            rightBorderSum += a[i];
            if (rightBorderSum > maxRightBorderSum) {
                maxRightBorderSum = rightBorderSum;
            }
        }
        // 返回最大子序列的和
        int maxCenterSum = maxLeftBorderSum + maxRightBorderSum;
        List<Integer> maxSumList = new ArrayList<>();
        maxSumList.add(maxLeftSum);
        maxSumList.add(maxRightSum);
        maxSumList.add(maxCenterSum);
        Collections.sort(maxSumList);
        if (maxSumList.isEmpty()) {
            throw new RuntimeException("");
        }
        return maxSumList.get(maxSumList.size() - 1);
    }

    /**
     * 最优算法,内存使用为常量
     */
    private static int maxSubSum4(int[] arr) {
        int maxSubSum = 0;
        int thisSubsum = 0;
        for (int i = 0; i < arr.length; i++) {
            thisSubsum += arr[i];
            if (maxSubSum < thisSubsum) {
                maxSubSum = thisSubsum;
            } else if (thisSubsum < 0) {
                thisSubsum = 0;
            }
        }
        return maxSubSum;
    }


    public static void main(String[] args) {
        int maxSum1 = maxSubSum1(new int[]{1, 2, -3, -4, -2});
        int maxSum2 = maxSubSum1(new int[]{1, 2, -3, -4, -2});
        int maxSum3 = maxSumRec(new int[]{-1, -2, -3, -4, -5}, 0, 4);
        int maxSum4 = maxSubSum4(new int[]{1, 2, -3, -4, -2});
        System.out.println("maxSum=" + maxSum1);
        System.out.println("maxSum2=" + maxSum2);
        System.out.println("maxSum3=" + maxSum3);
        System.out.println("maxSum4=" + maxSum4);
    }
}

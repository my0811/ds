package com.yang.ds.algorithm.sort.array;

import java.util.Arrays;

/**
 * 快排 TODO 算法复杂度
 * 1. 递归的应用
 * 2. 快排适合大数据量的应用排序，如果小数据量的排序可以使用选择、冒泡、插入等
 */
public class ArrayQuickSort {

    public static void main(String[] args) {
        int data[] = {3, 6, 9, 5, 1};
        // int data[] = {99, 88, 77, 66, 55, 44, 33, 22, 11, 9, 8, 7, 6, 5, 4, 3, 1, 2};
        System.out.println(Arrays.toString(data));
        quickSort(data, 0, data.length - 1);
        System.out.println(Arrays.toString(data));
    }

    /**
     * 块排实现
     */
    private static void quickSort(int[] data, int left, int right) {
        // 边界条件
        if (left >= right) { // 左右指针碰撞，到了中间位置了
            return;// 结束递归
        } else {
            /**
             * 递归之前把数组data分割成两部分,前半部分元素全都应该小于数组小于后半部分 before<=base<=after
             * base 为中间的基准值
             *
             * */
            int partition = partition(data, left, right);
            // 数组分割后进行递归
            quickSort(data, left, partition - 1);// 左半部分
            quickSort(data, partition + 1, right);// 右半部分
        }
    }

    /**
     * 左右数据根据中间基准数据进行左右位置交换
     */
    private static int partition(int[] data, int left, int right) {

        //左边界指针
        int i = left;

        //右边界指针( 加1是为了下面循环的优雅写法，要不就少了最后一个元素了)
        int j = right + 1;

        // 默认基准值；默认被分割的数组的第一个元素，可以用三项取中算法优化
        int base = tMid(data, left, right);

        while (true) {

            // 相向运动指针，直到出现比基准值大的停止,或者到了被切割的数组的右边界
            while (i < right && data[++i] < base) {
            }

            // 相向运动指针，直到出现比基准值小的停止，或者到了呗切割数组的左边界
            while (j > left && data[--j] > base) {
            }

            // 说明指针碰撞了，也就是所有的元素都走了一遍，越界了，结束循环，因为这个时候已经找到了比中间值的小的左边界值
            if (i >= j) {
                break;
            } else {// 指针没有碰撞，需要交换元素位置，这样就是小元素在前面，大元素在后面,最终想要的效果就是：left(基准值左边所有元素)<base<right(基准值右边所有元素)
                swap(data, i, j);
            }
        }

        // 因为数组碰撞之后，右面的指针最终移动的位置一定是之前全都交换过位置的数据了，并且第一个碰到比基准值小的就停止，那么这个位置就是基准值右面已经没有比这个基准值更小的元素了
        // 所以这个时候的基准值是被切割数组的第一个元素，最左面的，所以这个时候j的这个位置就应该放入基准值,移动完成之后，这个基准值左面的元素都小于基准值,基准值右面的元素都大于基准值
        swap(data, left, j);
        return j;
    }

    /**
     * 数组数据的位置的交换,swap
     */
    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    /**
     * 三项取中方案，避免快排出现倾斜的问题,如:{5.4,3,2,1},这样的话移动次数就接近于N,就无异于冒泡、选择排序了，非常糟糕
     * ***********{5, 4, 3, 2, 1}
     * *************(1,4,3,2,5*)
     * ********{1,4,3,2}        {}
     * *******(1*,4,3,2)
     * ******{1}      {4,3,2}
     * ***************(2,3,4*)
     * ************{2,3}     {}
     * **********(2*,3)
     * *********{}    {}
     * 1,2,3,4,5
     */

    private static int tMid(int[] data, int left, int right) {
        // 如果小于三个元素不能使用三项取中
        if (data.length < 3) {
            return data[left];
        }
        int mid = (left + right) / 2;
        if (data[left] > data[right]) {
            swap(data, left, right);
        }
        if (data[mid] > data[right]) {
            swap(data, mid, right);
        }

        // 最后一步，把中间值放到left上，因为快排上面的逻辑是，每次都以第一个元素为基本元素
        if (data[left] < data[mid]) {
            swap(data, left, mid);
        }
        return data[left];

    }
}

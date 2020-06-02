package test.ds.sort;


import org.junit.Test;

import java.util.Arrays;

/**
 * 快排，复杂度, O(NlogN)
 * 是冒泡排序的基础上进行了优化，也是交换位置，
 * 核心思想是分支思想，一开始找一个中间值进行交换，O(N)级别的复杂度,
 * 找到中间值之后，不断递归，不断把数组从中间值位置，开始向下切分，然后把没切分的一个小数组进行左右大小排序，之后，
 * 整个数组就排序好了
 * 数据量小于20应该用简单排序效率比较高,
 * <p>
 * 快排，递归边界也是从数组只有两个元素开始，下一层递归，左递归和右递归都会结束
 * 比如1:2
 * <p>
 * *
 */

public class Quick {

    private void quickSort(int[] arr, int low, int high) {
        // 递归边界
        if (low >= high) {
            return;
        }

        // 排序,中间值选取
        int p = partition(arr, low, high);
        quickSort(arr, low, p - 1);
        quickSort(arr, p + 1, high);
    }

    private int partition(int[] arr, int low, int high) {
        int l = low;
        int h = high + 1;
        int base = getMid(arr, low, high);
        for (; ; ) {
            for (; l < high && arr[++l] < base; ) ;
            for (; h > low && arr[--h] > base; ) ;
            if (l >= h)
                break;
            swap(l, h, arr);
        }
        // 中间值选成功，交换
        swap(low, h, arr);
        return h;
    }

    private void swap(int l, int h, int[] arr) {
        int tmp = arr[l];
        arr[l] = arr[h];
        arr[h] = tmp;
    }

    private int getMid(int[] arr, int low, int high) {
        int size = high - low + 1;
        if (size < 3) {
            return arr[low];
        }

        // 尽量每次都是中间值
        int mid = (low + high) >>> 1;
        if (arr[low] > arr[high])
            swap(low, high, arr);
        if (arr[mid] > arr[high])
            swap(low, high, arr);
        if (arr[low] < arr[mid])
            swap(low, mid, arr);
        return arr[low];
    }

    @Test
    public void quikSortTest() {
        int[] arr = new int[]{3, 5, 1, 9, 3, 5, 6};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}

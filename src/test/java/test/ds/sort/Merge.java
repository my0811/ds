package test.ds.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 归并排序
 * 1.先实现，有序数组的归并，然后再实现归并排序
 * 2. 每次切分都是logn，的复杂度，然后分左右两部分，也就是2logN，然后两个有序数组的归并排序，是N的操作，所以复杂度在n*2logN= NlogN
 */


public class Merge {

    private int[] merge(int[] a, int[] b) {
        if (a == null || b == null || a.length <= 0 || b.length <= 0) {
            throw new IllegalArgumentException("array is not be null");
        }
        int[] tmp = new int[a.length + b.length];
        int k = 0;
        int aIdx = 0;
        int bIdx = 0;
        for (; aIdx < a.length && bIdx < b.length; ) {
            if (a[aIdx] < b[bIdx]) {
                tmp[k++] = a[aIdx++];
            } else {
                tmp[k++] = b[bIdx++];
            }
        }
        for (; aIdx < a.length; )
            tmp[k++] = a[aIdx++];
        for (; bIdx < b.length; )
            tmp[k++] = b[bIdx++];
        return tmp;
    }

    private void mergeSort(int[] arr, int low, int high) {
        // 递归边界
        if (low >= high)
            return;

        // 折中
        int mid = (low + high) >>> 1;
        mergeSort(arr, low, mid);
        mergeSort(arr, mid + 1, high);
        // 归并排序
        merge(low, high, mid, arr);


    }

    private void merge(int low, int high, int mid, int[] arr) {
        // 递归边界
        int k = 0;
        int[] tmp = new int[high - low + 1];
        int h = mid + 1;
        int l = low;
        for (; l <= mid && h <= high; ) {
            if (arr[l] < arr[h])
                tmp[k++] = arr[l++];
            else
                tmp[k++] = arr[h++];
        }
        for (; l <= mid; )
            tmp[k++] = arr[l++];
        for (; h <= high; )
            tmp[k++] = arr[h++];

        // 数组拷贝
        for (int i=0;i<tmp.length;i++){
            arr[low + i] = tmp[i];
        }
    }


    @Test
    public void mergeTest() {
        int[] a = new int[]{1, 5, 3, 4};
        int[] b = new int[]{2, 5, 4, 3};
        mergeSort(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
        mergeSort(b, 0, b.length - 1);
        System.out.println(Arrays.toString(b));
        int[] c = merge(a, b);
        System.out.println(Arrays.toString(c));
    }
}

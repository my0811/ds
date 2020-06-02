package test.ds.heap;

import java.util.Arrays;

/**
 * 30 min 搞定堆，以及堆的排序
 * */

public class Heap<V extends Comparable<? super V>> {

    // 最大数组长度2^30
    private static final int MAX_CAPACITY = 1 << 30;

    // 堆数组
    private V[] values;

    // 当前元素个数
    private int size;

    public Heap(int initCap) {
        this.values = (V[]) new Comparable[initCap];
        size = 0;
    }

    // 添加
    private void add(V value) {
        resize(size + 1);
        values[size] = value;
        // 上滤
        selfUp(size, value, values);
        size++;
    }

    private void resize(int minCap) {
        if (minCap - values.length > 0) {
            // 扩容1.5
            int oldCap = values.length;
            int newCap = (oldCap >>> 1) + oldCap;
            if (newCap - MAX_CAPACITY > 0) {
                throw new IllegalArgumentException("capacity is over");
            }
            values = Arrays.copyOf(values, newCap);
        }
    }

    public V remove() {
        // 堆顶移除
        V removed = values[0];
        values[0] = values[size - 1];
        values[size - 1] = null;
        selfDown(0, values[0], values, size - 1);
        size--;
        return removed;
    }

    private void selfDown(int index, V v, V[] values, int len) {
        // 数组长度大于0
        if (len <= 0) {
            return;
        }
        // 最后非叶子为 size/2-1
        int half = len >>> 1;
        while (index < half) {
            // left 2*index+1, right 2*index+2
            int child = (index << 1) + 1;
            // right
            int right = child + 1;
            // max
            V max = values[child];
            if (right < len && values[right].compareTo(max) > 0) {
                max = values[child = right];
            }
            if (v.compareTo(max) > 0) {
                break;
            }
            values[index] = max;
            index = child;
        }
        values[index] = v;
    }

    private void selfUp(int index, V v, V[] arr) {
        // 第一个元素为0，不用调整
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            int cmp = arr[parent].compareTo(v);
            if (cmp > 0) {
                break;
            }
            arr[index] = arr[parent];
            index = parent;
        }
        // 等于0，就调整到了根
        arr[index] = v;
    }

    private void sort(V[] arr) {
        if (arr == null || arr.length <= 0) {
            throw new IllegalArgumentException("arr is null ");
        }

        // 从size/2-1开始调整成堆
        int len = arr.length;
        for (int i = (len >>> 1) - 1; i >= 0; i--) {
            selfDown(i, arr[i], arr, len);
        }

        for (int j = arr.length - 1; j > 0; j--) {
            // 移除堆顶
            swap(0, j, arr);
            selfDown(0, arr[0], arr, j);
        }
    }

    private void swap(int i, int j, V[] arr) {
        V tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        Heap<Integer> heap = new Heap<>(10);
        heap.add(1);
        heap.add(9);
        heap.add(6);
        heap.add(10);
        heap.add(2);
        while (!heap.isEmpty()) {
            System.out.println(heap.remove());
        }
        Integer[] arr = new Integer[]{1, 4, 4, 8, 6};
        heap.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}

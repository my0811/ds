package com.yang.ds.algorithm.sort.array;

import com.yang.ds.algorithm.heap.Heap;

import java.util.Arrays;

/**
 *
 * 堆数据结构实现数组排序 O(nlogN)
 * 1. 把无序数组对应成一个完全二叉树堆，但是无序数组不能满足堆的规则，就是父节点元素要比下一层子节点的元素大
 * 所以需要调整成堆的规则
 * 2.把堆顶的元素，把堆顶的元素调整到数组的最后一个位置，类似于冒泡排序，这样就出现了一个最值，然后就不需要再处理这个最值了
 *
 * 3.经过第二步侧处理，数组逻辑上应该减小一个单位长度，然后把-1个长度的数组继续调整为符合对的结构
 *
 *
 * */
public class ArrayHeapSort {
    public static void main(String[] args) {
        Heap<Integer, String> heap = new Heap<>(10);
        Heap.Node<Integer, String>[] nodes = new Heap.Node[4];
        nodes[0] = new Heap.Node(1, "y");
        nodes[1] = new Heap.Node(9, "k");
        nodes[2] = new Heap.Node(3, "z");
        nodes[3] = new Heap.Node(1, "yang");
        System.out.println(Arrays.toString(nodes));
        heap.sort(nodes);
        System.out.println(Arrays.toString(nodes));
    }
}

package com.yang.ds.algorithm.sort.array;

import com.yang.ds.algorithm.linkedlist.SortLinkedList;

/**
 * 无序的数组可以用有序链表来实现排序
 * 1.多占用了差不多2倍的空间
 * 算法复杂度，个人推到是O(1/2N^2)，等比求和,算法复杂度也没有比插入、冒泡、选择好到哪里，稍微好了一丢丢！
 */
public class ArrayLinkedListSort {
    public static void main(String[] args) {
        int[] unSortArr = new int[]{3, 1, 0, 9, 1, 2};
        SortLinkedList sortLinkedList = new SortLinkedList();
        for (int i = 0; i < unSortArr.length; i++) {
            sortLinkedList.insert(unSortArr[i]);
        }
        int j = 0;
        for (; sortLinkedList.size() != 0; ) {
            unSortArr[j] = sortLinkedList.deleteHead();
            j++;
        }
        for (int i = 0; i < unSortArr.length; i++) {
            System.out.print(unSortArr[i]);
        }
    }
}

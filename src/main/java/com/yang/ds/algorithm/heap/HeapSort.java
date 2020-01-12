package com.yang.ds.algorithm.heap;

import java.util.Arrays;

public class HeapSort {
    public static void main(String[] args) {
        int[] arr = {1,4,2,6,3,8,7,5};
        sort(arr);
        System.out.println(Arrays.toString(arr));

    }

    //堆排序的实现方法
    public static void sort(int[] arr) {
        //1.首先把arr调整为一个合法的堆
        for(int i=arr.length/2-1;i>=0;i--) {
            //具体的调整，封装成一个方法，因为反复要用
            adjustHeap(arr,i,arr.length);
        }

        //2和3合在一起实现
        for(int j=arr.length-1;j>0;j--) {
            //2.交换堆顶元素和堆尾巴元素的位置
            swap(arr,0,j);
            //3.交换完了后，立马把剩下的元素调整为一个合法的堆
            adjustHeap(arr, 0, j);
        }
    }



    //调整的方法
    public static void adjustHeap(int[] arr,int i,int length) {
        int temp = arr[i];
        for(int k=i*2+1;k<length;k=k*2+1) {
            if(k+1<length && arr[k]<arr[k+1]) {//这个if实在上就是找到大的那个子节点
                k++; //让k指向左右两个子节点中，大的那个
            }
            if(temp >= arr[k]) {
                break;
            }else {
                arr[i] = arr[k];
                i = k;
            }
        }
        arr[i] = temp; // 被调整的节点应该放的正确位置
    }

    //交换
    public static void swap(int[] arr,int a,int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

}

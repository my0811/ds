package com.yang.ds.algorithm.array;

/**
 * 无序数组 (增加，删除，查找) 基本操作
 * 无序数组 增加、删除、查找都是O(N)
 *
 * 优点
 * 1. 插入快O(1)
 * 2.
 * 缺点
 * 1. 查找慢O(N),比如要查找的数据再最后面，最坏的情况
 * 2. 删除O(N),最坏的情况删除第一个元素，基本每个元素都要移动一次,最重要的还得先查一遍，找到元素，然后再删除
 *
 */
public class DisorderArray {
    public static void main(String[] args) {
        ArrayClass arrayClass = new ArrayClass(5);
        // 新增数组元素
        arrayClass.insert(1);
        arrayClass.insert(2);
        arrayClass.insert(3);
        arrayClass.insert(4);
        arrayClass.insert(5);
        arrayClass.print();
        arrayClass.del(3);
        arrayClass.print();
    }
}

class ArrayClass {
    private long[] arr;  // 被封装的数组对象
    private int nElems;   //数组存在的元素个数，当前的

    /**
     * 初始化数组，构造方法初始化
     */
    public ArrayClass(int maxSize) { // maxSize数组的最大长度
        arr = new long[maxSize];
        nElems = 0;
    }

    /**
     * 新增
     */
    public void insert(long data) {
        arr[nElems] = data;
        nElems++;
    }

    /**
     * 删除
     */
    public void del(long delData) {
        // 删除首先要找到元素，就是一个遍历
        int i = 0;
        for (; i < nElems; i++) {
            if (delData == arr[i]) {
                break;
            }
        }
        if (i != nElems) { // 找到了元素
            for (int j = i; j < nElems-1; j++) {// 数组删除数据，需要把数据对齐
                arr[j] = arr[j + 1];
            }
            nElems--;
        } else {
            System.out.println("没有找到目标删除数据!");
        }
    }

    /**
     * 查找
     */
    public boolean find(long searchData) {
        int i = 0;
        for (; i < nElems; i++) { //没必要到数组长度，减少程序消耗
            if (arr[i] == searchData) {
                break;
            }
        }
        // 没有找到数据的话，for循环的会进行最后一次自加，所以没有找到的话，i最后的值为 nElems
        return i == nElems ? false : true;
    }

    /**
     * 显示，循环遍历
     */
    public void print() {
        for (int i = 0; i < nElems; i++) {
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

}

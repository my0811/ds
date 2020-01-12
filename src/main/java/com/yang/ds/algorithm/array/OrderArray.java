package com.yang.ds.algorithm.array;

/**
 * 有序数组（增加，删除，遍历，二分查找）
 * 1.有序数据可以用二分查找O(logN)
 * 2.
 */
public class OrderArray {
    public static void main(String[] args) {
        OrderArrayClass orderArrayClass = new OrderArrayClass(5);
        // 无序添加，但是数组中的元素打印出来是有序的
        orderArrayClass.insert(3);
        orderArrayClass.insert(2);
        orderArrayClass.insert(5);
        orderArrayClass.print();
        System.out.println(orderArrayClass.binarySearch(9));
        orderArrayClass.del(2);
        orderArrayClass.print();
    }
}

/**
 * 自己分装数组，模拟数的数据结构的操作
 */
class OrderArrayClass {
    private long[] arr; // 封装数组
    private int nElems;// 数组当前长度

    public OrderArrayClass(int maxSize) {
        this.arr = new long[maxSize];
    }

    /**
     * 插入,有序数组插入需要注意比较大小才能确定插入位置，不能随便在末尾插入,复杂度也是比较高
     */

    public void insert(long data) {
        // 先找到插入位置
        /**
         * 插入位置两种情况，
         * 一是比所有的数都大
         * 二是不是最大的
         * */
        int i;
        for (i = 0; i < nElems; i++) {// 第一个比自己大的元素就插入位置
            if (arr[i] > data) {// 找到了第一个比需要插入数据大的元素，并且就是需要插入的位置
                break;
            }

        }
        // 判断是否找到了需要插入的位置,注意，nElems是长度，i不会=nElems，除非遍历完了，i进行最后一次的自增
        if (i < nElems) {// 找到了
            //step1把从找到插入位置的元素全部向后移动一位(从最后一个元素开始向后移动)
            if (nElems < arr.length) {// 判断数组越界，必须有空闲位置，当前元素个数要比数组长度小
                for (int j = nElems; j > i; j--) {
                    arr[j] = arr[j - 1];
                }
                arr[i] = data;// 插入数据
                nElems++;
            } else {
                throw new RuntimeException("Index Out Of Bounds Exception");
            }
        } else {// 没找到，没找到说明什么呢？说明这个插入的元素是最大的，直接放到数组最后就ok了
            if (nElems < arr.length) {// 判断数组越界
                arr[nElems] = data; // 插入数据
                nElems++;
            } else {
                throw new RuntimeException("Index Out Of Bounds Exception");
            }

        }
    }

    /**
     * 二分查找 O(logN)
     * <p>
     * 没有找到元素就返回-1
     */
    public int binarySearch(long data) {
        // 定义边界
        int low = 0, high = nElems - 1, mid = 0;
        // 定义折半值
        while (low <= high) {// 不断的这种，最终两个元素的边界会无限靠拢在一起，如果出现low>high或者high<low则所有元素都折中一遍了
            mid = (low + high) / 2;
            if (arr[mid] < data) {
                low = mid + 1;
            } else if (arr[mid] > data) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;// 没有查询到
    }

    /**
     * 删除数据
     */
    public void del(long data) {
        // 先用一下比较优秀的二分查找，找到删除的元素
        int index = binarySearch(data);
        if (-1 != index) {
            // 删除的时候需要此节点之后的数据全都向前移动,从删除的节点之后的几点全都向前移动
            if (nElems < arr.length) { // 考虑数组最后一个元素是否完全被替换掉，gc问题
                for (int i = index; i < nElems; i++) {
                    arr[i] = arr[i + 1];
                }
                nElems--;
            } else {
                for (int i = index; i < nElems - 1; i++) {
                    arr[i] = arr[i + 1];
                }
                // 或者增加边界值，让数组足够用，用空代替
                arr[nElems - 1] = 0;// help gc(如果是对象的引用的话)
                nElems--;
            }

        }
    }

    public void print() {
        for (int i = 0; i < nElems; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}

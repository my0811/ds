package test.ds.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 插入排序
 * 插入排序其实是一种贪婪算法的思路:
 * 每次不要求数组全部都是有序的，但是每次保证一小部分是有序的，后面的处理会基于弱序的在排序，将会更加减少判断的次数
 * 处理方式：类似于二叉堆的调整，每次都是移动，空出来的位置就是插入位置
 * <p>
 * 要求数组整体是一种从小到大的这种弱序，可以进行微调的移动就可以了，乱序的比较平均比如:
 * 6,3,5,9,7,8，但是整体感觉还是从小到大，可以理解把数组切成很多部分，左面的部分比右面的部分小，每个部分间的数据可以
 * 没什么顺序，但是整体要有个大的顺序,或者是平衡的关系，分割成的每部分数据没有强的从小到大，或者从大到小的顺序，这样只需要
 * 微调每一个小部分的数组基本就能满足了整体的排
 * 但是极端情况下：6，5，4，3，2，1,这种情况与选择没啥区别，只不过是冒泡的相反处理，冒泡是随着最大值的出现，判断和交换越来越少
 * 插入则正好相反，随着遍历移动和判断则越来越多
 * 所以插入的排序如果考虑最坏的情况下也是O(N^2),但是平均的话应该要优于选择和冒泡
 */

public class Insert {

    @Test
    public void insertSortTest() {
        int[] arr = new int[]{8, 6, 1, 2, 3};
        insertSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void insertSort(int[] arr) {
        // 选择从第0个元素开始判断，如果数组小于等于1，走一次空判断
        for (int o = 1; o < arr.length; o++) {
            int tmp = arr[o];
            int i = o;
            for (; i > 0 && arr[i - 1] > tmp; i--) {
                arr[i] = arr[i - 1];
            }
            arr[i] = tmp;
        }
    }
}

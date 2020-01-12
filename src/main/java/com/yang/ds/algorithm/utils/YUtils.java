package com.yang.ds.algorithm.utils;

import java.util.List;

public class YUtils {

    /**
     * 实现二分查找(模拟Collections中的二分查找)
     *
     * 解释:有序数组二分查找返回"-(low + 1)"(就是插入的位置,"+1"还有去负数是为了区别正常索引，用的时候再取反即可)
     1. 当元素找不到的时候经过二分查找，low和high一定会重合,并且还会继续再执行一次low的+1或者high-1，对于有序数组而言
     从左到右的数据一次增大，所以low的位置就是应该插入的位置
     1)如果数据比重合的位置还大，此时最后一次循环low=mid=high,low=low+1,此时low+1之后的位置就是插入位置
     2)如果数据比重合的位置还小，此时最后一次循环low=mid=high，low=low，high=high-1，但是此时我们还是看做low是插入位置
     并且，low的值和重合时候的值是一样的

     2. 1中的逻辑是针对于数组底层插入的复制逻辑，插入数据当前索引右面的所有数据都向后移动一位，空出来的这个位置正好是插入位置,所以说总结如下:
     (1).插入数据比重合点小，那么重合点插入数据，把重合点索引之后的数据全都向后移动一位，然后插入到重合点位置即可，插入之后，就是插入的数据比原来重合点
     索引的数据要小
     (2),反之，插入的数据位置应该是重合点索引+1的位置，然后重合点索引+1位置之后的所有数据向后移动一位

     3. java list集合针对于指定位置添加数据，底层数组移动数据的实现如下:
     System.arraycopy(elementData, index, elementData, index + 1,size - index);
     "size-index" 非常完美的解决了需要移动多少个数据的问题，因为index比size始终小1，所以移动数据个数（做差）可以包含两种极端情况，一种是第0个元素，和
     第size(最大索引+1也就是当前数组最大索引的下一个位置)都可以满足如下：
     比如数组长度是4，从第0个位置插入，则复制4个数据,从第4个位置插入则移动0个数据，两个极端（左右边界）都满足

     4. 如果数组为空则返回的索引值是-1，取反处理之后就是0

     5. 二分查找没有找到返回的就是一个插入位置，可能是一个元素的左面插入，那返回这个元素的index就可以了，如果是一个元素的右面插入
     则返回这个元素index+1，但是返回的的这个位置肯定是两个相邻元素中间的夹缝位置，只不过数组的插入是向后排挤的方式，所以能达到无论以当前元素作为
     插入点还是当前这个元素的右侧左为插入点，都能对应插入到当前这个元素的左缝隙或者右缝隙,当前插入当前元素的位置，我们也可以看做是插入当前元素的前一个元素
     的右面位置，因为插入的时候当前元素被排挤到后面去了，当前元素的前面的元素右面却又多了一个元素，反之当前元素的右侧插入一样的道理
     *
     * @param key  查找的key
     * @param list 需要查找的集合(有序集合，如果无序集合可以用空集合利用二分查找，找到插入位置)
     * */
    public static <K> int binarySearch(List<? extends Comparable<? super K>> list, K key) {
        if (null == list || null == key) {
            throw new IllegalArgumentException("args exit null");
        }
        int high = list.size() - 1;
        int low = 0;
        while (low <= high) {// 注意这个里面<=的意义，当没有查找到数据的时候，low和high重合的时候，low的索引的前一个位置就是插入位置
            int mid = (high + low) >>> 1;
            Comparable<? super K> midVal = list.get(mid);
            int cmp = midVal.compareTo(key);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // 找到返回对应索引值
        }
        // +1防止数组为空的时候，返回的是0与正常索引冲突
        return -(low + 1);// 没有找到，返回需要插入的位置索引(为了区别正常索引,所以+1取负数，用的时候反操作)
    }


    /**
     * 判断是否是质数的方法
     *
     * 关于循环步长的优化，如果数据非常大的，步长如果是1的话，影响还是挺多的
     * 设x是一个大于1的数，意思就是6的x倍，大于1的x倍
     * 6x，所以这个数对6取余一定是0-5的余数，0就是整除，余数是1-5
     * 1.6x不是质数
     * 2.6x+2不是质数，2的倍数
     * 3.6x+3不是质数，3的倍数
     * 4.6x+4不是质数,2的倍数
     * 6x+1,和6x+5
     * 也就是对6取余数,余数是1和5的才会存在质数,2，3，4都不可能
     * @param num int 整数
     * */
    public static boolean isPrime(int num) {
        int n = num;
        // 第一层优化，如果size是3以下的质数只有2或者3，只要不是1就可以了
        if (n <= 3) {
            return num > 1;
        }
        // 第二层优化，不用每次步伐都是1，我们可以用6来代替比较好，证明如下:
        int nm6 = n % 6;
        if (nm6 != 1 && nm6 != 5) {
            return false;
        }
        // 第三层优化，判断质数只要判断到这个树的开平方就行了，去平方根的左侧一半的数就可了，平方根右侧的数是对称的
        // 开平方如果不能开的话，向下取整也没问题，说明那个小数的临界值是除不开的,不是他的倍数
        int sqrt = (int) Math.sqrt(n);
        // 进入这里说明肯定是大于最大余等于数5的数了，而且无论是6的多少倍又余多少，余数在1-5的都被排除掉了，只需要每次按照6的倍数增加就可以了
        for (int i = 5; i <= sqrt; i += 6) {
            if (n % i == 0 && n != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * 把数值转成成靠近，数值的最小的2的n次方的数值，进制的整数倍
     * @param size size值
     * @param defaultSize 默认size
     * @param maxSize 最大size
     * */
    public static int sizeForBit32(int size, int defaultSize, int maxSize) {
        if (size <= 0) {
            throw new IllegalArgumentException("illegal size " + size);
        }
        int bitSize = size - 1;
        bitSize |= bitSize >>> 1;
        bitSize |= bitSize >>> 2;
        bitSize |= bitSize >>> 4;
        bitSize |= bitSize >>> 8;
        bitSize |= bitSize >>> 16;//  32位搞定
        return bitSize <= 0 ? defaultSize : (bitSize > maxSize ? maxSize : bitSize + 1);
    }
}

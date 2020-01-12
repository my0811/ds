package com.yang.ds.algorithm.hash;

import com.yang.ds.datastruct.hash.HashTable;

import java.util.Objects;

/**
 * 实现hash表
 *  一：
 *  System.out.println(Integer.MAX_VALUE << 1);
 *  这个值为-2，因为有符号左移，如果溢出，就相当于最高位把符号位顶掉了，-2就是因为全是1，开头是1表示负数，会变成一个负数的补码形式了，
 *  如果-1，然后再取反，就是反码逆运算，得出了绝对值就是2，所以说java中的<<1如果溢出，则不管符号位了
 *
 * 二：
 * int n = 10;
 n |= n >>> 1;// 除了无效位肯定都是有效位，右移动一位,变成了2个"1"
 n |= n >>> 2;// 原基础上在移动两位，变成了4个"1"
 n |= n >>> 4;// 原基础上已经4个"1",需要在移动4位变成了8个"1"
 n |= n >>> 8;// 再移动8个"1"变成16个"1"
 n |= n >>> 16;//在移动16个"1"，刚好填满32位
 当然n的最大值也就是31个1组成的，从存在1的有效位不断的向右移动，最终会把最高的有效值最高位之后的所有位都变成1，都变成1之后无论移动多少位，或的值都是上一次全是1的值
 这样做的好处就是能把n转换成和自己贴近的最小的2^n-1的二进制数,就是从有效位的最高位一直到最后一个为都是1，这样可以方便做低位与运算
 代替取模运算，计算更加高效，直接作用在二进制位上，实现循环数组
 *
 * 三：
 * 左移n位，末尾补0， *2^n，右移，前面根据符号来定，如果是正则补0，如果是负数则补1，因为负数存的是补码，就是反码+1,所以最后，获取的时候还需要-1取反，高位补1即是补0，一个道理
 * 右移，需要不断的阉掉末尾数，如果末尾数不是0，说明有有余数，就不是2进制的正数倍，或者n次方，就会出现小数，但是32位都是表示正数的组合，干掉一位，就相当于向下取整了
 * int类型的32所表示的正数没有小数也就是这样，没有表示小数的位
 *
 * 四:
 * ">>>","<<<" 无符号右移，无符号左移,
 * 只有无符号右移，没有左移，这种操作也是逻辑位移动，不管高位的符号，一律用0补充空位,这种用法只是左位运算用，不用来表示数值的运算
 * 因为没有意义，负数的话移动就变成了正数
 *
 *
 *
 * */
public abstract class AbstractHashTable<K, V> implements HashTable<K,V> {
    /**装填因子*/
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**最大容量 2^30*/
    public static final int MAX_CAPACITY = 1 << 30;

    /**默认初始大小2^4*/
    public static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**hash表，装填因子，默认0.75,太小空间浪费多，太大，冲突太多*/
    protected float loadFactor = DEFAULT_LOAD_FACTOR;

    /**hash表，数组实现*/
    protected Object[] table;

    /**当前数组大小*/
    protected int size;

    /**
     * 阈值，通过装填因子来计算出新的容量，超过这个容量阈值就需要扩容了，
     * 如果等数组满了再扩容会出现更多的冲突，增加插入和查找的复杂度
     * */
    protected int threshold;

    protected AbstractHashTable(int size, float loadFactor) {
        // 获取size的最小的2^n次方，这样为了之后用有效位与运算代替取模（原理就是利用进制的末尾的有效值，来代替余数,更高效的位运算）
        if (size <= 0)
            throw new IllegalArgumentException("Illegal initial capacity" + size);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        // 大小的边界判断
        if (size > MAX_CAPACITY)
            size = MAX_CAPACITY;

        // 初始化装填因子，默认是0.75f
        this.loadFactor = loadFactor;

        // 重新更新数组大小为传入参数的最小2^n，方便后面的实现位运算实现取模
        int cap = tableSizeFor(size);
        table = new Object[cap];

        // 更新数组扩容阈值，容量到达数组的75%进行扩容，扩容按照两倍的大小扩容，阈值和数组大小同时扩大两倍，比例不变还是0.75
        this.threshold = (int) ((float) cap * loadFactor);
    }

    public AbstractHashTable() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public AbstractHashTable(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 调整数组的大小，让数组的大小为大于传入size的最小的2^n次方
     * */
    private int tableSizeFor(int size) {
        int n = size - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? DEFAULT_INITIAL_CAPACITY : (n > MAX_CAPACITY ? MAX_CAPACITY : n + 1);
    }

    /**
     * 数组扩容,不能像list一样，底层重新直接复制数组就可以了，需要重新创建数组，之前的put流程需要循环重新回放一遍
     * 因为计算数组位置，是根据数组的大小取模实现的，数组大小变了话，需要从新取模运算，成本非常之高，所以尽量减少
     * map的扩容，保证map的空间充足，数据项和数组容量比值在0.75交好
     * es 中的副本不能随便扩容就是因为之前hash到这个副本分片上的数据太大，如果更改所有的副本shard上的数据都需要重新
     * hash，显然这是一件无法完成的事情，搞死人不偿命啊
     * */
    abstract void resize();

    protected boolean isFull() {
        return table.length == size;
    }


    /**
     * 获取hash值,hash算法采用jdk自己的hash算法
     *@param key 存储key值
     * */
    protected int hash(K key) {
        return Objects.hashCode(key);
    }
}

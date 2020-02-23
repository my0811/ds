package com.yang.ds.algorithm.array;

import java.util.Arrays;

/**
 * 模拟arrayList集合，就是增加了数组自动扩容，数组扩容底层是数组复制实现
 * arrayList是一个无序数组的实现
 * 查询遍历:O(N)
 * 尾部添加:O(1)
 * 删除:O(N)
 */
public class YArrayList<E> {

    private static final long serialVersionUID = 8683452581122892189L;

    // 默认数组初始化长度
    private static int DEFAULT_CAPACITY = 10;

    // 数组最大元素个数
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    // 传入初始化大小构造参数使用，jdk1.8之后值保留了这一个默认值了
    private static final Object[] EMPTY_ELEMENT_DATA = {};

    // 保存数据的数组,不可以序列化
    transient Object[] elementData;
    // 更改数组时候需要修改,防止多线程修改错误
    private int modCount;

    // 当前数组的长度
    private int size;

    /**
     * 指定数组容量的初始化s
     */
    public YArrayList(int initialCapacity) {
        super();
        if (initialCapacity < 0) {// 传入数值合法
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        }
        elementData = new Object[initialCapacity];
    }

    /**
     * 无参构造，不指定数组长度，默认是10个，在插入的时候回进行默认值赋值
     */
    public YArrayList() {
        super();
        this.elementData = EMPTY_ELEMENT_DATA;
    }

    /**
     * add
     */
    public boolean add(E e) {
        // 计算容量
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    public int size() {
        return size;
    }

    public int arrLen() {
        return elementData.length;
    }

    /**
     * 检查容量,当前插入元素所需要的数组的最小长度,等于当前size+1
     */
    private void ensureCapacityInternal(int minCapacity) {
        // 判断是否是初始化,如果是初始化无参构建的参数，那么默认数组大小为10
        if (elementData == EMPTY_ELEMENT_DATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        // 确定此次插入需要的数组的具体大小
        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        // 修改容量的时候防止被篡改
        modCount++;
        if (minCapacity - elementData.length > 0) {// 增加后的元素所需要的数组长度比当前数组大
            // 扩容
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        // 原来数组长度
        int oldCapacity = elementData.length;
        // 新容量为原来的容量的1.5倍，右移一位=/2,就是在原来数组长度的基础上在扩容原数组长度的一半
        int newCapacity = oldCapacity + (oldCapacity >> 1);//
        // 如果扩充1.5倍之后还不能装得下此次插入数据的所需要的数组长度，则已插入元素之后所需要的真实长度为准，
        if (newCapacity - minCapacity < 0) {// 数组为0的情况下回出现这种情况,这个时候回赋值为默认值10的长度
            newCapacity = minCapacity;//
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {// 源码这里面值最后一个元素覆盖
            throw new IllegalArgumentException("over array max len");
        }
        // 数组复制,当前数组，复制一个新容量的数组对象，重新赋给elementData，对外假装无感知，数组扩容还是比较消耗资源的，code时候最好考虑好容量，避免数组扩容
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    /**
     * get O(1)
     */
    public E get(int index) {
        rangeCheck(index);// 检查数组越界
        return (E) elementData[index];
    }

    /**
     * del
     * 删除也是比有复杂度的操作
     */
    public E remove(int index) {
        rangeCheck(index);
        modCount++;
        // 找到元素的值
        E oldValue = elementData(index);
        // 数组删除需要位置移动，就是删除之后的元素全部都要向前移动，java中的实现是数组拷贝实现
        int numMoved = (size - index) - 1;
        if (numMoved > 0) {// 存在需要移动的元素个数，因为不是最后一个元素
            // 脑补吧，不想写了，就是把原来数组删除的元素"索引"之后的数据然后再复制到这个数组，但是开始位置变了，从删除的那个位置开始覆盖，这样就把原来index的那个数据覆盖掉了
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null; // gc 原来数组最后的那个元素没有覆盖呢，但是也没有用了，已经覆盖到前面了
        return oldValue;
    }

    private E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * get equals方法
     */

    private void rangeCheck(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    /**
     * 查询一个元素的位置O(N)
     */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i] == null)
                    return i;
        } else {// 元素必须复写equals方法，要不没啥毛用
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        YArrayList<String> yArrayList = new YArrayList<>();
        for (int i = 0; i < 11; i++) {
            yArrayList.add("test");
        }
        System.out.println(yArrayList.size());
        System.out.println(yArrayList.arrLen());
    }
}

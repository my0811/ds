package com.yang.ds.algorithm.tree.btree.node;

import java.util.List;

/**
 * B树节点类
 * */
public interface BNode<K extends Comparable<? super K>, V> extends Spit<K, V>, Merge<K, V>, Rotate<K, V> {

    /**
     * 插入
     * @param key
     * @param value
     * */
    void insert(K key, V value);

    /**
     * 删除
     * @param key 指定删除的key值
     * */
    BKeyValue<K, V> delete(K key, int keyLoc);

    /**
     * 查找
     * @param key 指定删除的key值
     * */
    V search(K key);

    /**
     * 元素数量
     * @return int
     * */
    int keyNumber();

    /**
     * keys的结合,返回节点中对应的的key的集合
     * @return List<K>
     * */

    List<K> keys();

    /**
     * value集合
     * @return int
     * */

    List<V> values();

    /**
     * 树的阶
     * @return int
     * */

    int degree();

    /**
     * 最左侧第一个key 如果存在value则把value也封装在BKeyValue
     * @return int
     * */
    BKeyValue<K, V> getFirstLeftKey();

    /**
     * 删除并返回左侧第一个key
     * */

    BKeyValue<K, V> delFirstLeftKey();

    /**
     * 获取最右侧最后一个key 如果存在value则把value也封装在BKeyValue
     * @return int
     * */

    BKeyValue<K, V> getLastRightKey();

    /**
     * 删除并返回,最右侧最后一个key 如果存在value则把value也封装在BKeyValue
     * @return int
     * */

    BKeyValue<K, V> delLastRightKey();

    /**
     * 删除key
     * */
    void insertKey(int index, BKeyValue<K, V> newKey);

    /**
     * 删除key
     * @return BKeyValue
     * */
    BKeyValue<K, V> delKey(int index);

    /**
     * 修改key
     * */
    void setKey(int index, BKeyValue<K, V> replaceKey);

    /**
     * 获取key
     * @return BKeyValue
     * */
    BKeyValue<K, V> getKey(int index);

    /**
     * 是否上溢出
     * */
    boolean isOverflow();

    /**
     * 是否下溢出
     * */
    boolean isUnderflow();

    /**
     * key元素数量是否富裕
     * */
    boolean isAffluent();
}

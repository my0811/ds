package com.yang.ds.datastruct.tree.bstree;

import java.util.List;

public interface BSTree<K extends Comparable<? super K>, V> {

    /**
     * 添加
     * @param key
     * @param value
     * */
    void add(K key, V value);

    /**
     * 删除
     * @param key
     * */
    void delete(K key);

    /**
     * 查找
     * @param key
     * */
    V get(K key);

    /**
     * 最大值
     * */
    V getMax();

    /**
     * 最小值
     * */
    V getMin();

    /**
     * 中序遍历，左,根,右
     * */
    List<V> midErgodic();

    /**
     * 前序遍历,根,左,右
     * */
    List<V> preErgodic();

    /**
     * 后续遍历，左,右,根
     * */
    List<V> afterErgodic();
}

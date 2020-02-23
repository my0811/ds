package com.yang.ds.datastruct.tree.btree;

/**
 * b-tree接口
 * */
public interface BTree<K extends Comparable<? super K>, V> {
    /**B-树最小阶*/
    int MIN_DEGREE = 3;

    /**
     * 添加
     * @param key
     * @param value
     * */
    void insert(K key, V value);

    /**
     * 删除
     * @param key
     * */
    void delete(K key);

    /**
     * 查找
     * @param key
     * */
    V search(K key);

    /**
     * 打印tree结构
     * */
    String treeToString();


}

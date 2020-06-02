package com.yang.ds.datastruct.tree.btree.node;

import java.util.List;

/**
 * B树节点类
 * */
public interface BNode<K extends Comparable<? super K>, V> {

    /**
     * 插入
     * @param key
     * */
    void insert(K key, V value);

    /**
     * 删除
     * @param key 指定删除的key值
     * */
    Object delete(K key, int keyLoc);


    /**
     * 查找
     * @param key 指定删除的key值
     * */
    V search(K key);

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

    /**
     * 节点合并
     * @param parent 父节点
     * @param sibling 兄弟节点
     * @param delIndexer 删除索引器
     * */
    void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer delIndexer);

    /**
     * 左旋，针对右侧兄弟
     * @param rightSib 右侧兄弟
     * @param delIndexer 删除索引器
     * */
    void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer delIndexer);

    /**
     * 右旋，针对左侧兄弟
     * @param leftSib 左侧兄弟
     * @param delIndexer 删除索引器
     * */
    void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer delIndexer);

    /**
     * 节点分裂
     * */
    BNode split();

    /**
     * 返回当前节点的所有的key值集合
     * */
    List<K> keys();

    /**
     * 获取所有的值，如果没有的可以不实现，或者返回null
     * @return 返回当前节点存储的值
     * */
    List<V> values();

    /**
     * 元素key个数
     * @return 返回当前节点元素key的个数,删除和移动，数组的大小都会变的
     * */
    int keyNumber();
}

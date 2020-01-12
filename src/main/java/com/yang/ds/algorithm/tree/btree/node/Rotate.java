package com.yang.ds.algorithm.tree.btree.node;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;

/**
 * 实现B树的旋转
 * */
public interface Rotate<K extends Comparable<? super K>, V> {
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
}

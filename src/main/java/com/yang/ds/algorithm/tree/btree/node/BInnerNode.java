package com.yang.ds.algorithm.tree.btree.node;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;

import java.util.List;

public interface BInnerNode<K extends Comparable<? super K>, V> extends BNode<K, V> {
    /**
     * @param key 获取孩子
     * */
    BNode<K, V> getChild(K key);

    /**
     * 获取所有的孩子集合
     * */
    List<BNode<K, V>> children();

    /**
     * 删除孩子
     * @param index 删除孩子的索引
     * */

    BNode<K, V> delChild(int index);

    /**
     * 删除左侧第一个孩子
     * */


    BNode<K, V> delFirstLeftChild();

    /**
     * 删除右侧最后一个孩子
     * */

    BNode<K, V> delLastRightChild();

    /**
     * 创建删除的索引器
     * */

    BDelIndexer createDelIndexer(int childIdx);
}

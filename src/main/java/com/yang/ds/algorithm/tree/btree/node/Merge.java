package com.yang.ds.algorithm.tree.btree.node;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;

/**
 * 实现B树的合并
 * */
public interface Merge<K extends Comparable<? super K>,V> {
    /**
     * 节点合并
     * @param parent 父节点
     * @param sibling 兄弟节点
     * @param delIndexer 删除索引器
     * */
    void merge(BNode<K,V> parent, BNode<K,V> sibling, BDelIndexer delIndexer);
}

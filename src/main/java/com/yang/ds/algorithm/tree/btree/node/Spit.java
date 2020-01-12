package com.yang.ds.algorithm.tree.btree.node;

/**
 * 实现B树的分裂
 * */
public interface Spit<K extends Comparable<? super K>, V> {
    /**
     * 节点分裂
     * */
    BNode split();


}

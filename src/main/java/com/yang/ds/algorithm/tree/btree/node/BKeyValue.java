package com.yang.ds.algorithm.tree.btree.node;

public interface BKeyValue<K extends Comparable<? super K>, V> {
    K key();

    V value();
}

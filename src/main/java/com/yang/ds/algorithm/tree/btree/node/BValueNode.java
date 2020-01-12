package com.yang.ds.algorithm.tree.btree.node;

public interface BValueNode<K extends Comparable<? super K>, V> extends BNode<K, V> {
    BKeyValue<K, V> delete(K key, int loc);
}

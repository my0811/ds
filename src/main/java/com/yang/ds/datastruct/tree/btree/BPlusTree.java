package com.yang.ds.datastruct.tree.btree;

import java.util.List;

/**
 * B+tree接口
 * */
public interface BPlusTree<K extends Comparable<? super K>, V> extends BTree<K, V> {
    enum RangePolicy {
        EXCLUSIVE, INCLUSIVE
    }

    List<V> searchRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy);
}

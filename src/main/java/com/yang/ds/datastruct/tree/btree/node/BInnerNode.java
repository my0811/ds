package com.yang.ds.datastruct.tree.btree.node;

import java.util.List;

public interface BInnerNode<K extends Comparable<? super K>, V> extends BNode<K, V> {

    /**
     * 获取所有的孩子集合
     * */
    List<BNode<K, V>> children();

    /**
     * 根据指定索引获取孩子
     * @param childIdx
     * */
    BNode<K, V> getChild(int childIdx);

    /**
     * 根据key获取孩子,如果key相等，则从key的右侧值域查找孩子
     * @param key
     * */
    BNode<K, V> getChild(K key);

}

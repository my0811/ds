package com.yang.ds.datastruct.tree.bstree;

public interface BSNode<K extends Comparable<? super K>, V> {

    K key();

    V value();

    BSNode<K, V> left();

    BSNode<K, V> right();

    BSNode<K, V> parent();


}

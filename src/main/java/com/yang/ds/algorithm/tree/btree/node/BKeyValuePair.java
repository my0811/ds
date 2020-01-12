package com.yang.ds.algorithm.tree.btree.node;

public class BKeyValuePair<K extends Comparable<? super K>, V> implements BKeyValue<K, V> {
    private K key;
    private V value;

    public BKeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public BKeyValuePair(K key) {
        this.key = key;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

}

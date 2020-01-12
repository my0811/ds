package com.yang.ds.datastruct.hash;

public interface HashTable<K, V> {
    /**
     * 添加覆盖
     * @param key hash使用的key
     *@param value 存储的value
     * */
    void put(K key, V value);

    /**
     * 根据key删除对应的value
     *@param key 存储key值
     * */
    void remove(K key);

    /**
     * 根据key获取对应value
     *@param key 存储key值
     * */
    V get(K key);
}

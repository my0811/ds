package com.yang.ds.datastruct.graph;

/**
 * 图顶点操作对象，提供状态控制
 *
 * */
public class Vertex<K, V> {

    /**初始状态*/
    public static final int INIT_STATE = -1;

    /**状态*/
    private int state;

    /**顶点Key 用于查找，边链接的使用*/
    private K key;

    /**存储顶点的值*/
    private V value;

    public Vertex(K key, V value) {
        this.key = key;
        this.value = value;
        this.state = INIT_STATE;
    }

    public boolean isInitState() {
        return state == INIT_STATE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(key).toString();
    }
}

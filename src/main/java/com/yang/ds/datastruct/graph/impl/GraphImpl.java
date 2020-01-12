package com.yang.ds.datastruct.graph.impl;

import com.yang.ds.datastruct.graph.Graph;

/**
 * 无向图，数据结构实现，可以带有权重，默认是1，无权图相当于是权重为1的一种特殊的带权图
 *
 * 贼happy 写着写着，眸然回首，发现大体功能实现逻辑都一样，抽象的有点多，先这样吧,有机会在优化调整继承关系和接口
 * */
public class GraphImpl<K, V> extends AbstractGraph<K, V> implements Graph<K, V> {
    public GraphImpl(int size) {
        super(size);
    }

    public GraphImpl() {

    }

}

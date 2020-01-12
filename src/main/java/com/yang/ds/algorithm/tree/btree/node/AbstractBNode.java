package com.yang.ds.algorithm.tree.btree.node;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBNode<K extends Comparable<? super K>, V> implements BNode<K, V> {
    private List<K> keys;
    private int degree;

    protected AbstractBNode(int degree) {
        this.degree = degree;
        this.keys = new ArrayList<>(degree);
    }

    @Override
    public List<K> keys() {
        return this.keys;
    }

    @Override
    public int keyNumber() {
        return keys.size();
    }

    @Override
    public int degree() {
        return degree;
    }

    @Override
    public boolean isOverflow() {
        return keyNumber() > (degree - 1);
    }

    @Override
    public boolean isUnderflow() {
        return keyNumber() < (degree + 1) / 2 - 1;
    }

    @Override
    public boolean isAffluent() {
        return keyNumber() > (degree + 1) / 2 - 1;
    }

    protected int subKeyFrom() {
        return keyNumber() / 2;
    }

    protected int subKeyTo() {
        return keyNumber();
    }

    @Override
    public String toString() {
        return keys().toString();
    }
}

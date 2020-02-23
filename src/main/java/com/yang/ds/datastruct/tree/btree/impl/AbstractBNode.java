package com.yang.ds.datastruct.tree.btree.impl;

import com.yang.ds.datastruct.tree.btree.node.BDelIndexer;
import com.yang.ds.datastruct.tree.btree.node.BInnerNode;
import com.yang.ds.datastruct.tree.btree.node.BNode;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBNode<K extends Comparable<? super K>, V> implements BNode<K, V> {

    /**阶*/
    private int degree;

    /**key集合*/
    protected List<K> keys;

    protected AbstractBNode(int degree) {
        this.degree = degree;
        keys = new ArrayList<>(degree);
    }

    @Override
    public int keyNumber() {
        return keys.size();
    }

    @Override
    public List<K> keys() {
        return keys;
    }

    protected int degree() {
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

    /**
     * 平衡修复通用方法，适合所有种类的B树
     * 左旋、右旋、合并，对平衡修复，合并是分裂的逆操作
     * @param child 当前处理的孩子
     * @param indexer 删除辅助索引器，帮助获取旋转顶点、分裂key索引，左、右兄弟
     * */
    protected BNode fixBalance(BNode<K, V> child, BInnerNode parent, BDelIndexer indexer) {
        BNode rightSib = indexer.hasRightSib() ? parent.getChild(indexer.rightSibIdx()) : null;
        BNode leftSib = indexer.hasLeftSib() ? parent.getChild(indexer.leftSibIdx()) : null;

        if (indexer.hasLeftSib() && leftSib.isAffluent()) {// 右旋
            child.rightRotate(parent, leftSib, indexer);
        } else if (indexer.hasRightSib() && rightSib.isAffluent()) {// 左旋
            child.leftRotate(parent, rightSib, indexer);
        } else {// 合并
            BNode left = parent.getChild(indexer.mergeLeftIdx());
            BNode right = parent.getChild(indexer.mergeRightIdx());
            left.merge(parent, right, indexer);
            return left;
        }
        return null;
    }

    public List<V> values() {
        throw new UnsupportedOperationException("no value");
    }
}

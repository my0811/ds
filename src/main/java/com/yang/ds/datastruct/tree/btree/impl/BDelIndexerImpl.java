package com.yang.ds.datastruct.tree.btree.impl;

import com.yang.ds.datastruct.tree.btree.node.BDelIndexer;

/**
 * B+树获取节点的相关索引值
 * 1.分裂的父节点key对应的位置的index
 * 2.孩子兄弟节点的index
 * 3.旋转父节点key对应的index
 * 4.孩子的索引
 * */
public class BDelIndexerImpl implements BDelIndexer {

    private int keyNumber;
    private int curChildIdx;

    private BDelIndexerImpl(int curChildIdx, int keyNumber) {
        this.curChildIdx = curChildIdx;
        this.keyNumber = keyNumber;
    }

    public static BDelIndexerImpl newDelIndexer(int curChildIdx, int keyNumber) {
        return new BDelIndexerImpl(curChildIdx, keyNumber);
    }

    private int curChildIdx() {
        return curChildIdx;
    }


    @Override
    public boolean hasLeftSib() {
        return leftSibIdx() >= 0;
    }

    @Override
    public boolean hasRightSib() {
        return rightSibIdx() <= keyNumber;
    }

    @Override
    public int leftSibIdx() {
        return curChildIdx() - 1;
    }

    @Override
    public int rightSibIdx() {
        return curChildIdx() + 1;
    }

    @Override
    public int splitUpKeyIdx() {
        return mergeRightIdx() - 1;
    }

    @Override
    public int mergeLeftIdx() {
        return hasLeftSib() ? leftSibIdx() : curChildIdx();
    }

    @Override
    public int mergeRightIdx() {
        return mergeLeftIdx() + 1;
    }

    @Override
    public int leftRotateUpKeyIdx() {
        return curChildIdx();
    }

    @Override
    public int rightRotateUpKeyIdx() {
        return curChildIdx() - 1;
    }
}

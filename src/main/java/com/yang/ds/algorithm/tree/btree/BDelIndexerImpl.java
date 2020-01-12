package com.yang.ds.algorithm.tree.btree;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;

/**
 * B+树获取节点的相关索引值
 * 1.分裂的父节点key对应的位置的index
 * 2.孩子兄弟节点的index
 * 3.旋转父节点key对应的index
 * 4.孩子的索引
 * */
public class BDelIndexerImpl implements BDelIndexer {

    private int keyNumber;
    private int delChildIdx;

    private BDelIndexerImpl(int delChildIdx, int keyNumber) {
        this.delChildIdx = delChildIdx;
        this.keyNumber = keyNumber;
    }

    public static BDelIndexerImpl newDelIndexer(int delChildIdx, int keyNumber) {
        return new BDelIndexerImpl(delChildIdx, keyNumber);
    }

    private int delChildIdx() {
        return delChildIdx;
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
        return delChildIdx() - 1;
    }

    @Override
    public int rightSibIdx() {
        return delChildIdx() + 1;
    }

    @Override
    public int splitUpKeyIdx() {
        return mergeRightIdx() - 1;
    }

    @Override
    public int mergeLeftIdx() {
        return hasLeftSib() ? leftSibIdx() : delChildIdx();
    }

    @Override
    public int mergeRightIdx() {
        return mergeLeftIdx() + 1;
    }

    @Override
    public int leftRotateUpKeyIdx() {
        return delChildIdx();
    }

    @Override
    public int rightRotateUpKeyIdx() {
        return delChildIdx() - 1;
    }
}

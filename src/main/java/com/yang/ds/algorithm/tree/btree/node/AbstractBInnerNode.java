package com.yang.ds.algorithm.tree.btree.node;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;
import com.yang.ds.algorithm.utils.YUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBInnerNode<K extends Comparable<? super K>, V> extends AbstractBNode<K, V> implements BInnerNode<K, V> {
    private List<BNode<K, V>> children;

    protected AbstractBInnerNode(int degree) {
        super(degree);
        this.children = new ArrayList<>(degree);
    }

    @Override
    public List<BNode<K, V>> children() {
        return children;
    }

    @Override
    public V search(K key) {
        return getChild(key).search(key);
    }

    @Override
    public BKeyValue<K, V> delete(K key, int keyLoc) {
        // 获取孩子
        int loc = YUtils.binarySearch(keys(), key);
        int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
        BNode<K, V> child = getChild(childIdx);
        int findLoc = keyLoc >= 0 ? keyLoc : loc;
        BKeyValue<K, V> replaceKey = child.delete(key, findLoc);
        BDelIndexer delIndexer = createDelIndexer(childIdx);
        // 如果key为空，第二层节点的最后一个元素，找右侧兄弟节点替换，如果没有右侧兄弟节点，则会出发merge
        if (null == replaceKey && delIndexer.hasRightSib()) {
            replaceKey = getChild(delIndexer.rightSibIdx()).getFirstLeftKey();
        }
        // 找到了key递归回归的时候，替换对应后继key，找到了说明删除的key在非叶子节点存在
        if (loc >= 0) {
            setKey(loc, replaceKey);
        }
        if (child.isUnderflow()) {
            fix(child, delIndexer);
        }
        return replaceKey;
    }

    private void fix(BNode<K, V> child, BDelIndexer delIndexer) {
        BNode rightSib = getChild(delIndexer.rightSibIdx());
        BNode leftSib = getChild(delIndexer.leftSibIdx());

        if (delIndexer.hasLeftSib() && leftSib.isAffluent()) {// 右旋
            child.rightRotate(this, leftSib, delIndexer);
        } else if (delIndexer.hasRightSib() && rightSib.isAffluent()) {// 左旋
            child.leftRotate(this, rightSib, delIndexer);
        } else {// 合并
            BNode left = getChild(delIndexer.leftSibIdx());
            BNode right = getChild(delIndexer.rightSibIdx());
            left.merge(this, right, delIndexer);
        }
    }

    protected BNode<K, V> getChild(int childIdx) {
        if (childIdx < 0 || childIdx > keyNumber()) {
            return null;
        }
        return children().get(childIdx);
    }

}

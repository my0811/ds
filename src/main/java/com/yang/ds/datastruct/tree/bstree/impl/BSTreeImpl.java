package com.yang.ds.datastruct.tree.bstree.impl;

import com.yang.ds.datastruct.tree.bstree.BSNode;
import com.yang.ds.datastruct.tree.bstree.BSTree;

/**
 *
 * 二叉树实现
 * */

public class BSTreeImpl<K extends Comparable<? super K>, V> extends AbstractBSTree<K, V> implements BSTree<K, V> {

    private Node root;

    public BSTreeImpl() {

    }


    @Override
    public void add(K key, V value) {
        if (null == key) {
            throw new IllegalArgumentException("key not null");
        }
        // 为空树，直接插入root，返回
        if (isEmpty()) {
            root = new Node(key, value);
            return;
        }

        Node nNode = new Node(key, value);
        Node parent = findNode(key, root, INSERT);
        int cmp = nNode.key.compareTo(parent.key);
        // 判断在插入位置父节点的左右
        if (cmp < 0) {
            addChild(LEFT, parent, nNode);
        } else {
            addChild(RIGHT, parent, nNode);
        }
    }

    /**
     *
     *
     * */
    @Override
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key not null");
        }
        Node needDel = findNode(key, root, FIND);
        if (needDel == null) {
            return;
        }
        int lr = lr(needDel, needDel.parent);

        // case1叶子节点,直接删除
        if (isLeaf(needDel)) {
            // 删除为root节点，则root删除掉，整棵树删除掉
            if (needDel == root) {
                root = null;
            } else {
                addChild(lr, needDel.parent, null);
            }
            return;
        }

        // case2存在一个子树,删除节点的父节点直接链到删除节点子节点,删除needDel
        if (justOneChild(needDel)) {
            Node child = needDel.left != null ? needDel.left : needDel.right;
            // 删除节点是root则需要把root节点更换成删除节点的孩子节点
            if (needDel == root) {
                root = child;
            } else {
                addChild(lr, needDel.parent, child);
            }
            return;
        }

        // case3存在两个子树
        if (hasTowChild(needDel)) {
            // 选举出后继节点
            Node replacement = getMinNode(needDel.right);
            // 如果删除的为root节点，则更换root为选举的后继节点
            // 1.后继节点右侧孩子处理,后继节点父节点不是被删除的节点
            if (replacement.parent != needDel) {
                addChild(LEFT, replacement.parent, replacement.right);
            }

            // 2.后继节点替换删除节点，把删除节点的父节点、左子树、右子树转移到后继节点
            if (needDel == root) {
                replaceRoot(replacement);
            } else {
                addChild(lr, needDel.parent, replacement);
            }
            // 链接删除节点做孩子
            addChild(LEFT, replacement, needDel.left);
            // 链接删除节点右孩子，如果后继节点的父节点是删除节点，则无需处理右侧孩子
            if (replacement.parent != needDel) {
                addChild(RIGHT, replacement, needDel.right);
            }
        }
    }

    private void addChild(int lr, Node parent, Node child) {
        notNullNode(parent);
        if (lr == LEFT) {
            parent.left = child;
        } else if (lr == RIGHT) {
            parent.right = child;
        } else {
            throw new IllegalArgumentException("error child");
        }
        if (child != null) {
            child.parent = parent;
        }
    }

    private void replaceRoot(Node node) {
        node.parent = null;
        root = node;
    }

    @Override
    public BSNode root() {
        return root;
    }

    private class Node implements BSNode<K, V> {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private Node parent;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public V value() {
            return value;
        }

        @Override
        public BSNode<K, V> left() {
            return left;
        }

        @Override
        public BSNode<K, V> right() {
            return right;
        }

        @Override
        public BSNode<K, V> parent() {
            return parent;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}

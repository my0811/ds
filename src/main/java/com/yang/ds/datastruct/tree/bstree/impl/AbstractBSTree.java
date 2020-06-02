package com.yang.ds.datastruct.tree.bstree.impl;

import com.yang.ds.datastruct.tree.bstree.BSNode;
import com.yang.ds.datastruct.tree.bstree.BSTree;

import java.util.*;

public abstract class AbstractBSTree<K extends Comparable<? super K>, V> implements BSTree<K, V> {

    /**节点方向*/
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    /**操作类型*/
    public static final int INSERT = 1;
    public static final int FIND = 2;

    /**
     *判断节点的方向，孩子节点在父节点的哪一侧
     * @param child 孩子节点
     * @param parent 父节点
     *
     * */
    protected int lr(BSNode child, BSNode parent) {
        notNullNode(parent);
        if (child == parent.left()) {
            return LEFT;
        } else if (child == parent.right()) {
            return RIGHT;
        } else {
            throw new IllegalArgumentException("error lr");
        }
    }

    protected void notNullNode(BSNode... nodes) {
        for (BSNode node : nodes) {
            if (node == null) {
                throw new IllegalArgumentException("node not is null" + node);
            }
        }
    }

    /**
     * 根据操类型查找节点，如果OPT为INSERT返回插入节点的位置的父节点,如果为FIND则找到key相同的节点返回
     * @param key 查找key
     * @param begin 从树的哪个节点位置开始查找
     * @param opt 操作类型INSERT,FIND
     *
     * */
    protected <T extends BSNode<K, V>> T findNode(K key, T begin, int opt) {
        notNullNode(begin);
        BSNode<K, V> cur = begin;
        BSNode<K, V> parent = begin;
        while (cur != null) {
            parent = cur;
            int cmp = key.compareTo(cur.key());
            if (cmp == 0 && opt == FIND) {
                break;
            }
            if (cmp < 0) {
                cur = cur.left();
            } else {
                cur = cur.right();
            }
        }
        return opt == FIND ? (T) cur : (opt == INSERT ? (T) parent : null);
    }

    protected boolean isLeaf(BSNode node) {
        notNullNode(node);
        if (node.left() == null && node.right() == null) {
            return true;
        }
        return false;
    }

    protected boolean justOneChild(BSNode node) {
        notNullNode(node);
        if (node.left() == null && node.right() == null) {
            return false;
        }
        if (node.left() != null && node.right() != null) {
            return false;
        }
        return true;
    }

    protected boolean hasTowChild(BSNode node) {
        notNullNode(node);
        if (node.left() != null && node.right() != null) {
            return true;
        }
        return false;
    }

    protected <T extends BSNode<K, V>> T getMinNode(T begin) {
        notNullNode(begin);
        BSNode<K, V> cur = begin;
        while (cur.left() != null) {
            cur = cur.left();
        }
        return (T) cur;
    }

    @Override
    public V getMin() {
        if (isEmpty()) {
            return null;
        }
        return (V) getMinNode(root()).value();
    }

    @Override
    public V getMax() {
        if (isEmpty()) {
            return null;
        }
        BSNode<K, V> cur = root();
        while (cur.right() != null) {
            cur = cur.right();
        }
        return cur.value();
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (isEmpty()) {
            return null;
        }
        BSNode<K, V> node = findNode(key, root(), FIND);
        return node != null ? node.value() : null;
    }

    public abstract BSNode root();

    protected boolean isEmpty() {
        return root() == null;
    }

    @Override
    public List<V> midErgodic() {
        List<V> values = new ArrayList<>();
        midErgodic(root(), values);
        return values;
    }

    private void midErgodic(BSNode<K, V> cur, List<V> values) {
        // 边界值
        if (cur == null) {
            return;
        }
        midErgodic(cur.left(), values);
        values.add(cur.value());
        midErgodic(cur.right(), values);
    }

    @Override
    public List<V> preErgodic() {
        List<V> values = new ArrayList<>();
        preErgodic(root(), values);
        return values;
    }

    private void preErgodic(BSNode<K, V> cur, List<V> values) {
        if (cur == null) {
            return;
        }
        values.add(cur.value());
        preErgodic(cur.left(), values);
        preErgodic(cur.right(), values);
    }

    @Override
    public List<V> afterErgodic() {
        List<V> values = new ArrayList<>();
        afterErgodic(root(), values);
        return values;
    }

    private void afterErgodic(BSNode<K, V> cur, List<V> values) {
        if (cur == null) {
            return;
        }
        afterErgodic(cur.left(), values);
        afterErgodic(cur.right(), values);
        values.add(cur.value());
    }

    @Override
    public String toString() {
        // 采用广度优先遍历
        /*return null;*/
        return bfsPrint(root());
    }

    /**
     * 二叉树广度优先遍历，打印二叉树的树状结构输出
     *
     * */
    private String bfsPrint(BSNode root) {
        if (isEmpty()) {
            return "empty tree what ?";
        }
        StringBuilder printBuilder = new StringBuilder();

        // 获取二叉树的深度
        int maxLevel = getDepth(root);
        // 获取完全二叉树总节点数量
        int fullTotal = (int) (Math.pow(2, maxLevel) - 1);


        // 每一层第一个开始的节点索引值
        int start = 0;
        // 每一层节点间隔的单位距离个数(单位就是一个节点的距离)
        int stepSize = 0;
        // 每个节点的在树中每一层的索引值，每一层都是从1开始
        int index = 0;
        // 上一个层级
        int preLevel = 1;
        // 广度优先遍历处理队列
        Queue<BSNode> bfsQue = new LinkedList<>();
        // 每一层每个节点的所以维护，方便子节点从map中获取父节点，然后
        Map<BSNode, Integer> indexMap = new HashMap<>();
        // 完全二叉树映射为数组初始化
        BSNode[] nodes = new BSNode[fullTotal];
        bfsQue.offer(root);
        indexMap.put(root, 1);
        while (!bfsQue.isEmpty()) {
            BSNode cur = bfsQue.poll();
            // 获取当前节点所在树的底基层
            int curLevel = levelOfFullTree(cur);
            // 说明上一层已经处理完了，可以打印上一层了
            if (curLevel > preLevel) {
                printLevel(nodes, printBuilder, preLevel);
            }
            // 每次处理上一次处理的一半，再向左一半，root节点一定在数组最中间，第一次处理总数组长度的一半(数组索引向下取整-1) 数组索引=size/2
            start = (int) (Math.pow(2, maxLevel - curLevel) - 1);
            // 节点间的间距刚好是上一次处理的长度，所以+1
            stepSize = (int) Math.pow(2, maxLevel - curLevel + 1);
            if (cur == root) {
                index = indexMap.get(root);
            } else {
                int lr = lr(cur, cur.parent());
                if (LEFT == lr) {
                    index = 2 * indexMap.get(cur.parent()) - 1;
                } else {
                    index = 2 * indexMap.get(cur.parent());
                }
                indexMap.put(cur, index);
            }
            // 节点放入完全二叉树数组中
            nodes[start + (index - 1) * stepSize] = cur;

            // 广度优先，处理完当前节点，然后把当前节点下一层孩子按照顺序入队，然后再按顺序出队当前节点的相邻节点...递归
            if (cur.left() != null) {
                bfsQue.offer(cur.left());
            }
            if (cur.right() != null) {
                bfsQue.offer(cur.right());
            }
            preLevel = curLevel;
        }
        // 最后一层处理已跳出循环，把最后一次的处理一次
        printLevel(nodes, printBuilder, preLevel);
        return printBuilder.append("\n").toString();
    }

    /**
     * 打印每一层节点数组，如果当前位置是null则用占位符顶替，
     *
     * */
    private void printLevel(BSNode[] nodes, StringBuilder printBuilder, int level) {
        String placeholder = "-";
        int nodeSize = 3;
        String nodePlaceholder = "---";
        printBuilder.append("\n");
        printBuilder.append(level + "|");
        for (int i = 0; i < nodes.length; i++) {
            StringBuilder sb = new StringBuilder();
            if (null == nodes[i]) {
                sb.append(nodePlaceholder);
                printBuilder.append(sb.toString());
                continue;
            }
            sb.append(nodes[i].toString());
            int len = sb.toString().length();
            if (len < nodeSize) {
                int diff = nodeSize - len;
                for (int j = 0; j < diff; j++) {
                    sb.append(placeholder);
                }
            } else if (len > nodeSize) {
                sb = new StringBuilder(sb.substring(0, nodeSize - 1)).append("?");
            }
            printBuilder.append(sb.toString());
            // 清空存在数据的位置的数据，方便数据复用
            nodes[i] = null;
        }
        printBuilder.append("|");
    }

    /**
     * 获取树的深度 (深度从1开始算，root层为第一层)
     *
     * 解决左子树的最大深度，右子树最大什么，去最大值，整棵树递归，左子树和右子树最大深度，最后到root
     *
     * 层级从1开始，也就是从最底层左右子树都没有的一个节点开始，就是开始+1直到加到root节点，然后root+1
     *
     *
     *
     *        5       3
     *     3     8    2
     *   2   4 7   9  1
     *
     * */
    private int getDepth(BSNode cur) {
        // 边界值
        if (null == cur) {
            return 0;
        }
        return 1 + Math.max(getDepth(cur.left()), getDepth(cur.right()));
    }

    /**
     *当前节点在树的第几层
     * */
    private int levelOfFullTree(BSNode node) {
        int depth = 0;
        BSNode cur = node;
        while (cur != null) {
            depth++;
            cur = cur.parent();
        }
        return depth;
    }
}

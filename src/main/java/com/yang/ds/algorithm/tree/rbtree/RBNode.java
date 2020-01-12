package com.yang.ds.algorithm.tree.rbtree;

public class RBNode {
    public static final int RED = 1;
    public static final int BLACK = 2;
    // 颜色，1：红色，2黑色
    private int color;

    // 数据
    private int data;

    // 左子节点
    private RBNode leftChildNode;

    // 右子节点
    private RBNode rightChildNode;

    // 父节点
    private RBNode parentNode;

    public RBNode() {
        this.data = data;
    }

    public RBNode(int color, int data, RBNode leftChildNode, RBNode rightChildNode, RBNode parentNode) {
        this.color = color;
        this.data = data;
        this.leftChildNode = leftChildNode;
        this.rightChildNode = rightChildNode;
        this.parentNode = parentNode;
    }

    public RBNode(int data) {
        this(RED, data, null, null, null);
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public RBNode getLeftChildNode() {
        return leftChildNode;
    }

    public void setLeftChildNode(RBNode leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    public RBNode getRightChildNode() {
        return rightChildNode;
    }

    public void setRightChildNode(RBNode rightChildNode) {
        this.rightChildNode = rightChildNode;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public RBNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(RBNode parentNode) {
        this.parentNode = parentNode;
    }

}

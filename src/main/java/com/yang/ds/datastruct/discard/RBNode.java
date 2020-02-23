package com.yang.ds.datastruct.discard;
@Deprecated
public class RBNode {
    public static final int RED = 1;
    public static final int BLACK = 2;
    // 颜色，1：红色，2黑色
    private int color;

    // 数据
    private int data;

    // 左子节点
    private RBNode left;

    // 右子节点
    private RBNode right;

    // 父节点
    private RBNode parentNode;

    public RBNode() {
        this.data = data;
    }

    public RBNode(int color, int data, RBNode left, RBNode right, RBNode parentNode) {
        this.color = color;
        this.data = data;
        this.left = left;
        this.right = right;
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

    public RBNode getLeft() {
        return left;
    }

    public void setLeft(RBNode left) {
        this.left = left;
    }

    public RBNode getRight() {
        return right;
    }

    public void setRight(RBNode right) {
        this.right = right;
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

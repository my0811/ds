package com.yang.ds.datastruct.discard;

/**
 * 红黑树数据节点
 */
@Deprecated
public class RedBlackNode {
    public enum NodeColor {
        RED,
        BLACK;
    }

    // true 为红色，false为黑色
    private NodeColor color;

    // 数据
    private int data;

    // 左子节点
    private RedBlackNode leftChildNode;

    // 右子节点
    private RedBlackNode rightChildNode;

    // 父节点
    private RedBlackNode parentNode;

    public RedBlackNode() {
        this.data = data;
    }

    public RedBlackNode(NodeColor color, int data, RedBlackNode leftChildNode, RedBlackNode rightChildNode, RedBlackNode parentNode) {
        this.color = color;
        this.data = data;
        this.leftChildNode = leftChildNode;
        this.rightChildNode = rightChildNode;
        this.parentNode = parentNode;
    }

    public RedBlackNode(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public RedBlackNode getLeftChildNode() {
        return leftChildNode;
    }

    public void setLeftChildNode(RedBlackNode leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    public RedBlackNode getRightChildNode() {
        return rightChildNode;
    }

    public void setRightChildNode(RedBlackNode rightChildNode) {
        this.rightChildNode = rightChildNode;
    }

    public NodeColor getColor() {
        return color;
    }

    public void setColor(NodeColor color) {
        this.color = color;
    }

    public RedBlackNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(RedBlackNode parentNode) {
        this.parentNode = parentNode;
    }

}

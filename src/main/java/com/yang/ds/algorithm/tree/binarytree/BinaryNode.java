package com.yang.ds.algorithm.tree.binarytree;

/**
 * 二叉树node节点
 */
public class BinaryNode {
    private int data;
    private BinaryNode leftChildNode;
    private BinaryNode rightChildNode;

    public BinaryNode(int data) {
        this.data = data;
    }

    public BinaryNode getLeftChildNode() {
        return leftChildNode;
    }

    public void setLeftChildNode(BinaryNode leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    public BinaryNode getRightChildNode() {
        return rightChildNode;
    }

    public void setRightChildNode(BinaryNode rightChildNode) {
        this.rightChildNode = rightChildNode;
    }

    public int getData() {
        return data;
    }
}

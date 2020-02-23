package com.yang.ds.datastruct.tree.btree.impl;

import com.yang.ds.datastruct.tree.btree.node.BInnerNode;
import com.yang.ds.datastruct.tree.btree.node.BNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BtreeStringBuilder {
    public static String toString(BNode root) {
        // 从最顶层，一层一层向下遍历，一个节点的所有所属孩子作为一个单位打印,方便看出层级关系
        Queue<List<BNode>> childrenQueue = new LinkedList<>();
        // 添加打印字符串
        StringBuilder sb = new StringBuilder();

        // root节点先入队，作为第一批孩子，r
        childrenQueue.offer(Arrays.asList(root));

        // 每一层节点出队，打印
        while (!childrenQueue.isEmpty()) {
            // 下一层孩子的开始，声明一个que保存下一层孩子
            Queue<List<BNode>> nextChildQueue = new LinkedList<>();
            while (!childrenQueue.isEmpty()) {
                List<BNode> nodes = childrenQueue.poll();
                // begin
                sb.append("{");
                for (BNode node : nodes) {
                    sb.append(node.toString());
                    if (node instanceof BInnerNode) {
                        nextChildQueue.offer(((BInnerNode) node).children());
                    }
                }
                sb.append("}");
                // end
                if (!childrenQueue.isEmpty()) {
                    sb.append(",");
                } else {
                    sb.append("\n");
                }
            }
            // 继续下一层孩子的打印
            childrenQueue = nextChildQueue;
        }
        return sb.toString();
    }
}

package com.yang.ds.datastruct.graph;

/**
 * 图边的操作对象
 * 提供开始顶点，和到达顶点，提供权重，如果是无权图，默认权重是1
 * */
public class Edge<K> implements Comparable<Edge<K>> {
    /**默认权重*/
    public static final int DEFAULT_WEIGHT = 1;

    /**权重，如果是无权图，则默认权重是1*/
    private int weight = DEFAULT_WEIGHT;

    /**起始顶点*/
    private K from;

    /**结束顶点*/
    private K to;

    public Edge(K from, K to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge() {

    }

    public Edge(K from, K to) {
        this(from, to, DEFAULT_WEIGHT);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public K getFrom() {
        return from;
    }

    public void setFrom(K from) {
        this.from = from;
    }

    public K getTo() {
        return to;
    }

    public void setTo(K to) {
        this.to = to;
    }

    @Override
    public int compareTo(Edge other) {
        if (other == null) {
            return 1;
        }
        // 倒叙,方便查找最小路径和最小生成树
        int cmp = Integer.valueOf(weight).compareTo(other.getWeight());
        return cmp < 0 ? 1 : -1;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(from)
                .append(to).append("(")
                .append(weight).append(")").toString();
    }
}

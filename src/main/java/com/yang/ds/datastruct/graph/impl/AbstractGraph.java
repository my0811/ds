package com.yang.ds.datastruct.graph.impl;

import com.yang.ds.algorithm.hash.YLinkedHashTable;
import com.yang.ds.datastruct.graph.DAG;
import com.yang.ds.datastruct.graph.Edge;
import com.yang.ds.datastruct.graph.Graph;
import com.yang.ds.datastruct.graph.Vertex;
import com.yang.ds.datastruct.hash.HashTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 实现图数据结构抽象类
 * */
public class AbstractGraph<K, V> implements Graph<K, V> {

    private static final int DEFAULT_SIZE = 1;
    /**最大保存顶点个数为2^30*/
    private static final int MAX_VERTEXES = 1 << 30;

    /**邻接矩阵，初始化边的权重值，默认一个非常大的值*/
    protected static final int INF = 1 << 30;

    /**保存顶点数组*/
    protected Vertex[] vertexes;

    /**实现图的边，邻接矩阵实现方式，实现简单*/
    protected int[][] adjMat;

    /**hash表方便根据key值来查找顶点*/
    protected HashTable<K, Integer> table;

    /**栈结构，进行深度优先搜索*/
    protected GraphStack<Integer> gStack;

    /**双端队列，实现广度优先搜索*/
    protected GraphDeque<Integer> graphDeque;

    /**优先队列，权值树的最小生成树使用*/
    protected GraphPriorityQueue<Edge<K>> graphPriorityQueue;

    /**当前顶点个数*/
    protected int size;

    protected AbstractGraph(int size) {
        if (size == 0) {
            throw new IllegalArgumentException("size is illegal" + size);
        }

        // 初始化顶点数组
        vertexes = new Vertex[size];

        // 初始化矩阵
        adjMat = adjMatFor(new int[size][size], size, 0);

        // 初始化hash表
        table = new YLinkedHashTable<>((size << 1));

        //初始化 栈，队列
        gStack = new GraphStack<>(size);
        graphDeque = new GraphDeque<>(size);
        graphPriorityQueue = new GraphPriorityQueue<>();
    }

    /**
     * 邻接矩阵更新
     * */
    private int[][] adjMatFor(int[][] adjMat, int newCap, int oldCap) {
        for (int i = 0; i < newCap; i++) {
            for (int j = 0; j < newCap; j++) {
                if (oldCap == 0) {// 初始化则全部赋值默认值
                    adjMat[i][j] = INF;
                } else if (i >= oldCap) {// 行超过老长度，则剩余的全部赋默认值
                    adjMat[i][j] = INF;
                } else {
                    if (j >= oldCap) {// 没有超过老长度，则把老的数组列扩容的部分赋值默认值
                        adjMat[i][j] = INF;
                    }
                }
            }
        }
        return adjMat;
    }

    protected AbstractGraph() {
        this(DEFAULT_SIZE);
    }


    @Override
    public void add(Vertex<K, V> vertex) {
        resize();
        // 不存在插入
        if (table.get(vertex.getKey()) == null) {
            vertexes[size] = vertex;
            table.put(vertex.getKey(), size);
            size++;
        } else {// 存在更新
            vertexes[size] = vertex;
        }
    }

    /**
     * 数组扩容
     *
     * */
    private void resize() {
        int minCap = size + 1;
        int oldCap = vertexes.length;
        // 扩容 2倍
        if (minCap - oldCap > 0) {
            int newCap = oldCap << 1;
            if (newCap < 0 || newCap - MAX_VERTEXES > 0) {
                throw new IllegalArgumentException("graph capacity is over flow " + newCap);
            }
            // 扩容顶点数组
            Vertex[] newVertexes = new Vertex[newCap];
            System.arraycopy(vertexes, 0, newVertexes, 0, size);
            vertexes = newVertexes;
            // 扩容邻接矩阵
            int[][] newAdjMat = new int[newCap][newCap];
            for (int i = 0; i < newCap; i++) {
                if (i < oldCap) {
                    newAdjMat[i] = Arrays.copyOf(adjMat[i], newCap);
                }
            }
            adjMat = adjMatFor(newAdjMat, newCap, oldCap);
        }
    }

    @Override
    public void addEdge(Edge<K> edge) {
        checkEdge(edge.getFrom(), edge.getTo());

        int from = table.get(edge.getFrom());
        int to = table.get(edge.getTo());
        int weight = edge.getWeight();
        if (this instanceof DAG) {// 有向图
            adjMat[from][to] = weight;
        } else {// 无向图
            adjMat[from][to] = weight;
            adjMat[to][from] = weight;
        }
    }

    /**
     * 深度优先访问实现逻辑
     *
     *采用栈的结构来实现，不断的向栈中添加，所以叫深度优先搜索
     *初始化：指定一个顶点作为开始的顶点，然后放入栈中同时标识为visited
     *case1:如果栈顶元素存在邻接顶点，则继续获取邻接点(没有访问过的邻接节点，也就是标志不是visited)，放入栈中，并且标记为visited
     *case2:不存在邻接顶点，则当前栈如果不为空则pop弹出栈栈顶元素
     *case3:如果case1和case2都不能满足，则整个搜索过程结束，因为栈中已经没有元素了，肯定是所有的元素都访问了一遍
     * */
    @Override
    public List<Edge<K>> dfs(K from) {
        checkEdge(from);
        if (isEmpty()) {
            return Collections.emptyList();
        }

        int visit = 1;
        int begin = table.get(from);
        gStack.push(begin);
        vertexes[begin].setState(visit);
        // 初始化
        List<Edge<K>> edges = new ArrayList<>(size);
        Edge curEdge;
        while (!gStack.isEmpty()) {
            int v = gStack.peek();

            // 获取邻接未访问的节点
            int adj = getAdjUnVisit(v);
            if (adj != -1) {// 当前顶点存在邻接顶点继续入栈
                // 记录边
                curEdge = new Edge();
                curEdge.setFrom(vertexes[v].getKey());
                curEdge.setTo(vertexes[adj].getKey());
                edges.add(curEdge);
                // 入栈标识已经访问过
                gStack.push(adj);
                vertexes[adj].setState(visit);
            } else {// 当前顶点不存在邻接顶点弹出当前顶点
                gStack.pop();
            }
        }
        // 清除访问
        clearVertexes();
        return edges;
    }

    private void clearVertexes() {
        for (int i = 0; i < size; i++) {
            vertexes[i].setState(Vertex.INIT_STATE);
        }
    }

    private K toKey(Vertex vertex) {
        return (K) vertex.getKey();
    }

    /**
     * 获取指定的顶点的邻接顶点
     * */
    private int getAdjUnVisit(int from) {
        for (int i = 0; i < size; i++) {
            if (isAdjUnVisit(from, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取所有的邻接节点
     * */
    private List<Integer> getAllAdjUnVisit(int from) {
        List<Integer> adjList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (isAdjUnVisit(from, i)) {
                adjList.add(i);
            }
        }
        return adjList;
    }

    private boolean isAdjUnVisit(int from, int to) {
        if (!vertexes[to].isInitState()) {
            return false;
        }
        if (adjMat[from][to] == INF) {
            return false;
        }
        return true;
    }

    /**
     * 广度优先搜索，实现细节
     *采用队列来实现,有点类似树的层级遍历，先把邻接的最近的一层的所有邻接节点获取，所以叫以广度作为优先的搜索
     *初始化：指定一个开始的顶点放入到队列，并且标记为visited
     *case1:出队队列中的顶点，如果顶点存在临街顶点，则按顺序获取所有的邻接顶点，让如到队列中标记visited
     *case2:如果出队的顶点没有了邻接顶点，则执行case1
     *case3：如果队列为空了，则处理结束，说明所有顶点都访问了
     * */
    @Override
    public List<Edge<K>> bfs(K from) {
        checkEdge(from);
        List<Edge<K>> edges = new ArrayList<>(size);
        if (isEmpty()) {
            return Collections.emptyList();
        }

        // 初始化访问
        int visit = 1;
        int begin = table.get(from);
        graphDeque.addFirst(begin);
        vertexes[begin].setState(visit);
        Edge curEdge;
        while (!graphDeque.isEmpty()) {
            int v = graphDeque.removeLast();
            // 获取所有的邻接节点
            List<Integer> adjList = getAllAdjUnVisit(v);
            if (!adjList.isEmpty()) {
                for (int adj : adjList) {
                    // 记录边
                    curEdge = new Edge<>();
                    curEdge.setFrom(vertexes[v].getKey());
                    curEdge.setTo(vertexes[adj].getKey());
                    edges.add(curEdge);
                    // 邻接访问的顶点入队列，并且标识已经访问过
                    graphDeque.addFirst(adj);
                    vertexes[adj].setState(visit);
                }
            }
        }
        clearVertexes();
        return edges;
    }

    /**
     * @see Graph
     * 最小生成树
     *
     *初始化:首先定义一个V集合保存访问过的顶点，同时设置一个E集合保存获取到的最小的边，
     * 声明一个优先队列,同时指定一个开始的顶点添加到V集合中，开始顶点作为当前处理顶点
     *循环:遍历N个顶点，执行N-1次循环即可，一位边的条数比顶点少1，所以不用遍历所有的顶点
     *case1更改当前处理节点的标志位，相当于放入到V集合当中
     *case2如果优先队列中没有边了，则处理结束
     *case3:如果遍历的节点不存在V集合中，并且与当前节点邻接，则把这个边放入到优先队列中
     *case4:从优先队列中出队一个最小边(也就是优先级最高的，我们相当于优先级队列是倒叙的，用小顶堆来实现)
     *case5:如果从优先队列中获取的最小边的起始顶点和终点顶点都在V集合中，则继续case4，否则进行case6
     *case6:获取最小边的终点顶点作为下一次处理的节点，也就是当前处理节点从上一次处理的顶点改成此顶点
     *注意:（程序实现一般不会创建集合来保存，而是用标志位来逻辑上放入V集合，我们每次获取遍历还是原有的顶点数组）
     *case3，需要做到如下过滤
     *1. 获取的顶点已经存在V集合过滤
     *2. 获取的顶点就是当前处理的顶点则过滤
     *3.获取到的顶点和当前处理节点不邻接的也就是权重为INF的过滤掉
     *case5过滤,
     *因为优先队列是每个顶点都会获取邻接的边放入到优先队列，可能有一些边在上次处理已经放入了，
     *但是由于节点已经放入了V集合，访问过的节点内部的链接的边，不可以再次使用了，
     *所以要出队删除，继续获取下一个不在V集合中的优先级最高的边，也就是最小的边
     * */
    @Override
    public List<Edge<K>> mTree(K from, boolean withWeight) {
        checkEdge(from);
        if (isEmpty()) {
            return Collections.emptyList();
        }

        if (Boolean.FALSE == withWeight) {
            return dfs(from);
        }

        // 最小生成树,只需要N-1条边，N为顶点个数
        List<Edge<K>> setE = new ArrayList<>(size);
        int setV = 1;
        int curIdx = table.get(from);
        /*
         * 宏观上看就把V集合看做一个整体,然后获取到达V集合中的所有连接的边画一条交叉线，相交的边
         * 通过不断向优先队列中添加边，来实现获取V集合邻接的边的交叉线边的逻辑
         * */
        for (int j = 0; j < size - 1; j++) {
            // 标记当前处理的顶点已经在V集合中，不要在对V集合中的顶点处理
            vertexes[curIdx].setState(setV);
            for (int i = 0; i < size; i++) {
                // 过滤掉存在V集合中的顶点，V集合内部存在的边不需处理，因为已经处理过最小
                if (!vertexes[i].isInitState()) {
                    continue;
                }
                // 过滤掉，当前处理的顶点，没有任何意义
                if (curIdx == i) {
                    continue;
                }
                // 不能连接的边过滤掉
                if (adjMat[curIdx][i] == INF) {
                    continue;
                }
                K fromK = toKey(vertexes[curIdx]);
                K toK = toKey(vertexes[i]);
                int wight = adjMat[curIdx][i];
                graphPriorityQueue.add(new Edge<K>(fromK, toK, wight));
            }
            // 没有最小边了，说明已经都获取完成了
            if (graphPriorityQueue.isEmpty()) {
                return setE;
            }
            while (!graphPriorityQueue.isEmpty()) {
                Edge<K> edge = graphPriorityQueue.remove();
                int fromIdx = table.get(edge.getFrom());
                int toIdx = table.get(edge.getTo());
                if (!vertexes[fromIdx].isInitState() && !vertexes[toIdx].isInitState()) {
                    continue;
                }
                // 把最小的路径添加到E集合中
                setE.add(edge);
                // 找到了最小边，以最小边所在顶点为当前处理顶点继续处理下一个顶点
                curIdx = toIdx;
                break;
            }
        }
        clearVertexes();
        return setE;
    }

    /**
     * @see Graph
     * 最小路径
     *
     * 1.定义S集合，里面保存每次产生的最小路径,定义U保存从开始顶点到其他所有顶点边，
     * 并且把开始顶点加入到S集合中作为第一此最开始的顶点（此例中开始顶点为A）
     *
     * 2. 每次从U集合中找出一条最小的边，然后放入到S集合中
     *
     * 3.然后从S集合中获取上一次加入的最小边对应的顶点，以此顶点为开始和其他边计算权值+此最小边的权值，
     * 如果比U集合中对应的边的权值小,则更新对应的边的权值为刚才计算的权值的和，
     * 并且把U集合对应的这个边的上一级指向改成此顶点,
     * （默认一开始上一级顶点的指向都是开始顶点，此例子中开始顶点是A）
     *
     *
     *
     * */

    @Override
    public List<Edge<K>> mPath(K from, K to) {
        checkEdge(from, to);
        if (isEmpty()) {
            return Collections.emptyList();
        }

        ShortPath[] setU = new ShortPath[size];
        int setS = 1;
        int begin = table.get(from);
        // 不需要找A-A的边，所以直接标记
        vertexes[begin].setState(setS);
        // 从指定顶点开始，把所有的以开始顶点链接其他顶点的边放入到U集合中
        for (int i = 0; i < size; i++) {
            setU[i] = new ShortPath(begin, adjMat[begin][i]);
        }

        /*
         * 与最小生成树不同，这里需要每个顶点都处理，最小生成树N-1条边，所以循环可以少一次，就能找出最少的边
         * */
        for (int i = 0; i < size; i++) {
            // 从U集合中找出一条权值最小的边
            int mE = getMinWeightEdge(setU);
            // U集合中的所有最小边已经全部转移到S集合中了，可以结束处理
            if (mE == -1) {
                break;
            }
            // 从U集合中找到的最小边加入到S集合
            vertexes[mE].setState(setS);
            // 重新更新U集合中的边的最小权值和边的上一级指向
            updateU(mE, setU);
        }
        List<Edge<K>> edges = new ArrayList<>(size);
        int toIdx = table.get(to);
        int prevIdx = -1;
        while (prevIdx != begin) {
            prevIdx = setU[toIdx].prev;
            K fromK = (K) vertexes[prevIdx].getKey();
            K toK = (K) vertexes[toIdx].getKey();
            int weight = adjMat[prevIdx][toIdx];
            edges.add(new Edge(fromK, toK, weight));
            toIdx = prevIdx;
        }
        // 转换成正向路径
        Collections.reverse(edges);
        // clear
        clearVertexes();
        return edges;
    }

    @Override
    public int size() {
        return size;
    }

    private void updateU(int mE, ShortPath[] setU) {
        // 从U集合中找出的最小边的权重(且已经转移到S集合中了)
        int mW = setU[mE].weight;
        int sumW;
        int mEtoOtherW;
        int nowW;
        /*
         * 这里面实现，选择出来的当前最小边，以最小边为开始点到达U集合中每一个边的的到达顶点组成新的邻接路径
         * 然后用当前最小路径的全职和组成的这个邻接边的权值累加，比较
         * 1：如果<当前边则更新U集合中此边的权值为累加和
         * 2：如果>则不处理，因为我们需要获取是最小路径，最大路径相反求法即可
         * eg:当前最小边为AB(5)则需要 5+BD>?AD 5+BC>?AC,5+BE>AE?5+BF=>BF?
         * if (5+BD>AD)--> AD=BD(AB)(相当于ABD)
         *
         * */
        for (int i = 0; i < setU.length; i++) {
            // 相当于如果选择出了最小路径加入到了S集合，所以U集合转移到S集合中，这里需要过滤
            if (!vertexes[i].isInitState()) {
                continue;
            }
            // 最小边与其他顶点边的权重
            mEtoOtherW = adjMat[mE][i];
            // 与现在U集合对应的边进行权值累加
            sumW = mW + mEtoOtherW;
            // 当前U集合所对应的边的权重
            nowW = setU[i].weight;

            //以最小边顶点为出发顶点，到其他对应的顶点的边的权重
            if (sumW < nowW) {
                setU[i].prev = mE;
                setU[i].weight = sumW;
            }
        }
    }

    /**
     * 获取最小权重的边
     *
     * */
    private int getMinWeightEdge(ShortPath[] setU) {
        int minW = INF;
        int minIdx = -1;
        for (int i = 0; i < setU.length; i++) {
            // 相当于如果选择出了最小路径加入到了S集合，所以U集合转移到S集合中，这里需要过滤
            if (!vertexes[i].isInitState()) {
                continue;
            }
            if (minW > setU[i].weight) {
                minW = setU[i].weight;
                minIdx = i;
            }
        }
        return minIdx;
    }

    private void checkEdge(K... ks) {
        for (K k : ks) {
            if (table.get(k) == null) {
                throw new IllegalArgumentException("illegal vertex key" + k);
            }
        }
    }

    private boolean isEmpty() {
        return size == 0;
    }

    /**求最短路径临时使用类*/
    private static class ShortPath {
        private int prev;
        private int weight;

        public ShortPath(int prev, int weight) {
            this.prev = prev;
            this.weight = weight;
        }
    }

}

package com.yang.ds.datastruct.graph;

import java.util.List;

/**
 * 图顶级接口，需要实现此接口实现图的基本功能
 * 大体的复杂度在O(V^2)(比如拓扑排序)到O(|V|+|E|)/O(|V|)/O(|E|)
 *
 *
 *无向图
 *
 *=========================图逻辑结构===============================================
 *    ↙↗B↔ ↔ ↔C
 *  ↙↗
 * ↗↙
 * A ↔ ↔ ↔ D
 *  ↖↘
 *    ↖↘
 *      ↖↘E
 *
 *=========================图边表示(临接矩阵)=========================================
 *
 *    A   B   C   D   E
 *-----------------------   1. 两个点之间存在边，表示为1，不存在表示为0
 *A   0   1   0   1   1     2. 对于无向图来讲A->B,同时B->A也就是这个上下三角形都需要更改成1
 *-----------------------   3.对角线都是0，形成一个上下三角的镜像三角，数据存在冗余，看似冗余低效，但是计算机没法创建三角形数组，比较费劲
 *B   1   0   1   1   1     4.每一行都是表示当前行所代表的顶点，到其他所有顶点的可直接链接的路径,所以找某个顶点的邻接顶点，只需要通过定位
 *-----------------------     到顶点所在数组的坐标，就能定位到顶点所对应的二维数组的行坐标，然后这一行中所有的"0，1"标记就能知道谁是他的邻接
 *C   1   0   0   0   0       顶点
 *-----------------------
 *D   1   1   0   0   0
 *-----------------------
 *E   1   1   0   0   0
 *-----------------------
 *=========================图边表示(邻接表)============================================
 *
 * 顶点             包含邻接顶点的链表
 * -------------------------------
 * A                B->C->D
 * -------------------------------
 * B                A->D
 * -------------------------------
 * C                A
 * -------------------------------
 * D                E
 * -------------------------------
 * E               A->B->C->D
 * -------------------------------
 *
 *=========================深度优先遍历================================================
 *
 * 深度优先遍历,采用栈结构，遍历结果:
 * A B C D E
 *
 *=========================广度优先遍历================================================
 *
 * 广度优先遍历，采用队列,遍历结果:
 * A B D E C
 *
 *=========================无向权图辑结构===============================================
 * 每一条边都设置有权重值
 *
 *
 *                           10
 *                  B • • • • • • • • • • C
 *                • ••                  • •    •
 *              •  •   •  7          •            •   6
 *     6      •          •        •       •
 *          •     •         •   •                      •
 *        •                  •          5 •
 *      •      7 •          •   •                           •
 *    A              8   •       •        •                    F     凑合着看把，哈哈
 *   •          •      •            •                       •
 *      •           •                •    •             •
 *        •    •  •                    •            •
 *     4    •  •                          •     •    7
 *            D • • • • • • • • • • •  •  E  •
 *                   12
 *
 *
 *
 *=========================有权图链接矩阵表示===============================================
 *    A   B   C   D   E
 *-----------------------    1. 两个点之间存在边，则表示为权重值
 *A   M   5   M   6   12     2. 如果两点之间没有边，则设置一个非法的特别大的一个数值"M"，代表永远也到达不了
 *-----------------------
 *B   8   M   4   9   11
 *-----------------------
 *C   7   M   M   M   M
 *-----------------------
 *D   3   9   M   M   M
 *-----------------------
 *E   2   6   M   M   M
 *-----------------------
 *
 *
 *=========================权重图最小生成树===============================================
 *                           10
 *                  B • • • • • • • • • • C
 *                • ••                  • •    •
 *              •  •   •  7          •            •   6
 *     6      •          •        •       •
 *          •     •         •   •                      •
 *        •                  •          5 •
 *      •      7 •          •   •                           •
 *    A              8   •       •        •                    F     凑合着看把，哈哈
 *   •          •      •            •                       •
 *      •           •                •    •             •
 *        •    •  •                    •            •
 *     4    •  •                          •     •    7
 *            D • • • • • • • • • • •  •  E  •
 *                   12
 * 举例:A B C D E F 五个城市架设电线，求成本最低
 *求解:可以使用最小生成树来解决
 * 最小生成树:
 *     A
 *  D    B
 *     E
 *   C
 *     F
 * 的最小权重路径是:
 *  AD AB BE EC CF
 * 用最小生成树，解得：AD AB BE EC CF 为成本最低的架设方案
 * 总结：最小生成树就是权重图的最优解
 *=========================权重图最小路径===============================================
 **                           10
 *                  B • • • • • • • • • • C
 *                • ••                  • •    •
 *              •  •   •  7          •            •   6
 *     6      •          •        •       •
 *          •     •         •   •                      •
 *        •                  •          5 •
 *      •      7 •          •   •                           •
 *    A              8   •       •        •                    F     凑合着看把，哈哈
 *   •          •      •            •                       •
 *      •           •                •    •             •
 *        •    •  •                    •            •
 *     4    •  •                          •     •    7
 *            D • • • • • • • • • • •  •  E  •
 *                   12
 *
 * star:A 纵列矩阵的索引为A的索引0
 * S:AA(INF)                              U:AB(6),AC(INF),AD(4),AE(INF),AF(INF)   [AB,AC,AD,AE,AF]
 *
 * S:AA,AD(4)                             U:AB(6),DC(12),DE(16),AF(INF)           [DB,DC,DE,DF]
 *
 * S:AA,AD(4),AB(6)                       U:DC(12),BE(13),AF(INF)                 [BC,BE,BF]
 *
 * S:AA,AD(4),AB(6),DC(12)                U:BE(13),CF(18)                         [CE,CF]
 *
 * S:AA,AD(4),AB(6),DC(12),BE(13)         U:CF(18)                                [EF]
 *
 * S:AA,AD(4),AB(6),DC(12),BE(13),CF(18)                                          END
 *  <-----------------------------------
 * A-B最短 B->A
 * A-D最短 D->A      最短路径每个路径保存上一个最短顶点的索引，最终形成一个链，从结束节点向上查找就是整个最短路径
 * A-C最短 C->D->A
 * A-E最短 E->B-A
 * A-F最短 F->C->D->A
 *
 *
 * */
public interface Graph<K, V> {

    void add(Vertex<K, V> vertex);

    /**
     * 添加边
     * @param edge 传入边
     * */
    void addEdge(Edge<K> edge);

    /**
     * 深度优先搜索
     * @return List<Edge>
     * */
    List<Edge<K>> dfs(K from);

    /**
     * 广度优先搜索
     * @return List<Edge>
     * */
    List<Edge<K>> bfs(K from);

    /**
     * 最小生成树
     * @return List<Edge>
     * */
    List<Edge<K>> mTree(K from, boolean withWeight);

    /**
     * 最小路径
     * @return List<Edge>
     * */
    List<Edge<K>> mPath(K from, K to);

    /**
     * 返回图大小
     * @return int
     * */
    int size();
}

package com.yang.ds.datastruct.graph;

/**
 * 有向图接口
 * 增加拓扑排序功能，有向图需要实现此接口
 *
 *
 * 有向图，有向图，一定是有向无环图，否则就出现死循环了，永远到不了边界
 * =========================图逻辑结构===============================================
 *
 *       A
 *     ↙   ↘                  A->F
 *   ↙      ↘              ↗ → → → → → → → ↘
 *  B → → → → F          ↗  B->D      D->F  ↘
 *  ↓↘       ↗↑  sort  ↗   ↗ → → → ↘ ↗ → → → ↘     array      A -> B -> C -> D -> E -> F
 *  ↓ ↘     ↗ ↑       A → B  → C  → D  → E  → F
 *  C  ↘   ↗  E            ↘ → → → → → → → → ↗
 *   ↘  ↘ ↗  ↗                B->F
 *     ↘ D ↗
 *
 *
 * */
public interface DAG<K,V> extends Graph<K,V> {
    /**
     * 拓扑排序
     * */
    Vertex[] topologySort();
}

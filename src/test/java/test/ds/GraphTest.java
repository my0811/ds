package test.ds;

import com.yang.ds.datastruct.graph.DAG;
import com.yang.ds.datastruct.graph.Edge;
import com.yang.ds.datastruct.graph.Graph;
import com.yang.ds.datastruct.graph.Vertex;
import com.yang.ds.datastruct.graph.impl.DAGImpl;
import com.yang.ds.datastruct.graph.impl.GraphImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;


/**
 *图测试类
 * */

public class GraphTest {

    /**无向图*/
    Graph graph;

    /**有向图*/
    DAG dag;

    @Before
    public void init() {
        graph = new GraphImpl(3);
        dag = new DAGImpl();
    }

    /**
     * 深度优先搜索-测试
     *   ↙↗B↔ ↔ ↔C
     *  ↙↗
     * ↗↙
     * A ↔ ↔ ↔ D
     *  ↖↘
     *    ↖↘
     *      ↖↘E
     *
     * */
    @Test
    public void dfsTest() {
        // 添加顶点
        graph.add(new Vertex("A", "A"));
        graph.add(new Vertex("B", "B"));
        graph.add(new Vertex("C", "C"));
        graph.add(new Vertex("D", "D"));
        graph.add(new Vertex("E", "E"));
        // 添加边
        graph.addEdge(new Edge("A", "B"));
        graph.addEdge(new Edge("A", "D"));
        graph.addEdge(new Edge("A", "E"));
        graph.addEdge(new Edge("B", "C"));
        System.out.println(graph.dfs("A"));
    }

    /**
     * 广度优先搜索-测试
     *   ↙↗B↔ ↔ ↔C
     *  ↙↗
     * ↗↙
     * A ↔ ↔ ↔ D
     *  ↖↘
     *    ↖↘
     *      ↖↘E
     *
     * */
    @Test
    public void bfsTest() {
        // 添加顶点
        graph.add(new Vertex("A", "A"));
        graph.add(new Vertex("B", "B"));
        graph.add(new Vertex("C", "C"));
        graph.add(new Vertex("D", "D"));
        graph.add(new Vertex("E", "E"));
        // 添加边
        graph.addEdge(new Edge("A", "B"));
        graph.addEdge(new Edge("A", "D"));
        graph.addEdge(new Edge("A", "E"));
        graph.addEdge(new Edge("B", "C"));
        System.out.println(graph.bfs("A"));
    }

    /**
     * 无权图最小生成树
     *  ↙↗B↔ ↔ ↔C
     *  ↙↗
     * ↗↙
     * A ↔ ↔ ↔ D
     *  ↖↘
     *    ↖↘
     *      ↖↘E
     * */
    @Test
    public void mTeeWithoutWeightTest() {
        // 添加顶点
        graph.add(new Vertex("A", "A"));
        graph.add(new Vertex("B", "B"));
        graph.add(new Vertex("C", "C"));
        graph.add(new Vertex("D", "D"));
        graph.add(new Vertex("E", "E"));
        // 添加边
        graph.addEdge(new Edge("A", "B"));
        graph.addEdge(new Edge("A", "D"));
        graph.addEdge(new Edge("A", "E"));
        graph.addEdge(new Edge("B", "C"));
        System.out.println(graph.mTree("A", false));
    }

    /**
     * 带权图最小生成树
     * *                           10
     *                  B • • • • • • • • • • C
     *                • ••                  • •    •
     *              •  •   •  7          •            •   6
     *     6      •          •        •       •
     *          •     •         •   •                      •
     *        •                  •          5 •
     *      •      7 •          •   •                           •
     *    A              8   •       •        •                    F
     *   •          •      •            •                       •
     *      •           •                •    •             •
     *        •    •  •                    •            •
     *     4    •  •                          •     •    7
     *            D • • • • • • • • • • •  •  E  •
     *                   12
     *
     * */
    @Test
    public void mTreeWithWeightTest() {
        // 顶点 A B C D E F
        graph.add(new Vertex("A", "A"));
        graph.add(new Vertex("B", "B"));
        graph.add(new Vertex("C", "C"));
        graph.add(new Vertex("D", "D"));
        graph.add(new Vertex("E", "E"));
        graph.add(new Vertex("F", "F"));
        // 边
        graph.addEdge(new Edge("A", "B", 6));
        graph.addEdge(new Edge("A", "D", 4));
        graph.addEdge(new Edge("B", "C", 10));
        graph.addEdge(new Edge("B", "D", 7));
        graph.addEdge(new Edge("B", "E", 7));
        graph.addEdge(new Edge("C", "D", 8));
        graph.addEdge(new Edge("C", "E", 5));
        graph.addEdge(new Edge("C", "F", 6));
        graph.addEdge(new Edge("D", "E", 12));
        graph.addEdge(new Edge("E", "F", 7));
        System.out.println(graph.mTree("A", true));
    }

    /**
     * 最少边路径,最少边可能存在多条，默认把权重值设置为1，相同的算法按照顶点的优先插入顺序,选择了一条最短路径
     *                           10
     *                  B • • • • • • • • • • C
     *                • ••                  • •    •
     *              •  •   •  7          •            •   6
     *     6      •          •        •       •
     *          •     •         •   •                      •
     *        •                  •          5 •
     *      •      7 •          •   •                           •
     *    A              8   •       •        •                    F
     *   •          •      •            •                       •
     *      •           •                •    •             •
     *        •    •  •                    •            •
     *     4    •  •                          •     •    7
     *            D • • • • • • • • • • •  •  E  •
     * */
    @Test
    public void mEdgePathTest() {
        graph.add(new Vertex("A", "A"));
        graph.add(new Vertex("B", "B"));
        graph.add(new Vertex("C", "C"));
        graph.add(new Vertex("D", "D"));
        graph.add(new Vertex("E", "E"));
        graph.add(new Vertex("F", "F"));
        // 边
        graph.addEdge(new Edge("A", "B"));
        graph.addEdge(new Edge("A", "D"));
        graph.addEdge(new Edge("B", "C"));
        graph.addEdge(new Edge("B", "D"));
        graph.addEdge(new Edge("B", "E"));
        graph.addEdge(new Edge("C", "D"));
        graph.addEdge(new Edge("C", "E"));
        graph.addEdge(new Edge("C", "F"));
        graph.addEdge(new Edge("D", "E"));
        graph.addEdge(new Edge("E", "F"));
        System.out.println(graph.mPath("A", "F"));
    }

    /**
     * 最小权重路径
     *                          10
     *                  B • • • • • • • • • • C
     *                • ••                  • •    •
     *              •  •   •  7          •            •   6
     *     6      •          •        •       •
     *          •     •         •   •                      •
     *        •                  •          5 •
     *      •      7 •          •   •                           •
     *    A              8   •       •        •                    F
     *    •          •      •          •                       •
     *      •           •                •    •             •
     *        •    •  •                    •            •
     *     4    •  •                          •     •    7
     *            D • • • • • • • • • • •  •  E  •
     *
     * */
    @Test
    public void minWeighPathTest() {
        // 顶点 A B C D E F
        graph.add(new Vertex("A", "A"));
        graph.add(new Vertex("B", "B"));
        graph.add(new Vertex("C", "C"));
        graph.add(new Vertex("D", "D"));
        graph.add(new Vertex("E", "E"));
        graph.add(new Vertex("F", "F"));
        // 边
        graph.addEdge(new Edge("A", "B", 6));
        graph.addEdge(new Edge("A", "D", 4));
        graph.addEdge(new Edge("B", "C", 10));
        graph.addEdge(new Edge("B", "D", 7));
        graph.addEdge(new Edge("B", "E", 7));
        graph.addEdge(new Edge("C", "D", 8));
        graph.addEdge(new Edge("C", "E", 5));
        graph.addEdge(new Edge("C", "F", 6));
        graph.addEdge(new Edge("D", "E", 12));
        graph.addEdge(new Edge("E", "F", 7));
        System.out.println(graph.mPath("A", "A"));
    }

    /**
     * 有向图，拓扑排序
     *       A
     *     ↙   ↘                  A->F
     *   ↙      ↘              ↗ → → → → → → → ↘
     *  B → → → → F          ↗  B->D      D->F  ↘
     *  ↓↘       ↗↑  sort  ↗   ↗ → → → ↘ ↗ → → → ↘     array  A -> B -> C -> D -> E -> F
     *  ↓ ↘     ↗ ↑       A → B  → C  → D  → E  → F
     *  C  ↘   ↗  E            ↘ → → → → → → → → ↗
     *   ↘  ↘ ↗  ↗                B->F
     *     ↘ D ↗
     * */
    @Test
    public void topologySortTest() {
        // 顶点
        dag.add(new Vertex("A", "A"));
        dag.add(new Vertex("B", "B"));
        dag.add(new Vertex("C", "C"));
        dag.add(new Vertex("D", "D"));
        dag.add(new Vertex("E", "E"));
        dag.add(new Vertex("F", "F"));
        // 边
        dag.addEdge(new Edge("A", "B"));
        dag.addEdge(new Edge("A", "F"));
        dag.addEdge(new Edge("B", "F"));
        dag.addEdge(new Edge("B", "C"));
        dag.addEdge(new Edge("B", "D"));
        dag.addEdge(new Edge("C", "D"));
        dag.addEdge(new Edge("D", "F"));
        dag.addEdge(new Edge("D", "E"));
        dag.addEdge(new Edge("E", "F"));
        System.out.println(Arrays.toString(dag.topologySort()));
    }
}

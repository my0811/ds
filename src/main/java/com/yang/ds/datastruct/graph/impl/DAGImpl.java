package com.yang.ds.datastruct.graph.impl;

import com.yang.ds.datastruct.graph.DAG;
import com.yang.ds.datastruct.graph.Vertex;

public class DAGImpl<K, V> extends AbstractGraph<K, V> implements DAG<K, V> {
    /**
     * @see com.yang.ds.datastruct.graph.Graph
     *
     *1. 循环所有的顶点，每次获取最深的顶点（没有邻接顶点）倒叙的插入到数组中，
     *也就是从数组的尾巴开始插入，这样数组正常的顺序就是拓扑的顺序，否则，还需要把数组
     *倒叙，蛋疼的一匹
     *2. 每次获取到最深的顶点，则在图中把它干掉（干掉就是删除，如果数组实现的图，需要考虑数组删除的移动，jdk采用复制，
     *当然我的代码也采用复制），如果是邻接表实现的边，要考虑二维数组的row和col都删除，
     *并且删除需要循环的每一个row的数组的处理，和每个row的col的处理(需要记住扩容前老数组的长度，
     *涉及到邻接表默认值的问题)
     *3. 简单实现，这里有很多可以优化，比如获取无邻接边的顶点，可以记录每个节点的入度，入度为0的放入队列，只需要处理队列中入度为
     * 0的顶点即可，不需要搞两层循环来判断，更新邻接矩阵也无序删除，可以直接标记即可
     * */

    @Override
    public Vertex[] topologySort() {
        Vertex[] sortArray = new Vertex[size];
        CopyInner copyDAG = new CopyInner(adjMat, size);
        for (; copyDAG.size != 0; ) {
            // 获取没有邻接边的顶点
            int bounder = copyDAG.getWithoutAdjV();
            if (bounder == -1) {// 出现无环图的情况
                break;
            }
            // 倒叙放入数组，这样就不需要再对数组反转了
            sortArray[copyDAG.size - 1] = vertexes[bounder];
            // 删除此节点，不断的删除，会逐渐所有的顶点变成没有邻接边的顶点，直到所有顶点都放入数组当中
            copyDAG.delWithoutAdjV(bounder);
        }
        return sortArray;
    }

    /**
     * 邻接矩阵拷贝，因为拓扑排序，会不断删除邻接矩阵中的边,保护原有数据
     * */
    private class CopyInner {
        private int adjMat[][];
        private int size;

        public CopyInner(int[][] adjMat, int size) {
            this.adjMat = copyOf(adjMat, size);
            this.size = size;
        }

        public int[][] copyOf(int[][] oriAdjMat, int size) {
            int[][] copyAdjMat = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    copyAdjMat[i][j] = oriAdjMat[i][j];
                }
            }
            return copyAdjMat;
        }

        int getWithoutAdjV() {
            for (int i = 0; i < size; i++) {
                int j;
                for (j = 0; j < size; j++) {
                    if (adjMat[i][j] != INF) {
                        break;
                    }
                }
                // 每一行和所有列比较一遍，没有找到不是INF的边，说明没有邻接
                if (j == size) {
                    return i;
                }
            }
            // 出现了循环，不是无环图
            return -1;
        }

        /**
         * 可以直接对裂解矩阵标记即可，没比较复制底层数组造成开销，这里就是为了熟悉一下数组复制的规则
         * */
        void delWithoutAdjV(int bounder) {
            // 判断是否删除了最后一个，如果是删除最后一个，则不用管最后一个元素
            int moved = size - bounder - 1;
            if (moved > 0) {
                System.arraycopy(adjMat, bounder + 1, adjMat, bounder, moved);
            }
            // 如果移动完，或者是删除最后一个元素，则直接干掉，GC，干掉一行，每一个行都是一个数组对象
            adjMat[size - 1] = null;// GC

            int[] col;
            for (int i = 0; i < size - 1; i++) {
                col = adjMat[i];
                if (moved > 0) {
                    System.arraycopy(col, bounder + 1, col, bounder, moved);
                }
                // 干掉最后一个空余出来的位置的元素，或者本来删除的就是数组尾巴的元素
                col[size - 1] = 0;
            }
            // 二维数组干掉了行和列
            size--;
        }
    }
}

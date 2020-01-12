package com.yang.ds.algorithm.tree.btree.index;

public interface BDelIndexer {
    /**
     * 是否有左兄弟
     * */

    boolean hasLeftSib();

    /**
     * 是否有右兄弟
     * */
    boolean hasRightSib();


    /**
     * 下一个孩子左兄弟索引
     * */
    int leftSibIdx();

    /**
     * 下一个孩子右兄弟索引
     * */
    int rightSibIdx();

    /**
     * merge操作需要与分裂进行逆操作，分裂上提的元素需要下移
     *
     * */
    int splitUpKeyIdx();

    /**
     * merge index 获取
     * 1.merge索引,如果有左侧孩子则以左侧孩子为准，从右向左合并，与分裂的方向相反(分裂是从左向右)
     * 2.没有左兄弟，可以用自己充当左兄弟，从右向左合并,所以不可能不存在右兄弟
     * */
    int mergeLeftIdx();

    int mergeRightIdx();

    /**
     * 旋转，上层父节点key顶点的索引位置
     * */
    int leftRotateUpKeyIdx();

    int rightRotateUpKeyIdx();
}

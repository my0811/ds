package com.yang.ds.algorithm.tree.tree23;

/**
 * 234树中的node节点
 * */
public class Tree23Node {

    public static final int NOT_EXIT = -1;
    // 234树最大孩子个数
    public static final int MAX_CHILD_COUNT = 4;
    //保存节点中含有的数据项个数

    public int itemCount;
    // 当前节点的父节点

    private Tree23Node parentNode;

    // 保存子节点的数组
    private Tree23Node[] childNodeArray = new Tree23Node[MAX_CHILD_COUNT];

    // 节点中放入的数据项，数据项数+1=子节点树
    private DataItem[] dataItemArray = new DataItem[MAX_CHILD_COUNT - 1];

    /**
     * 链接子节点
     * */
    public void connectChild(int childIndex, Tree23Node childNode) {
        childNodeArray[childIndex] = childNode;
        if (childNode != null) {
            childNode.setParentNode(this);
        }
    }

    /**
     *
     * 断开子节点，返回子节点
     * */

    public Tree23Node disConnectChild(int childIndex) {
        Tree23Node childNode = childNodeArray[childIndex];
        childNodeArray[childIndex] = null;
        return childNode;
    }

    /**
     *
     * 拿到当前节点的某个指定的子节点
     * */
    public Tree23Node childOf(int childIndex) {
        return childNodeArray[childIndex];
    }


    /**
     * 获取数据项数组
     * */
    public DataItem[] getDataItemArray() {
        return dataItemArray;
    }


    /**
     * 判断当前节点是否是叶子节点
     * */
    public boolean isLeaf() {
        return (childNodeArray[0] == null ? true : false);
    }

    /**
     * 拿到当前节点中包含的数据个数
     * */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * 判断当前节点是不是满节点
     * */
    public boolean isFull() {
        return (itemCount == (MAX_CHILD_COUNT - 1) ? true : false);
    }

    /**
     * 给出数据项，返回次数据项在节点中的位置
     *
     * */
    public int findItemIndex(int data) {
        for (int i = 0; i < itemCount; i++) {
            if (dataItemArray[i].getData() == data) {
                return i;
            }
        }
        return NOT_EXIT;
    }

    /**
     *实现新的数据插入此节点中的对应位置，有序数组，所以需要确定位置,移动位置
     * */
    public int insertItem(DataItem nDataItem) {
        itemCount++;
        int data = nDataItem.data;
        // -1是数据项个数，再-1是数据项数组的最大索引
        for (int i = (MAX_CHILD_COUNT - 2); i >= 0; i--) {
            // 空值跳过处理,很可能是数组的最后一个元素，还没有插入，此时是空的，但是绝对不会超过数组长度，外层有判断，满节点不能再插入
            if (dataItemArray[i] == null) {
                continue;
            } else {
                // 模拟java数组的底层数组复制的算法，所有数据向后移动,最后空出来的位置为插入位置
                if (data < dataItemArray[i].data) {
                    dataItemArray[i + 1] = dataItemArray[i];
                } else {
                    dataItemArray[i + 1] = nDataItem;
                    return i + 1;
                }
            }
        }
        // 循环完成之后，没有插入，证明这个数就是最小的，或者数组就是空，数组如果是空的，这个数也肯定是最小的
        dataItemArray[0] = nDataItem;
        return 0;
    }

    /**
     *
     * 实现移除当前节点的最后的一个数据项
     *
     */
    public DataItem removeLasItem() {
        if (itemCount == 0) {
            return null;
        }
        DataItem delDataItem = dataItemArray[itemCount - 1];
        dataItemArray[itemCount - 1] = null;
        itemCount--;
        return delDataItem;
    }

    /**
     * 打印当前节点中的所有数据项打印
     * */
    public void dispaly() {
        for (int i = 0; i < itemCount; i++) {
            dataItemArray[i].display();
        }
        System.out.println("/");
    }

    /**
     * 父节点引用
     * */

    public Tree23Node getParentNode() {
        return parentNode;
    }

    /**
     * 设置父节点引用
     * */
    public void setParentNode(Tree23Node parentNode) {
        this.parentNode = parentNode;
    }
}

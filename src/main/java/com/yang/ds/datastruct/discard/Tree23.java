package com.yang.ds.datastruct.discard;

/**
 * 23树
 * */
@Deprecated
public class Tree23 {
    private Tree23Node root;

    public Tree23() {
        this.root = new Tree23Node();
    }

    /**
     * 搜索,如果找到返回数据所在节点的数组中的索引值
     * */
    public int find(int data) {
        Tree23Node currentNode = root;
        int childIndex;
        while (true) {
            // 找到了data对应的数据项
            if ((childIndex = currentNode.findItemIndex(data)) != Tree23Node.NOT_EXIT) {
                return childIndex;
            }
            if (currentNode.isLeaf()) {// 找到了叶子节点都没有找到，查找不存在
                return Tree23Node.NOT_EXIT;
            }
            // 继续查找子节点，根据二叉树大小顺序规则，指针移动到指定的节点
            currentNode = nextNode(currentNode, data);
        }
    }

    /**
     * 获取下一个需要查找的子节点(节点内部使用，只能非叶子节点使用)
     * @param currentNode 必定不为空，并且不是叶子节点
     * @param data 数据节点的值，通过和次值比较大小，确定下一个节点的指向
     * */
    private Tree23Node nextNode(Tree23Node currentNode, int data) {
        // 内部使用，只能非叶子节点使用
        notNull(currentNode);
        notLeaf(currentNode);
        // 获取数据项个数
        int itemCount = currentNode.getItemCount();
        // 索引
        int childIndex;
        DataItem[] dataItemArray = currentNode.getDataItemArray();
        // 1.注意孩子的个数是数据项的个数加1，注意循环结束后的childIndex++最后一次自加
        // 2.注意这里面的索引也是和数据项相对应的，总的索引值比数据项多1个
        // 3.孩子肯定存在一个比数据项数组最左面还要小的，也存在一个比数据项数组最右面还要大的，剩下的大小肯定都在数据项中间（分裂形成的）
        // 4.夹缝中求生存，先判断数据是否比数组最左面的小，如果比第0个元素还小，就是最左孩子且孩子的索引也是0，其他的就是中间的，循环结束的最后一次自增
        // 刚好就是数据项中的最右面孩子的索引
        // 5. data < dataItemArray[childIndex].getData()这个逻辑就既能取到最左面孩子，也能去到中间的孩子，因为只要大于就向右移动，那么肯定比前一个大
        // 如果正好比循环到的索引小，就是中间，结束循环就是最右面的孩子
        // ps:一定牢记数据项和孩子的索引关系，除了孩子多了一个索引，其他顺序都是一样的，并且，第一个在1的左面，第二个在1.2中间，第三个在2.3中间，第四个在
        // 3.的右面
        for (childIndex = 0; childIndex < itemCount; childIndex++) {// 循环结束最后一次还是会自增的
            if (data < dataItemArray[childIndex].getData()) {
                return currentNode.childOf(childIndex);// 每次循环之后，都是比前一个大，然后再比较后一个（这就是中间夹缝）
            }
        }
        // 循环完都没有，证明孩子在最大数据项的右面
        return currentNode.childOf(childIndex);
    }

    /**
     * 插入
     * */
    public void insert(int data) {
        Tree23Node currentNode = root;
        DataItem nDataItem = new DataItem(data);
        // 遍历树
        while (true) {
            // 插入路径上不是满节点
            if (currentNode.isFull()) {// 最复杂就是这个case 节点分裂
                // 最核心方法，树节点数据项满了进行分裂,中间节点向上提升
                split(currentNode);
                // 分裂完成更改为父节点，递归继续循环处理(因为父节点如果满了，还需要递归继续分裂了，继续下一次循环)
                currentNode = currentNode.getParentNode();
                // 相当于从父节点开始递归执行插入逻辑，找到叶子节点的插入位置
                currentNode = nextNode(currentNode, data);
            } else if (currentNode.isLeaf()) {// 叶子节点进行数据项插入
                // 234树的插入，就是叶子节点，就是我们插入的位置,结束循环
                break;
            } else {// 不是满节点，也不是叶子节点，下一个子节点上移动
                currentNode = nextNode(currentNode, data);
            }
        }
        // 跳出循环，肯定是叶子节点，所以就是插入的位置
        currentNode.insertItem(nDataItem);
    }

    /**
     * 1 2 3
     * A B C
     *
     * 核心方法，节点分裂，也是23树的核心处理方法，一定不要有bug
     * */
    private void split(Tree23Node currentNode) {
        // 数据项B,中间数据项
        DataItem itemB;
        // 数据项C,最大的数据项，也是数组最右面的数据项
        DataItem itemC;
        // 当前节点第三个子节点
        Tree23Node child2;
        // 当前节点第四个子节点
        Tree23Node child3;
        // 当前节点的父节点
        Tree23Node parent;
        // 移除主要是从右到左移除最后一个数据项
        itemC = currentNode.removeLasItem();
        itemB = currentNode.removeLasItem();
        // 断开第三个孩子和第四个孩子的链接T
        // TODO 不要写死，如果是多个节点，用循环来搞,B-树的时候再升级
        child2 = currentNode.disConnectChild(2);
        child3 = currentNode.disConnectChild(3);
        Tree23Node rightNewNode = new Tree23Node();
        if (currentNode == root) {// 根分裂
            root = new Tree23Node();
            parent = root;
            // 新创建出来的新的根节点，肯定是一个空的数组，所以放到第一个位置上就可以了
            root.connectChild(0, currentNode);
        } else {// 普通分裂
            parent = currentNode.getParentNode();
        }
        // 分裂主要都是主要把满节点的中间B数据项放到父节点上，这样才能把3个的满节点拆开一分为2
        int indexOfParent = parent.insertItem(itemB);// 父节点数据项+1

        // 父节点数据项个数，插入了B数据向之后
        int itemCount = parent.getItemCount();
        // 1.这里面父节点中的子节点的位置需要变动，因为插入了新数据项，原来数组是有序的，所以会出现新数据项把原来数据项挤到后面
        // 2.当然了如果插入数据项的位置就是父节点的最后一个，那么进入不了循环
        // 3.数据项的位置对应的孩子的位置，一一对应的
        // 4.第一个孩子比第一个数据小，第二个孩子在数据项1和2之间，第三个孩子在数据项2和3之间，第四个孩子比数据项3大
        // 5.4中的情况并不是我们插入时候搞出来的，而是分裂搞出来的
        // 6.为什么说1个数据节点如果有俩孩子，一个比数据项大，一个比数据项小？两个节点三个孩子，两个在两边，一个在中间？我们要
        // 换成二叉树的后继节点的思想去裂解，因为第一次插入三个数据项经过分裂取得是中间的向上提生父节点,变成两个 TODO
        Tree23Node tmp;
        for (int i = itemCount - 1; i > indexOfParent; i--) {// 这里其实就是集合中的指定index添加效果一样（集合底层数组复制，和下面循环玩法一个效果）
            // 新插入的数据项B把之前数据挤走了，需要重新更改索引位置
            tmp = parent.disConnectChild(i);
            parent.connectChild(i + 1, tmp);
        }
        // 拆出来的第三个数据项C插入到新分裂出来的节点
        rightNewNode.insertItem(itemC);
        // 链接到父节点
        parent.connectChild(indexOfParent + 1, rightNewNode);
        rightNewNode.connectChild(0, child2);
        rightNewNode.connectChild(1, child3);
    }

    public void display() {
        display(root, 0, 0);
    }

    public void display(Tree23Node currentNode, int level, int childIndex) {
        System.out.println("level:" + level + " " + "childIndex:" + childIndex);
        currentNode.dispaly();
        int itemtNum = currentNode.getItemCount();
        for (int i = 0; i < (itemtNum + 1); i++) {
            Tree23Node childNode = currentNode.childOf(i);
            // 递归边界多条件
            if (childNode == null) {
                return;
            } else {
                display(childNode, level + 1, i);
            }
        }
    }

    private void notNull(Tree23Node currentNode) {
        if (null == currentNode) {
            throw new IllegalArgumentException("currentNode can not be null");
        }
    }

    private void notLeaf(Tree23Node currentNode) {
        if (currentNode.isLeaf()) {
            throw new IllegalArgumentException("currentNode can not be leaf");
        }
    }

    public static void main(String[] args) {
        Tree23 tree234 = new Tree23();
        tree234.insert(20);
        tree234.insert(22);
        tree234.insert(15);
        tree234.insert(98);
        tree234.insert(45);
        tree234.insert(6);
        tree234.display();
        System.out.println(Math.ceil(1.5));
    }
}

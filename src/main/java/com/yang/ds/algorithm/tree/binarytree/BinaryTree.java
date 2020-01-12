package com.yang.ds.algorithm.tree.binarytree;

/**
 * 练习-二叉树
 * 1.二叉树增加、查找、删除，复杂度均为O(logN)
 * 2.二叉树的遍历应用递归，效率较低，二叉树一般用于查找和存储，应用于遍历的比较少
 */

public class BinaryTree {

    private BinaryNode root;

    public BinaryTree() {
        this.root = null;
    }

    /**
     * 增加
     */

    public boolean insert(int data) {
        // 空树的情况直接插入到根节点
        if (isEmpty()) {
            root = new BinaryNode(data);
            return true;
        }
        // 父节点
        BinaryNode parentNode = null;
        // 节点的的当前指针，根据查找不断的移动,默认为root节点作为开始
        BinaryNode currentNode = root;
        boolean isLeft = false;
        // 不断的遍历，一直插入到具体的一个子节点的尾部，因为二叉树不会出现插入在中间然后再更改引用关系的情况,每次插入都是判断完大小，决定插入在最小的或者最大的子节点后面
        for (; currentNode != null; ) {
            parentNode = currentNode;
            if (data < currentNode.getData()) {// 需要插入的数据节点在左子树
                currentNode = currentNode.getLeftChildNode();
                isLeft = true;
            } else { // 需要插入的节点在右子树
                currentNode = currentNode.getRightChildNode();
                isLeft = false;
            }
        }
        // 异常情况,循环跳出之后currentNode指针指向的应该是插入节点的下一个位置，应该是null，因为不存在
        if (currentNode != null) {
            throw new IllegalArgumentException("insert error!");
        }

        // 执行插入 (最后一个节点就是parentNode,插入左面或者右面)
        if (isLeft) {
            parentNode.setLeftChildNode(new BinaryNode(data));
        } else {
            parentNode.setRightChildNode(new BinaryNode(data));
        }
        return false;
    }

    /**
     * 查找
     */
    public BinaryNode find(int data) {
        // 判断树是否为空
        if (isEmpty()) {
            return null;
        }
        // 从根据指定节点开始查找,一般情况下是从根开始查找
        BinaryNode currentNode = root;
        // 二叉树比较,logN次就会比较出数据是否存在
        for (; currentNode != null; ) {
            // 目标数据在左子树,向左子树移动引用
            if (data < currentNode.getData()) {
                currentNode = currentNode.getLeftChildNode();
            }
            // 目标数据在右子树，向右子树移动引用
            if (data > currentNode.getData()) {
                currentNode = currentNode.getRightChildNode();
            }
            // 找到数据节点
            if (data == currentNode.getData()) {
                return currentNode;
            }
        }
        // 没有找到节点，返回null
        return null;
    }


    /**
     * 最大值
     */
    public BinaryNode findMax() {
        if (isEmpty()) {
            return null;
        }
        BinaryNode currentNode = root;
        BinaryNode maxNode = root;
        for (; currentNode != null; ) {
            maxNode = currentNode;
            currentNode = currentNode.getRightChildNode();
        }
        return maxNode;
    }

    /**
     * 最小值
     */
    public BinaryNode findMin() {
        if (isEmpty()) {
            return null;
        }
        BinaryNode currentNode = root;
        BinaryNode minNode = root;
        for (; currentNode != null; ) {
            minNode = currentNode;
            currentNode = currentNode.getLeftChildNode();
        }
        return minNode;
    }

    /**
     * ---------删除--------------
     * I.找到节点
     * II.删除节点
     * ---------------------------
     * 删除节点最为麻烦，因为会有很多情况
     * 情况1 ----☆
     * 删除节点为叶子节点:直接干掉即可
     * 情况2 ----☆
     * 删除节点只有一个子树,左子树,或者右子树,直接干掉，把干掉的这个节点的父节点和自己的第一个子节点连接即可
     * 情况3 ----☆☆☆☆☆ 情况三比较繁琐，麻烦的一匹，需要多点篇幅描述
     * 这个就稍微有点恶心，删除节点同时拥有左子树和右子树,具体说明如下:
     * 3.1 二叉树的整体结构不能破坏，二叉树从root节点开始就左面部分的全都小于root，右半部分都大于root，至于root下面的各种子树以此类推
     * 也都遵循这样的原则
     * 3.2 如3.1所述，所以这样的的节点删除为了保持原来的关系，我们需要对节点的链接进行如下两大步骤调整:
     * 1. 需要删除节点的位置链接需要再选举出来一个节点代替，就是找个社会主义接班人,选取的规则就是要比左子树的最大值大
     * 同时要比右子树的所有值都小,那应该如何选择节点呢？当然就是删除节点的右子树的最小值的节点,最小值就不解释了，可以参考上面的
     * 最小值，最大值的方法实现,只不过这里的最小值不是整棵树的而是删除的节点的右子树中的最小值
     * 2.选举出来的节点N代替要删除的节点D，会出现如下情况
     * 1).N的父节点是D的右子节点,这个时候不需要处理
     * 2).N的父节点不是D的右子节点,需要把N的右子节点变更为N父节点的左子节点(现在N的右子节点的父节点是N)
     * 3).如果N没有右子节点逻辑与2)相同，只不过相当于N的父节点的左子节点置换为null,因为反正N都选举出来移动走，去充当被删除的节点位置，上调
     * 4).把N的右子节点设置为删除节点的右子节点
     * 5).把N的左子节点设置为删除节点左子节点
     * 结束----------------------------搞定收工,撸代码
     */

    public boolean delete(int data) {
        // 删除节点为根节点,那可以直接把根设置为null，整棵树就在jvm角度看就没有到达gc中root节点的引用了，会把所有对象gc掉,从而整棵树也就不存在了
        // 找到需要删除的节点,默认从根节点开始
        BinaryNode needDelNode = root;
        // 需要删除的节点的父节点
        BinaryNode needDelParentNode = root;
        // 需要删除的节点在父节点的左边还是右面
        boolean isNeedDelParentLeft = false;
        // 需要删除
        for (; needDelNode.getData() != data && needDelNode != null; ) {
            needDelParentNode = needDelNode;
            if (data < needDelNode.getData()) {// 需要删除的节点在左子树
                isNeedDelParentLeft = true;
                needDelNode = needDelNode.getLeftChildNode();
            } else {
                isNeedDelParentLeft = false;
                needDelNode = needDelNode.getRightChildNode();
            }
        }
        // 没有找到需要删除的数据
        if (needDelNode == null) {
            return false;
        }
        // 情况1:目标删除节点没有子树，是叶子节点
        if (noChild(needDelNode)) {
            // 判断是否删除的为根节点,如果是根节点且没有子树，直接干掉根节点
            if (needDelNode == root) {
                root = null;
            }
            // 直接删除掉，目标删除节点到和父节点之间的引用，需要区分是要删除掉父节点的左还是右
            appendChild(needDelParentNode, isNeedDelParentLeft, null);
            return true;
        }
        // 情况2,目标删除节点只有一个子树,左子树,或者右子树,直接父节点的引用指向目标删除节点的子节点即可
        if (hasOneChild(needDelNode)) {
            // 删除数据为root节点的情况判断
            if (needDelNode.getLeftChildNode() != null && needDelNode == root) {
                root = needDelNode.getLeftChildNode();
                return true;
            }
            if (needDelNode.getRightChildNode() != null && needDelNode == root) {
                root = needDelNode.getRightChildNode();
                return true;
            }

            // 只有左子树
            if (needDelNode.getLeftChildNode() != null) {
                appendChild(needDelParentNode, isNeedDelParentLeft, needDelNode.getLeftChildNode());
            }

            // 只有右子树
            if (needDelNode.getRightChildNode() != null) {
                appendChild(needDelParentNode, isNeedDelParentLeft, needDelNode.getRightChildNode());
            }
            return true;
        }
        // 情况3 目标删除节点的同时拥有左子树和右子树
        if (hasTwoChild(needDelNode)) {

            // 选举出新的节点,替换需要删除的节点
            BinaryNode electMinNode = electReplaceNode4DelNode(needDelNode);


            // 处理选举出来的节点左半部分的链接(右半部分的链接在electReplaceNode4DelNode中已处理)
            electMinNode.setLeftChildNode(needDelNode.getLeftChildNode());


            // 根节点判断
            if (needDelNode == root) {
                root = electMinNode;
            } else {
                // 删除节点的父节点和新选举出的节点进行链接,建议这步放到后面，因为这个一步才是真正干掉目标删除节点的引用,才可以被安全gc掉
                // 虽然needDelNode这个引用在方法没有结束的时候，gc不会干掉needDelNode这个节点的对象
                appendChild(needDelParentNode, isNeedDelParentLeft, electMinNode);
            }
            return true;
        }
        return false;
    }

    /**
     * 对于同时拥有左右子树的节点删除，选举出一个顶替被删除节点位置的节点
     */

    private BinaryNode electReplaceNode4DelNode(BinaryNode needDelNode) {

        // 最小值节点的父节点
        BinaryNode minParentNode = needDelNode;

        // 最小值节点,**这个就是选举出来需要替换需要删除节点位置的节点**
        BinaryNode minNode = needDelNode;

        // 当前遍历节点
        BinaryNode currentNode = needDelNode.getRightChildNode();


        for (; currentNode != null; ) {
            minParentNode = minNode;
            minNode = currentNode;
            currentNode = currentNode.getLeftChildNode();
        }
        /**
         * 如果最小的节点就是删除节点的右子节点,那么直接返回此节点即可，因为删除的节点右面所有子树都在一个方向如下面情况
         * 这种情况，不需要做过多的处理，当时的这个最小节点直接代替删除节点即可,不会涉及到下面的step1,和step2，因为删除节点的右
         * 子树顺序已经是没问题了，所有的数都比删除节点大，且删除节点的右子节点是目前右子树中最小的,满足选举右子树最小节点的条件
         *    30--------------(del),这个30就是替换删除的节点，不需要多余的任何操作了，已经满足条件了,
         *      40
         *         50
         *           60
         *
         * */
        if (minNode == needDelNode.getRightChildNode()) {
            return minNode;
        }

        // step1，最小节点父节点---->左子节点--->最小节点右子节点
        minParentNode.setLeftChildNode(minNode.getRightChildNode());
        // step2,最小节点-->右子节点--->删除节点右子节点
        minNode.setRightChildNode(needDelNode.getRightChildNode());
        return minNode;
    }


    /**
     * 中序遍历,左、中、右
     */
    public void midErgodic(BinaryNode node) {
        // 边界条件
        if (node == null) {
            return;
        }
        midErgodic(node.getLeftChildNode());
        System.out.print(node.getData() + " ");
        midErgodic(node.getRightChildNode());
    }

    /**
     * 前序遍历 根、左、右
     */

    public void preErgodic(BinaryNode node) {
        // 边界条件
        if (node == null) {
            return;
        }
        System.out.print(node.getData() + " ");
        preErgodic(node.getLeftChildNode());
        preErgodic(node.getRightChildNode());
    }

    /**
     * 后序遍历 左、根、右
     */
    public void afterErgodic(BinaryNode node) {
        // 边界条件
        if (node == null) {
            return;
        }
        afterErgodic(node.getLeftChildNode());
        afterErgodic(node.getRightChildNode());
        System.out.print(node.getData() + " ");
    }


    private boolean isEmpty() {
        return root == null;
    }

    private boolean noChild(BinaryNode currentNode) {
        if (currentNode == null)
            throw new IllegalArgumentException("error node!");
        return (currentNode.getLeftChildNode() == null) && (currentNode.getRightChildNode() == null);
    }

    private boolean hasOneChild(BinaryNode currentNode) {
        if (currentNode.getLeftChildNode() != null && currentNode.getRightChildNode() == null) {
            return true;
        }
        if (currentNode.getLeftChildNode() == null && currentNode.getRightChildNode() != null) {
            return true;
        }
        return false;
    }

    private boolean hasTwoChild(BinaryNode currentNode) {
        return (currentNode.getLeftChildNode() != null) && (currentNode.getRightChildNode() != null);
    }

    private void appendChild(BinaryNode currentNode, boolean isLeft, BinaryNode nNode) {
        if (isLeft) {
            currentNode.setLeftChildNode(nNode);
        } else {
            currentNode.setRightChildNode(nNode);
        }
    }

    public BinaryNode getRoot() {
        return root;
    }

    /**
     * 主函数测试
     */
    public static void main(String[] args) {
        BinaryTree bt = new BinaryTree();
        bt.insert(40);
        bt.insert(36);
        bt.insert(37);
        bt.insert(85);
        bt.insert(49);
        bt.insert(88);
        System.out.println("max: " + bt.findMax().getData());
        System.out.println("min: " + bt.findMin().getData());
        bt.midErgodic(bt.getRoot());
        System.out.println();
        bt.delete(40);
        bt.midErgodic(bt.getRoot());
    }
}
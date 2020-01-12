package com.yang.ds.algorithm.tree.rbtree;

/**
 *        -红黑树-
 * 1. 每个节点不是红色就是黑色
 * 2. 根节点总是黑色的
 * 3. 如果节点是红色的，则他的子节点必定是黑色的(反之不一定,也就是说可以黑->黑)
 * 4. 所有的叶子节点都是黑色(null)(null节点也算是黑色节点,所以一个节点只有一个子节点，那这个数的兄弟节点为空视为黑色)
 * 5. 从根节点到叶子节点或者空子节点的每条路径,必须包含相同数目的黑色节点,(即相同的黑色高度)
 * -- 红黑数修正
 *  1. 改变节点颜色
 *  2. 左旋，右旋
 * -- 红黑树节点插入修正 (三种大情况,I,II,III)
 * **插入节点默认是红色的**
 * ----------------------------------------------------------------------
 * I. 如果是第一次插入，由于原来数是空树，所以只会违反红黑树规则2,所以直接把根节点涂黑即可
 * -----------------------------------------------------------------------
 * II. 如果插入节点的父节点是黑色的，不会违背红黑树规则，什么都不需要做，正常插入即可
 * -----------------------------------------------------------------------
 * III. 第三中情况就是最恶心的情况，需要进行变色和旋转,主要由以下三种情况
 * ps:讨论中:"N"(now)表示当前节点,"P"(parent)当前节点的父节点,"U"(uncle)表示当前节点的叔叔节点,"G"(grandfather)表示当前节点祖父节点
 *
 * 情况1. [插入节点]的[父节点]和其[叔叔节点](祖父节点的另一个子节点)均为[红色]
 *  1).[当前节点N]指向[新插入的节点]
 *  2). P,U涂黑,把G涂红
 *  3). 把当前节点N指向更改成G(最开始N指向新插入节点,现在指向新插入节点的祖父节点)
 *  4). 此时变成了下面的情况2
 *
 * 情况2. [插入节点]的[父节点]是[红色]的，[叔叔节点]是[黑色]的，且[插入节点]是其[父节点]的[右子节点]
 *  1).以N的[父节点]作为[定点]进行[左旋]
 *  2).把当前节点N的指向更改为N的父节点
 *  3).此时情况变成了情况3
 *
 * 情况3. [插入节点]的[父节点]是[红色]的，[叔叔节点]是[黑色]的，且[插入节点]是其[父节点]的[左子节点]
 *  1).把P涂黑,把G涂红
 *  2).以G为[顶点]右旋
 *  3).完成红黑树的平衡修复
 * 最终:
 *  root节点涂黑
 *  ---------------------------
 *  主要步骤 :   变色-->左旋-->变色-->右旋
 *  N的变化 :   新插入节点-->G-->P
 *  这个三种情况的变化是呈先后的因果关系的,当时并不是都会发生
 *
 * 深入理解红黑树算法复杂度:
 *  一:红黑树的四条规则限制了红黑树的平衡,经过上面的一些情况的变动，红黑树最终肯定会趋向平衡,不断的变更不断的向根节点靠近调整
 *  最终如果二叉树的一面深度过高，最终都会通过上面的变动递归处理到根节点，从而又让让整棵树的从根节点的角度来看是趋于平衡的二叉树
 *  二：如果一个二叉树是平衡的二叉树，那么他操作增加、删除、查找都是O(logN)的复杂度,但是不平衡极端可能就会出现单向链表的，极度不平衡，那最坏的情况
 *  就是O(N)的复杂度，所以考虑到最坏的情况下，普通的二叉树只能是复杂度在O(logN)到O(N)之间,但是红黑树，虽然代码搞了一大堆，插入时候各种移动，但是这些移动也终归是一个常数级别的调整
 *  所以红黑树的增加、删除、查找的操作都是O(logN)加上一个常数级别的操作，复杂度大体上还是O(logN),性能良好
 * 深入理解树的旋转:
 * 1.左旋是针对于右子树，也就是左旋影响了右子树的变化,
 * 2.右旋是针对于左子树，也就是右旋影响了左子树的变化
 * 所以说左子树没有只有右子树，那只能左旋,只有左子树那只能右旋
 * ps :好好想一下红黑树的插入，还有二叉树的插入，只能在叶子节点进行插入，不会出现在中间突然插入一个数据，爆炸式的入侵
 * 所以最后插入只能要不只有左子树，要么只有右子树,然后以父节点为顶点左旋右旋,所以要看插入的是左面还是右面(也就是出现情况2和情况3的原因)
 * 不在祖父节点的同一面，以父节点为顶点，旋转成相对于祖父节点和父亲节点一个方向，就是一条线(脑补)
 *
 * 深入理解变色、左旋、右旋:
 *  情况1.总是第一步处理,红黑树的五条规则，插入节点违反了规则，我们首先想从最简单的调整入手，更改颜色,比如情况1,但是调整为情况1就会出现(情况2[或者]情况3[或者]情况1)
 *    经过情况1的调整之后相当于向树的根节点进行靠近了，因为当前节点指向改为祖父节点了,所以还是会出现情况1，情况2，情况3的case，以此类推，第一步还是处理情况1的case
 *  情况2.对于情况2的情况，我们可以这样理解，我们需要把当前节点和父节点调整到祖父节点的同一个方向，然后为情况3左准备，所以情况2完成必定要经理情况3
 *
 *  情况3.对于情况2的处里，已经完成了父亲节点和当前节点在祖父节点的统一方向,之后就是旋转了
 *  所以说，三种情况可能会经历其中情况1变色，或者经过情况3的旋转完成红黑树的平衡,但是如果经历了情况2必定要经历情况3
 *
 *
 *
 */
public class RedBlackTree {


    private RedBlackNode root;

    public RedBlackTree() {
        this.root = null;
    }

    /**
     * 左旋 以x为顶点
     *   x              y
     * a  y    ---->  x   b
     *   r b         a r
     *
     * 主要是三大步骤处理
     * step1 step2 step3
     */

    private void leftRotate(RedBlackNode x) {
        // step1.把y的节点的左子节点变成x的右子节点
        RedBlackNode y = x.getRightChildNode();
        x.setRightChildNode(y.getLeftChildNode());
        if (y.getLeftChildNode() != null) {
            y.getLeftChildNode().setParentNode(x);
        }
        // step2.把x的父节点变成为y的父节点(相当于y要充当与x的角色),有点绕(x的老爸去给y当老爸，不给x当老爸了)
        y.setParentNode(x.getParentNode());
        if (x.getParentNode() == null) {// x没有父节点，x为root节点,根
            root = y;
        } else {
            if (x == x.getParentNode().getLeftChildNode()) { // x为x父节点的左子节点
                x.getParentNode().setLeftChildNode(y);// 真正实现换儿子交换变更,x的父节点变成了y的父节点
            } else {// x为x父节点的右子节点
                x.getParentNode().setRightChildNode(y);
            }
        }
        // step3. y的左节点设置为x,x的父节点设置为y
        x.setParentNode(y);
        y.setLeftChildNode(x);
    }

    /**
     * 右旋
     *    y              x
     *  x  a     --->  r  y
     * r b               b a
     * */
    private void rightRotate(RedBlackNode y) {

        // step1 把x的右子节点变成y的左子节点
        RedBlackNode x = y.getLeftChildNode();
        y.setLeftChildNode(x.getRightChildNode());
        if (x.getRightChildNode() != null) {
            x.getRightChildNode().setParentNode(y);
        }

        // step2.把y父节点变成x的父节点(y的爸爸变成x的爸爸)
        if (y.getParentNode() == null) {// y是root节点
            root = x;
        } else {
            if (y == y.getParentNode().getLeftChildNode()) {// y为y父节点的左子节点
                y.getParentNode().setLeftChildNode(x);
            } else {// y为父节点的右子节点
                y.getParentNode().setRightChildNode(x);
            }
        }
        // 把x父节点更换成y的父节点
        x.setParentNode(y.getParentNode());

        // step3.把y的父节点变成x，x的右子节点变成y
        y.setParentNode(x);
        x.setRightChildNode(y);
    }

    /**
     * 插入 insert
     * */
    public void insert(int data) {
        RedBlackNode nNode = new RedBlackNode(RedBlackNode.NodeColor.RED, data, null, null, null);

        // 判断空树,红黑树准则2：根节点为黑色
        if (isEmpty()) {
            root = nNode;
            root.setColor(RedBlackNode.NodeColor.BLACK);
            return;
        }

        // 找到插入的位置
        RedBlackNode currentNode = root;
        RedBlackNode parentNode = null;
        boolean isLeft = false;
        for (; currentNode != null; ) {
            parentNode = currentNode;
            // 小于，向左子树移动
            if (data < currentNode.getData()) {
                currentNode = currentNode.getLeftChildNode();
                isLeft = true;
            } else {// 大于等于,向右子树移动
                currentNode = currentNode.getRightChildNode();
                isLeft = false;
            }
        }

        // 执行插入
        if (isLeft) {
            parentNode.setLeftChildNode(nNode);
        } else {
            parentNode.setRightChildNode(nNode);
        }
        // 链接父节点
        nNode.setParentNode(parentNode);

        // 新的节点插入以后，可能会破坏数的平衡,检查红黑色规则，如果说不符合红黑树规则，需要纠正
        balanceFix(nNode);
    }

    /**
     * 实现红黑树平衡修正
     * 主要是进行三大情况处理
     * @param nNode 新插入的节点
     *
     * */

    private void balanceFix(RedBlackNode nNode) {

        // 父节点
        RedBlackNode parentNode;
        // 祖父节点
        RedBlackNode grandfatherNode;
        // 叔叔节点
        RedBlackNode uncleNode;

        // 修正违反红黑规则的节点,满足大情况3（大情况1，和大情况2都不需要管）
        while ((parentNode = nNode.getParentNode()) != null && parentNode.getColor() == RedBlackNode.NodeColor.RED) {

            // 不能直接拿到叔叔节点，所以可以通过祖父节点来拿，所以要判断父节点在祖父节点哪一边，则祖父节点的另一边就是叔叔节点
            grandfatherNode = nNode.getParentNode().getParentNode();
            if (parentNode == grandfatherNode.getLeftChildNode()) { //父节点为祖父节点左边，所以叔叔节点在祖父节点右侧(变色->左旋->变色->右旋)
                uncleNode = grandfatherNode.getRightChildNode();
                // 三种情况的处理,是一个完成流程,情况1->情况2->情况3，是一整套顺序流程，必然会按顺序发生的

                // 情况1：[插入节点]的[父节点]和其[叔叔节点](祖父节点的另一个子节点)均为[红色]
                if (uncleNode != null && uncleNode.getColor() == RedBlackNode.NodeColor.RED) {
                    // 父节点涂黑
                    parentNode.setColor(RedBlackNode.NodeColor.BLACK);
                    // 叔叔节点涂黑
                    uncleNode.setColor(RedBlackNode.NodeColor.BLACK);
                    // 祖父节点涂红
                    grandfatherNode.setColor(RedBlackNode.NodeColor.RED);
                    // 指向更改到祖父节点,之后的操作针对于祖父节点
                    nNode = grandfatherNode;
                    // 继续情况2
                    continue;
                }
                // 情况2：[插入节点]的[父节点]是[红色]的，[叔叔节点]是[黑色]的，且[插入节点]是其[父节点]的[右子节点]
                if (nNode == parentNode.getRightChildNode()) {
                    // 当前节点的[父节点]为顶点[左旋]
                    leftRotate(parentNode);
                    // 指针指向更改，指向刚刚[父节点]
                    nNode = parentNode;
                }
                // 情况3:[插入节点]的[父节点]是[红色]的，[叔叔节点]是[黑色]的，且[插入节点]是其[父节点]的[左子节点]
                // 顶点左旋后的下降节点的父节点更改颜色为黑色
                nNode.getParentNode().setColor(RedBlackNode.NodeColor.BLACK);
                // 祖父节点变成红色
                grandfatherNode.setColor(RedBlackNode.NodeColor.RED);
                // 以祖父节点为顶点右旋
                rightRotate(grandfatherNode);
            } else {//父节点为祖父节点右边，所以叔叔节点在祖父节点左边(变色->右旋->变色->左旋)
                uncleNode = grandfatherNode.getLeftChildNode();
                // 同第一个if分支一样的逻辑，只不过所有操作都相反
                // 情况1:
                if (uncleNode != null && uncleNode.getColor() == RedBlackNode.NodeColor.RED) {
                    parentNode.setColor(RedBlackNode.NodeColor.BLACK);
                    uncleNode.setColor(RedBlackNode.NodeColor.BLACK);
                    grandfatherNode.setColor(RedBlackNode.NodeColor.RED);
                    nNode = grandfatherNode;
                    continue;
                }
                // 情况2
                if (nNode == parentNode.getLeftChildNode()) {
                    rightRotate(parentNode);
                    nNode = parentNode;
                }
                // 情况3
                nNode.getParentNode().setColor(RedBlackNode.NodeColor.BLACK);
                grandfatherNode.setColor(RedBlackNode.NodeColor.RED);
                leftRotate(grandfatherNode);
            }
        }
        // 千万不要忘记，最后一步，需要把root节点涂黑
        root.setColor(RedBlackNode.NodeColor.BLACK);
    }

    private boolean isEmpty() {
        return root == null;
    }

    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.insert(1);
        rbt.insert(2);
        rbt.insert(3);
        rbt.insert(4);
    }
}

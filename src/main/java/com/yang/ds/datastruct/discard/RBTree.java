package com.yang.ds.datastruct.discard;


/**
 *
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
 *  1).以N的[父节点]作为[顶点]进行[左旋]
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
 * 红黑树删除链接笔记:
 * http://note.youdao.com/noteshare?id=61a754239f5ffe2106c5461eba1ff1f8
 * 代码参考
 * https://blog.csdn.net/weixin_40037053/article/details/89947885
 * */
@Deprecated
public class RBTree {

    // 左节点
    private static final int LEFT = 1;

    // 右节点
    private static final int RIGHT = 2;

    // 插入方式查找插入节点位置
    private static final int INSERT = 1;

    // 普通查找节点
    private static final int FIND = 2;

    // 红黑树默认的叶子节点
    private static final RBNode NIL = null;

    // 根节点定义
    private RBNode root;


    public RBTree() {
        this.root = null;
    }

    public RBNode root() {
        return this.root;
    }

    /**
     * 左旋 以x为顶点，针对于右子树
     *   x              y
     * a  y    ---->  x   b
     *   r b         a r
     *
     * 主要是三大步骤处理
     * step1 step2 step3
     */
    private void leftRotate(RBNode x) {
        // 获取右子树
        RBNode y = rightOf(x);
        // 根节点判断 x为root节点
        if (root() == x) {
            root = y;
        }
        int lr = lr(x, parentOf(x));

        // step1: y父节点链接
        appendChild(lr, parentOf(x), y);

        // step2:x节点的右孩子链接
        appendChild(RIGHT, x, leftOf(y));

        // step3 y节点的左孩子链接
        appendChild(LEFT, y, x);
    }

    /**
     * 右旋，以y为顶点，针对左子树
     *    y              x
     *  x  a     --->  r  y
     * r b               b a
     * */
    private void rightRotate(RBNode y) {
        // 获取左子树
        RBNode x = leftOf(y);
        if (root() == y) {
            root = x;
        }
        int lr = lr(y, parentOf(y));

        //step1 x父节点链接
        appendChild(lr, parentOf(y), x);

        //step2 y节点左孩子链接
        appendChild(LEFT, y, rightOf(x));

        //step3 x节点右孩子链接
        appendChild(RIGHT, x, y);
    }

    /**
     * 插入
     * */
    public void insert(int data) {
        // 插入节点默认为黑色
        RBNode nNode = new RBNode(RBNode.RED, data, null, null, null);
        if (isEmpty()) {
            root = nNode;
            setBlack(root);
            return;
        }
        insert(nNode);
    }

    private void insert(RBNode nNode) {
        // 查找插入位置
        RBNode parentNode = findNode(nNode, INSERT);
        // 插入节点
        if (nNode.getData() < parentNode.getData()) {// 左侧
            parentNode.setLeft(nNode);
        } else {// 右侧
            parentNode.setRight(nNode);
        }
        nNode.setParentNode(parentNode);

        // 修复红黑树平衡
        insertBalanceFix(nNode);
    }

    /**
     *  讨论树的方向，左子树，右子树
     * --左子树
     *   正常存在普通的后继节点情况分析(后继节点是删除节点的右子树中的最左面的最小子节点)，[左子树少1个黑色节点]
     * --右子树
     *   选举出来的后继节点，父节点就是被删除的节点，这样出现红黑树不平衡就是在右子树[右子树少一个黑色节点]
     *   注意:如果出现后继节点父节点就是被删除节点，同时后继节点是黑色，那么它右孩子一定是Nil,因为后继节点的左孩子是Nil(如果左孩子不是Nil，那后继节点肯定在最左)
     * [左子树问题分析]:
     * 红黑树平衡修复主要讨论case3
     * 总结删除的几大情况(存在普通的后继节点,也就是说一个最完整case，删除节点存在左右子树，并且后继节点是右子树的最左面)
     * case1.替换节点是红色,(直接干掉，不影响黑色节点个数)
     *  因为替换节点是最左面的，所以左孩子是Nil，所以右孩子必须是黑色，且右孩子必须是Nil
     *
     * case2.替换节点是黑色,右孩子是红色,一定是存在不为Nil的红色右孩子(右孩子变黑色，右孩子提升到替换节点的位置(充当替换节点父节点的左儿子),也就是普通二叉树的删除，多个变色步骤而已)
     *  因为替换节点的左孩子是NIl这个时候如果存在不为Nil的右孩子，那么肯定是一个红色的，并且右孩子的左右孩子都是Nil，
     *
     * case3.替换节点是黑色，并且右孩子是黑色，那么右孩子一定是Nil
     *  因为替换节点的左孩子是黑色的Nil，右孩子如果也是黑色只能是Nil,这个时候就只能讨论右孩子在普通二叉树删除完之后提升到了替换节点的位置，我们
     *  以这个位置，来进行讨论，我们只是针对于这个位置讨论跟节点是什么没关系了，因为二叉树删除完之后，这个位置就是个null了也就是Nil，所以我们以这个位置为
     *  基础点出发（普通二叉树删除替换完之后替换节点的位置，如果替换节点存在右孩子，或者右孩子是null(Nil)），讨论起兄弟节点的问题,又会出现如下情况
     *
     *  1.兄弟节点红色
     *  必定存在两个"不为Nil"的黑色节点,-->下一层存在红红/红黑/黑黑(黑色节点为Nil)
     *  step1:兄弟涂黑
     *  step2:父亲涂红
     *  step3:父亲节点为顶点左旋
     *  step4:变成了兄弟节点为黑色的情况
     *
     *  2.父亲黑色，兄弟黑色，兄弟的两个孩子也是黑色，这种情况就是一个额外判断，就是当前逻辑已经处理不了，需要向上一层回溯
     *  step1:兄弟节点涂红
     *  step2:父节点角色改为右子节点角色
     *  step3:父节点的父节点充当父节点角色
     *  step4:回溯到上一层，递归处理
     *
     *  3.兄弟节点黑色
     *  相对于兄弟节点为红色少一层,兄弟节点-->下一层存在/红红/红黑/黑黑(黑色节点为Nil)
     *  这里面处理的情况就想红黑树的插入是一样，就是把三个节点弄到同一侧，然后旋转
     *  父节点-->兄弟节点-->兄弟的红色子节点 不在同一侧
     *  step1.兄弟节点涂成父亲节点颜色
     *  step2.兄弟节点红色孩子节点涂黑色
     *  step2.兄弟节点为顶点右旋变成:父节点-->兄弟节点-->兄弟的红色子节点 在同一侧
     *  父节点-->兄弟节点-->兄弟的红色子节点 在同一侧
     *  step1.兄弟节点涂成父亲节点颜色
     *  step2.兄弟节点的红色孩子节点涂黑色
     *  step3.父节点涂黑
     *  step4.父亲节点为顶点左旋
     *
     * case3:的情况主要思路是想把兄弟节点的下面如果存在红色节点，把它通过旋转提拉上来，通过变色旋转一系列操作让红黑树平衡
     *  左面少，就想办法让左面加1右面少就想办法让右面加1,然后左旋
     * [右子树问题分析]:
     * 出现存在右子少1个节点的情况
     *
     * 特殊情况变相对应,结合树的删除的四种情况
     * 1.删除节点为叶子节点
     * 2.删除节点有一个子树
     * 3.删除节点同时拥有两个子树，选举出来的后继节点是普通的后继节点，在右子树的最左面
     * 4.删除节点同时拥有两个子树，选举出来的后继节点父节点是被删除的节点
     * case1:直接删除
     * 1.删除节点为叶子节点
     *   叶子节点为红色,[可以直接删除]
     * 2.删除节点有一个子树
     *   删除节点是红色，不可能存在一个子树，如果存在只能是Nil,红红不行，红黑只能是Nil
     * 3.删除节点同时拥有两个子树，选举出来的后继节点是普通的后继节点（红色），在右子树的最左面
     *   这个是普通的后继节点问题，左孩子是Nil，右孩子必为Nil，[可以直接删除]
     * 4.删除节点同时拥有两个子树，选举出来的后继节点父节点是被删除的节点
     *   这时候后继节点的左子树为Nil(不为Nil肯定还有左孩子，次节点不可能是后继节点),右孩子肯定也是Nil,红红不行，红黑，黑色只能是Nil[可以直接删除]
     *case2:变色处理,统一归类于存在[黑色节点红色右孩子]的问题,只有存在红色右孩子，才有可能提升右孩子改色，实现红黑平衡
     * 1.删除节点为叶子节点
     *   普通树中的叶子节点没有孩子，所以不存在右孩子为红色的情况，只有两个黑色的Nil，不成立，这里不需处理
     * 2.删除节点有一个子树
     *   删除节点是黑色，有一个子树，说明其中一个是Nil那么另一个只能是红色，否则只能是Nil，这个时候删除节点父引用直接指向删除节点的红色孩子，[红色孩子变色]
     * 3.删除节点同时拥有两个子树，选举出来的后继节点是普通的后继节点，在右子树的最左面
     *   后继节点的红色右孩子提升到后继节点的位置，[红色孩子变色]
     * 4.删除节点同时拥有两个子树，选举出来的后继节点父节点是被删除的节点
     *   后继节点的左孩子为Nil右孩子是红色，且红色右孩子的父节点就是后继节点,[红色孩子变色]
     * case3:黑(替换或者删除的位置)-黑(右孩子)的情况处理，这里也是红黑树删除平衡的最关键的地方
     * 1.删除节点为叶子节点
     *   删除的是一个黑色的叶子节点，怎么去讨论其右孩子问题，这不是扯淡吗？我们需要看做，并不是绝对的，我们此时可以看做删除节点就是后继节点的位置
     * 右孩子就是Nil,而此时所讨论兄弟节点也就是相对于被删除节点的兄弟节点，父节点就是删除节点的父节点，因为删除完这个节点，父节点当然知道是左面还是右面的
     * 节点为Nil了，所以兄弟节点当然能判断出，并且删除节点是黑色，兄弟节点肯定存在啊，不可能是Nil，红色或者黑色真实存在的节点[满足case3处理逻辑]
     * 2.删除节点有一个子树
     *   上面已经说了只有一个子树，只能是有一个红色节点，满足变色处理，但是不满足case3，不予处理
     * 3.删除节点同时拥有两个子树，选举出来的后继节点是普通的后继节点，在右子树的最左面
     *   正是我们讨论最正常的普通后继节点的问题,右孩子是Nil，[满足case3处理逻辑]
     * 4.删除节点同时拥有两个子树，选举出来的后继节点父节点是被删除的节点
     *   此时如果右孩子是黑色只能是Nil，[满足case3处理逻辑]
     *
     *
     * 个人理解:
     * 1.黑色节点的高度相差不会超过1
     * 2.红黑树出现黑黑的情况，黑色的右孩子一定是Nil
     * 3.删除节点为黑色如果有一个子孩子一定是红色
     * 4.后继节点如果父节点是被删除节点并且后继节点是黑色，其右孩子一定是Nil
     * 5.普通后继节点如果右孩子是黑色也一定是Nil
     * 6.红黑树红色节点要么有俩黑色孩子，要么右两个Nil
     * 7.黑色节点可以只有一个红色孩子，也不能只有一个黑色孩子
     * 所以说讨论黑色的右孩子的问题实际上是站在删除节点(替换节点)的位置上讨论其兄弟节点的问题
     * 讨论位置:
     * 1.删除节点不满足后继节点选举，所以要站在删除节点,父节点，兄弟节点讨论
     * 2.删除节点满足后继节点选举，那删除节点位置我们就不需要管了，反正都会有新节点顶替，唯一能出现红黑树变化的实际上时后继节点的位置，只有那个地方才是
     * 真正少了节点的地方
     * 删除节点三板斧:
     * 1.红色直接删
     * 2.右子孩子是红色，提升上来改色
     * 3.黑-黑情况处理,旋转+加变色+回溯上一级
     * 大情况原则:
     * 1.存在后继节点看后继节点
     * 2.不存在后继节点看删除节点
     * 代码注意事项:
     * 在更改引用链接的时候，比如需要判断当前节点的原有关系时候一定要在最前面，不要放生了关系更改之后再判断关系,引用已经变了，大坑，注意
     *
     *
     * *********红黑树删除核心就是调整节点变化的相对位置的兄弟节点的问题,也就是黑黑的问题*******
     * */

    public boolean delete(int data) {
        RBNode needDelNode = findNode(new RBNode(data), FIND);
        if (needDelNode == null) {
            return false;
        }
        return delete(needDelNode);
    }

    private boolean delete(RBNode needDelNode) {
        // case1:叶子节点
        if (isLeaf(needDelNode)) {
            // 红黑树平衡修复 右子节点，父节点
            RBNode parent = parentOf(needDelNode);
            int fixColor = colorOf(needDelNode);

            // 删除节点为根节点的情况
            if (isRoot(needDelNode)) {
                root = NIL;
                return true;
            }
            int lr = lr(needDelNode, parentOf(needDelNode));
            appendChild(lr, parentOf(needDelNode), NIL);

            /*
             * 平衡修复
             * */
            //1. 直接删除
            if (isRed(fixColor)) {
                return true;
            }
            //2. 改色

            // 平衡修复
            delBalanceFix4Black(parent);
        }
        // case2:存在一个子节点,只能是删除节点是黑色节点，带一个红色子节点
        if (hasOneChild(needDelNode)) {
            RBNode replacement = leftOf(needDelNode) != null ? leftOf(needDelNode) : rightOf(needDelNode);
            // 必定是黑色
            if (needDelNode == root()) {
                root = replacement;
            }
            int lr = lr(needDelNode, parentOf(needDelNode));
            appendChild(lr, parentOf(needDelNode), replacement);
            /*
             * 平衡修复
             * */
            // 1.直接删除

            // 2. 改色
            setColor(replacement, colorOf(needDelNode));

            // 3. 平衡调整
            return true;
        }

        // case3:存在两个子节点
        if (hasTwoChild(needDelNode)) {
            // 选举后继节点
            RBNode replacement = findMin(needDelNode.getRight());
            RBNode rightChild = replacement.getRight();
            int fixColor = colorOf(replacement);
            RBNode parent = parentOf(replacement) == needDelNode ? replacement : parentOf(replacement);

            // 删除节点为root节点
            if (isRoot(needDelNode)) {
                root = replacement;
            }
            //step1 后继节点右孩子处理
            if (parentOf(replacement) != needDelNode) {/**重点注意这个逻辑*/
                appendChild(LEFT, parentOf(replacement), rightOf(replacement));
            }

            // step2 后继节点左子树
            appendChild(LEFT, replacement, leftOf(needDelNode));

            // step3 后继节点右子树
            if (parentOf(replacement) != needDelNode) {/**重点注意这个逻辑*/
                appendChild(RIGHT, replacement, rightOf(needDelNode));
            }

            // step4 后继节点父节点链接(以上用到了替换节点原有的父节点进行判断，一定要放在此处更改父节点引用之前,注意,注意，注意)
            int lr = lr(needDelNode, parentOf(needDelNode));
            appendChild(lr, parentOf(needDelNode), replacement);

            // step5 改色为删除节点的颜色
            replacement.setColor(colorOf(needDelNode));

            /*
             * 平衡修复
             * */
            // 1. 直接删除
            if (isRed(fixColor)) {
                return true;
            }
            // 2.改色
            if (isRed(rightChild)) {
                setColor(rightChild, colorOf(replacement));
                return true;
            }
            // 3.平衡修复
            delBalanceFix4Black(parent);
        }
        return true;
    }

    private void appendChild(int lr, RBNode parentNode, RBNode childNode) {
        // 如果传入的父节点是root节点的父节点情况
        if (NIL != parentNode) {
            if (LEFT == lr) {
                parentNode.setLeft(childNode);
            } else {
                parentNode.setRight(childNode);
            }
        }
        if (childNode != NIL) {
            childNode.setParentNode(parentNode);
        }
    }


    /**
     * 红黑树，插入节点平衡修复
     * */
    private void insertBalanceFix(RBNode nNode) {
        RBNode parentNode = null;
        //
        while ((parentNode = parentOf(nNode)) != null && isRed(colorOf(parentNode))) {
            // 祖父节点
            RBNode grandfatherNode = parentOf(parentOf(nNode));
            // 叔叔节点
            RBNode uncleNode = brotherOf(parentNode, parentOf(parentNode));

            // 情况1，父节点，叔叔节点都为红色，叔叔、父亲涂黑，祖父涂红，递归到祖父节点处理，祖父节点为root则跳出循环,涂黑root
            if (uncleNode != null && isRed(uncleNode)) {
                setBlack(parentNode);
                setBlack(uncleNode);
                setRed(grandfatherNode);
                nNode = grandfatherNode;
                continue;
            }

            // 情况2，叔叔节点为黑色,让当前插入节点和父节点旋转成祖父节点的同一方向
            if ((lr(parentNode, grandfatherNode) ^ lr(nNode, parentNode)) != 0) {// 异或值不为0代表不是同一方向
                if (LEFT == lr(parentNode, grandfatherNode)) {
                    leftRotate(parentNode);
                } else {
                    rightRotate(parentNode);
                }
                nNode = parentNode;
            }

            // 情况3,如果经历了情况2必定经历情况3，情况3也可能单独出现,所以这里不需要判断了
            setBlack(parentOf(nNode));
            setRed(grandfatherNode);
            if (LEFT == lr(parentOf(nNode), grandfatherNode)) {
                rightRotate(grandfatherNode);
            } else {
                leftRotate(grandfatherNode);
            }
        }

        // 根节点涂黑
        setBlack(root);
    }

    /**
     * @param parent 1.普通后继节点父节点,2. 被删除节点为黑色的普通树的叶子节点的父节点,3.后继节点
     *
     * 删除节点平衡处理(逻辑按照一个存在后继节点的普通情况，也就是后继节点在被删除节点的右子树中的最左面)
     * 这里只处理替换节点为黑色，同时替换节点的右孩子也是黑色的情况，因为这种情况无法做到把孩子上提，然后更改颜色修正红黑树的平衡
     *
     * 1. 这里面只处理后继节点的右子节点为黑色的情况，黑色节点存在两种情况:
     * NIL 节点，和真正存在的黑色节点
     *
     * 2. 二叉树删除之后，替换节点的右子节点的位置发生了变化，右子节点变成了替换节点的左子节点
     *  所以后面所有逻辑都是按照替换节点的相对位置来处理，并不依赖替换节点，比如：
     *  替换节点早已经被替换节点的右子节点替换了，我们还要处理替换节点的兄弟节点的问题，其实这个节点已经是NIL了
     *
     * 3. 这里面处理只是处理替换节点的右子节点为黑色的情况(下面特殊情况，我们可以变相看做是后继节点的普通情况处理,同样满足逻辑)
     *  case1) 被删除节点是一个黑色节点,并且是普通树中的叶子节点
     *    rightChildNode是一个NIL(黑色节点),parent是被删除节点的父节点
     *  case2)选举出来的替换节点(后继节点)父节点刚好就是被删除的节点
     *   rightChildNode是一个黑色节点(NIL或者非NIL的黑色节点),rightChildNode为替换节点的右孩子,parent就是替换节点
     *   理解后继节点的普通情况:
     *      1.而正常的选举出来的后继节点肯定是在最左面，所以替换完之后就是最左面少了一个黑色节点，而现在刚好相反，是右面少了一个黑色节点（因为替换节点需要顶替被删除位置的节点）
     *      2.而正常的选举出来的后继节点最终都是要替换删除节点的位置，指针会不存在引用删除掉，所以对于树来看，我们看到的是删除节点的右子树中最左的叶子节点被删除了，红黑树的平衡也是从
     *      这里开始破坏的，和被删除的节点颜色还有位置没啥毛线关系,因为后继节点完全一样的去顶替了被删除节点,而正好这个时候回把选举出来的右孩子来顶替他，也就是把替换节点的右孩子上提
     *      变成替换节点的父节点的做孩子(因为替换节点在最左边，肯定是他父节点的左孩子)
     *   理解后继节点的父节点为被删除节点的情况:
     *     1.这时候选举出来的替换节点刚好比较特殊,没有左子树更小的节点，这时候站在二叉树删除的角度，被删除节点直接链接到这个替换节点就可以了
     *     2.同时这个时候相对于后继节点的普通情况来比，不需要把右孩子在上提到替换节点的位置(替换节点父节点的左孩子的位置),因为这个位置不需要上提，
     *     因为已经满足二叉树的左右大小的规则
     *  总结:所以说，rightChildNode=替换节点的右孩子,parent=替换节点
     *
     *
     *
     *  Ps: 这个方法可以看做是右孩子被提升到替换节点的位置，然后再此节点的位置去继续处理兄弟节点的问题,就是右子孩子是NIL的时候，处理兄弟节点
     * */


    private void delBalanceFix4Black(RBNode parent) {
        RBNode curNode = NIL;
        while (isBlack(curNode) && !isRoot(curNode)) {
            /**
             * 写着写着突然有个问题想不明白了，在这里面说一下
             * 如果rightChildNode是NIL那么如何判断是在父节点的左还有呢？比如说父节点的左右都是NIL
             *
             * 分析:
             * 出现这种情况都会什么时候出现呢？
             *
             * 1.替换节点为普通的后继节点
             *   这种情况，如果替换节点黑色，那么其父节点肯定存在一个右子节点同时也是不为NIL的黑色节点(路径自己脑补红黑树规则)
             *   所以这种情况肯定能判断出左右
             *
             * 2.被删除节点为黑色，并且没有左子树或者没有右子树、或者都没有
             * 为啥我们把这三种情况放到一起讨论呢？分析如下：
             * 1).为啥讨论是黑色，红色我们不用管他了啊，删了也不影响平衡,并且如果是红色不可能存在子节点
             * 红红不行，红黑，必须有俩黑子节点，那就不是被删除节点只有一个子节点或者没有的情况了
             * 2).黑色的情况，要么子节点都是NIL ，要么可能存在一个红色的子节点，但是如果有红色的子节点，删除之后子节点被提上去，改色就可以了，所以我们也不用考虑
             * 3).除了改色的简单处理就剩下被删除节点是黑色，同时没有子节点了，这时候删掉了，就少了一个黑色节点了，并且没有能提升的孩子，经过删除之后父节点的指针肯定会发生变化
             * 要么左面变成NIL了要么右面变成NIL了，递归的去想，被删除节点在删除之前是一个不为NIL的黑色节点，那么肯定还有一个兄弟是不为NIL的黑色节点
             * 所以说：这种情况依然能判断出左右
             * 3.替换节点(黑色)的父节点就是被删除节点，因为这种情况可以看做是特殊的后继节点的选举,这种情况有点小复杂
             * 1).这种情况的替换节点就相当于父节点 参数parent，但是呢？这个时候rightChildNode就是这个替换节点的右孩子
             *   (1)右孩子不为NIL，那么肯定是一个红色节点，那也不是这里讨论的问题，直接把右孩子改色就可以了，不会进入这里处理
             *   因为替换节点不可能存在左孩子，如果存在左孩子就是普通的后继节点问题了，就是第1中情况了
             *   (2)所以说这种情况，如果右孩子也是黑色的话只能是NIL，那么这种情况就是父节点是黑色，左孩子是黑右孩子也是黑，这一层就不能解决了
             *   只能把父节点当做右孩子的情况，充当右孩子的角色向上一级追溯，递归继续处理
             *  所以说：判断这种情况无论进入代码的那个分支都不会执行旋转等一系列的操作，只能是回溯到上一级处理,无论进入那个分支都可以
             *
             *  罗里吧嗦的说了一大堆，希望对理解有所帮助，如果代码写到这里存在相同疑问的可以看看上面这一坨屎一样的解释
             *
             *
             * */

            /**
             *
             * 下面代码脑子里要装着普通后继节点处理的情况来写/还有就是特殊的后继节点情况，就是后继节点没左孩子(要么右俩NIL孩子/右一个红色的右孩子）
             * */

            //获取兄弟节点
            RBNode brotherNode = brotherOf(curNode, parent);
            // 普通的后继节点情况,后继节点在被删除节点的右子树中的最左侧
            int cur4PLr = lr(curNode, parent);
            int brother4PLr = lr(brotherNode, parent);

            // case1:兄弟节点为红色，那么兄弟节点必定有两个不为NIL的黑色孩子，并且孩子的下一层孩子只能有红色或者NIL（脑补路径）
            // 向rightChildNode方向旋转，因为rightChildNode是NIL节点,所以少1个黑色节点，这样旋转为下面平衡调整做准备
            if (isRed(brotherNode)) {
                // brotherNode涂黑,parent 涂红, parent为顶点左旋或者右旋
                setBlack(brotherNode);
                setRed(parent);
                if (cur4PLr == LEFT) {
                    leftRotate(parent);
                } else {
                    rightRotate(parent);
                }
                continue;
            }
            // case2:类似于红黑树的插入操作，判断祖孙三代节点是否在同一侧,parent相当于爷爷，brotherNode是父亲,brotherRedChild是孙子
            if (isBlack(brotherNode) && hasOnlyOneRedChild(brotherNode)
                    && revertDirectionRedChild(brother4PLr, brotherNode) != null
                    ) {
                setRed(brotherNode);
                setBlack(revertDirectionRedChild(brother4PLr, brotherNode));
                // 右旋针对于左子树，brotherRedChild在brotherNode的左面
                if (lr(brotherNode, parent) == RIGHT) {
                    rightRotate(brotherNode);
                } else {
                    leftRotate(brotherNode);
                }
                continue;
            }
            // case3:在同一侧,这样处理完成之后就可以跳出循环了，红黑树已经平衡
            if (isBlack(brotherNode) && sameDirectionRedChild(brother4PLr, brotherNode) != null
                    ) {
                setColor(brotherNode, colorOf(parent));
                setBlack(sameDirectionRedChild(brother4PLr, brotherNode));
                // 注意顺序，父节点最后涂黑，要不兄弟节点涂改成父节点颜色，不就出事了吗
                setBlack(parent);
                // 右旋针对于左子树，brotherRedChild在brotherNode的左面
                if (lr(brotherNode, parent) == RIGHT) {
                    leftRotate(parent);
                } else {
                    rightRotate(parent);
                }
                break;
            }

            // case4:如果兄弟节点为黑，兄弟节点的孩子也都是黑色，父节点是红色,则改变父节点为红色，兄弟节点为红色，左右黑色节点一样多
            if (isBlack(leftOf(brotherNode)) && isBlack(rightOf(brotherNode)) && isRed(parent)) {
                setBlack(parent);
                setRed(brotherNode);
                break;
            }

            // case5:兄弟节点没有子节点也就是兄弟节点的子节点都是NIL这种情况无法保证左旋右旋才能保证平衡，需要向上一层追溯
            if (isBlack(leftOf(brotherNode)) && isBlack(rightOf(brotherNode)) && isBlack(parent)) {
                // 这个时候其实把兄弟节点涂红，就平衡，但是只是parent这个子树平衡了，对于整棵树来说，经过这颗子树的路径黑色节点少一个
                setRed(brotherNode);
                // 角色向上替换
                curNode = parent;
                parent = parentOf(curNode);
                continue;
            }
        }
    }


    private boolean hasOnlyOneRedChild(RBNode node) {
        if (isRed(node.getLeft()) && isBlack(node.getRight())) {
            return true;
        } else if (isRed(node.getRight()) && isBlack(node.getLeft())) {
            return true;
        }
        return false;
    }

    private RBNode revertDirectionRedChild(int lr, RBNode node) {
        if (lr == LEFT) {
            if (isRed(node.getRight())) {
                return node.getRight();
            }
        } else {
            if (isRed(node.getLeft())) {
                return node.getLeft();
            }
        }
        return null;
    }

    private RBNode sameDirectionRedChild(int lr, RBNode node) {
        if (lr == LEFT && isRed(node.getLeft())) {
            return node.getLeft();
        }
        if (lr == RIGHT && isRed(node.getRight())) {
            return node.getRight();
        }
        return null;
    }


    /**
     * 删除节点存在左右子树，需要在右子树中选举出一个最小节点
     *
     * */


    public RBNode find(int data) {
        return findNode(new RBNode(data), FIND);
    }

    /**
     *
     * 查找节点，区分普通查找，还是插入查找
     * */
    private RBNode findNode(RBNode node, int opt) {
        notEmptyTree();
        RBNode currentNode = root;
        RBNode parentNode = null;
        while (currentNode != null) {
            parentNode = currentNode;
            // 如果不是insert查找节点,则找到的节点就停止
            if (opt != INSERT && node.getData() == currentNode.getData()) {
                break;
            }
            if (node.getData() < currentNode.getData()) { // 小于在左面
                currentNode = currentNode.getLeft();
                continue;
            } else {// 大于等于在右面
                currentNode = currentNode.getRight();
                continue;
            }
        }
        return ((opt == INSERT) ? parentNode : currentNode);
    }

    /**
     * 获取当前节点子树中的最大值
     * */
    private RBNode findMax(RBNode currentNode) {
        notNullNode(currentNode);
        while (currentNode.getRight() != null)
            currentNode = currentNode.getRight();
        return currentNode;
    }

    /**
     * 获取当前节点子树中的最小值
     *
     * */
    private RBNode findMin(RBNode currentNode) {
        notNullNode(currentNode);
        // 一直向左子树移动，直到左子树为空，获取最左面的叶子节点就是最小节点
        while (currentNode.getLeft() != null)
            currentNode = currentNode.getLeft();
        return currentNode;
    }

    /**
     * 中序遍历，左，根，右
     * */
    public void midErgodic(RBNode currentNode) {
        notEmptyTree();
        // 边界条件
        if (currentNode == null) {
            return;
        }
        midErgodic(currentNode.getLeft());
        System.out.print(currentNode.getData() + " ");
        midErgodic(currentNode.getRight());
    }

    /**
     * 前序遍历,根左右
     * */
    public void preErgodic(RBNode currentNode) {
        notEmptyTree();
        // 边界条件
        if (currentNode == null)
            return;
        System.out.print(currentNode.getData() + " ");
        preErgodic(currentNode.getLeft());
        preErgodic(currentNode.getRight());
    }

    /**
     * 后续遍历，根左右
     * */
    public void afterErgodic(RBNode currentNode) {
        notEmptyTree();
        if (currentNode == null)
            return;
        afterErgodic(currentNode.getLeft());
        afterErgodic(currentNode.getRight());
        System.out.print(currentNode.getData() + " ");
    }

    /**
     *
     * 节点位于父节点的左面还是右面,L左面,R右面
     * */
    private int lr(RBNode childNode, RBNode parentNode) {
        if (childNode == leftOf(parentNode)) {
            return LEFT;
        }
        return RIGHT;
    }

    private boolean isLeaf(RBNode currentNode) {
        return (isNil(leftOf(currentNode)) && isNil(rightOf(currentNode)));
    }

    private boolean isNil(RBNode currentNode) {
        return currentNode == NIL;
    }

    private boolean hasOneChild(RBNode currentNode) {
        if (!isNil(leftOf(currentNode)) && isNil(rightOf(currentNode))) {
            return true;
        }
        if (isNil(leftOf(currentNode)) && !isNil(rightOf(currentNode))) {
            return true;
        }
        return false;
    }

    private boolean hasTwoChild(RBNode currentNode) {
        return (!isNil(leftOf(currentNode)) && !isNil(rightOf(currentNode)));
    }

    private RBNode leftOf(RBNode curentNode) {
        return (curentNode == null ? null : curentNode.getLeft());
    }

    private RBNode rightOf(RBNode currentNode) {
        return (currentNode == null ? null : currentNode.getRight());
    }

    private RBNode parentOf(RBNode currentNode) {
        return (currentNode == null ? null : currentNode.getParentNode());
    }

    private int colorOf(RBNode currentNode) {
        return (currentNode == null ? RBNode.BLACK : currentNode.getColor());
    }


    private boolean isRoot(RBNode currentNode) {
        return currentNode == root;
    }

    private void setRed(RBNode currentNode) {
        if (NIL != currentNode)
            currentNode.setColor(RBNode.RED);
    }

    private void setBlack(RBNode currentNode) {
        if (NIL != currentNode)
            currentNode.setColor(RBNode.BLACK);
    }

    private void setColor(RBNode currentNode, int color) {
        currentNode.setColor(color);
    }

    private RBNode brotherOf(RBNode childNode, RBNode parent) {
        if (leftOf(parent) == childNode) {
            return rightOf(parent);
        } else {
            return leftOf(parent);
        }
    }

    private boolean isEmpty() {
        return root == null;
    }

    private void notEmptyTree() {
        if (isEmpty()) {
            throw new IllegalArgumentException("tree is empty!");
        }
    }

    private void notNullNode(RBNode node) {
        if (node == null)
            throw new IllegalArgumentException("node  is null!");
    }

    /**
     * 空节点为黑色，也就是红黑树中定义的叶子节点都是黑色NIL
     * */
    private boolean isRed(RBNode node) {
        return node == NIL ? false : node.getColor() == RBNode.RED;
    }


    private boolean isBlack(RBNode RBNode) {
        return !isRed(RBNode);
    }

    private boolean isRed(int color) {
        return color == RBNode.RED;
    }

    private boolean isBlack(int color) {
        return !isRed(color);
    }

    public static void main(String[] args) {
        // 初始化数据:
        // 1
        RBTree rbt = new RBTree();
        // 一下数据测试了红黑树旋转的所有case
        // case3 left
        rbt.insert(50);
        rbt.insert(25);
        rbt.insert(10);
        // case3 right
        rbt.insert(80);
        rbt.insert(85);
        rbt.insert(90);
        // case2/case3 right
        rbt.insert(87);
        // case2/case3 left
        rbt.insert(45);
        rbt.insert(48);
        // case1,case2,case3
        rbt.insert(40);
        // 不测了，所有case已经走一遍了
        rbt.insert(86);
        rbt.insert(43);
        rbt.insert(88);
        rbt.midErgodic(rbt.root());
        System.out.println();
        System.out.println(rbt.findMax(rbt.root()).getData());
        System.out.println(rbt.findMin(rbt.root()).getData());
        System.out.println(rbt.find(43).getData());
        // 测试删除
        //rbt.delete(rbt.root().getData());
        /**二叉树删除，被删除节点为叶子节点*/
        // case1 删除红色叶子节点
        // rbt.delete(45);

        // case2 删除黑色叶子
        //rbt.delete(50);

        /**二叉树删除，被删除节点存在一个子树*/
        // case1 删除节点为存在一个子树(删除节点存在一个子节点，只能黑色节点带着一个红色节点)
        //rbt.delete(85);

        /**二叉树删除，被删除节点同时拥有两个子树,并且后继节点为普通后继节点情况*/
        // case1 删除节点存在左右子树，并且为普通后继节点情况，后继节点为红色
        // rbt.delete(25);

        // case2 删除节点存在左右子树，并且为普通后继节点情况，后继节点为黑色，且存在红色右孩子
        //rbt.delete(80);

        // case3 删除节点存在左右子树，并且为普通后继节点情况，后继节点为黑色，并且右孩子是黑色Nil
        //rbt.delete(rbt.root().getData());// 数据比较少，只能用根节点的特殊情况来测试了，同时正好测试删除跟节点的情况

        // case4 删除节点为红色,这个case其实没啥卵用，存在后继节点的情况，我们不关注被删除节点的颜色,
        // rbt.delete(87);

        /**二叉树删除，被删除节点同时拥有两个子树,并且后继节点的父节点为删除节点*/
        //rbt.delete(40);// 删除数据满足此时需要测试的一下case
        //rbt.delete(88);// 删除数据满足此时需要测试的一下case
        // case1 删除节点颜色不关注，后继节点为黑色，测试满足 父黑、右孩子黑，右孩子兄弟节点的两个孩子也是黑色，返回上一级处理
        //rbt.delete(87);

        // case2 后继节点是红色
        //rbt.delete(43);

        // case3 后继节点是黑色,存在右孩子是红色
        //rbt.delete(25);
        rbt.delete(88);
        rbt.delete(86);
        rbt.delete(85);
        rbt.midErgodic(rbt.root());
        System.out.println();
        System.out.println("所有case测试完成，感谢所有TV.........");
        bianli();
    }

    private static void bianli() {
        RBTree rbt = new RBTree();
        rbt.insert(5);
        rbt.insert(10);
        rbt.insert(15);
        rbt.insert(20);
        rbt.insert(25);
        rbt.insert(30);
        rbt.insert(14);
        rbt.insert(17);
        rbt.insert(24);

        System.out.println("前序---------");
        rbt.preErgodic(rbt.root());
        System.out.println();
        System.out.println("中序----------");
        rbt.midErgodic(rbt.root());
        System.out.println();
        System.out.println("后序-----------");
        rbt.afterErgodic(rbt.root());
    }
}

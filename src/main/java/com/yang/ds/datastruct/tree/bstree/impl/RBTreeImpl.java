package com.yang.ds.datastruct.tree.bstree.impl;

import com.yang.ds.datastruct.tree.bstree.BSNode;
import com.yang.ds.datastruct.tree.bstree.BSTree;

/**
 * 红黑树实现
 *
 * ======节点简称=========
 N(now)
 P(parent) 当前节点的父节点
 U(uncle) 当前节点的叔叔节点
 S(sibling) 当前节点的兄弟节点
 G(grandfather) 当前节点的祖父节点
 D(delete)目标删除节点
 RD(real delete)真正删除的节点，因为非叶子节点的删除实际是转换到后继的叶子节点删除（真正删除可以理解为链表的删除，附近的引用链长度有没有缩减）
 SL(sib left)兄弟节点左孩子
 SR(sib right)兄弟节点右孩子
 * */

public class RBTreeImpl<K extends Comparable<? super K>, V> extends AbstractBSTree<K, V> implements BSTree<K, V> {
    private static final int RED = 1;
    private static final int BLACK = 2;
    private static final BSNode NIL = null;
    private Node root;

    @Override
    public void add(K key, V value) {
        // 查找二叉树的插入位置父节点
        if (null == key) {
            throw new IllegalArgumentException("key not null");
        }
        if (isEmpty()) {
            // 直接涂黑根节点,插入的是红色，与setBlack(root)合并成一步
            root = new Node(key, value, BLACK);
            return;
        }
        // 默认插入节点是红色的
        Node nNode = new Node(key, value, RED);
        Node parent = findNode(key, root, INSERT);
        int cmp = key.compareTo(parent.key);
        if (cmp < 0) {
            addChild(LEFT, parent, nNode);
        } else {
            addChild(RIGHT, parent, nNode);
        }

        // 红黑树插入平衡修复
        insertBalanceFix(nNode);
        // 涂黑根节点
        setBlack(root);
    }

    /**
     * 红黑树平衡修复
     * @param nNode  新插入的节点N
     * */
    private void insertBalanceFix(Node nNode) {
        Node parent = null;
        while (isRed((parent = nNode.parent)) && isRed(nNode)) {
            // 获取祖父节点
            Node grandFather = parentOf(parent);
            // 获取叔叔节点
            Node uncle = brotherOf(parent, grandFather);
            // 父节点所在祖父节点的方向
            int lr = lr(parent, grandFather);

            // case1:U为红色,U涂黑，P涂黑，G涂红，N指向G(祖父节点充当N继续处理),为出发case2和case3准备
            // case1可能会不断向上回溯
            if (isRed(uncle)) {
                setBlack(uncle);
                setBlack(parent);
                setRed(grandFather);
                nNode = grandFather;
                continue;
            }
            // case2: N、P、G不在同一侧，需要旋转到同一侧(P为顶点)，一定会出发case3,
            // case2是出发case3的前提条件，不能单独出现，肯定后面会执行case3
            if ((lr ^ lr(nNode, parent)) != 0) {
                if (LEFT == lr) {
                    leftRotate(parent);
                } else {
                    rightRotate(parent);
                }
                nNode = parent;
                continue;
            }
            // case3:N、P、G在同一侧，直接改色，旋转即可平衡，执行完case3，一定已经实现了平衡,所以case3可能是单独出现情况，case1和case2是为了触发case3
            if ((lr ^ lr(nNode, parent)) == 0) {
                setBlack(parent);
                setRed(grandFather);
                if (LEFT == lr) {
                    rightRotate(grandFather);
                } else {
                    leftRotate(grandFather);
                }
                break;
            }
        }
    }

    /**
     * 左旋 以x为顶点，针对于右子树
     *   x              y
     * a  y    ---->  x   b
     *   r b         a r
     *
     * @param x 旋转顶点x
     */
    private void leftRotate(Node x) {
        // 获取右子树，右子节点
        Node y = x.right;
        // root节点判断
        if (x == root) {
            root = y;
            y.parent = null;
        } else {
            addChild(lr(x, x.parent), x.parent, y);
        }
        addChild(RIGHT, x, y.left);
        addChild(LEFT, y, x);
    }

    /**
     * 右旋，以y为顶点，针对左子树
     *    y              x
     *  x  a     --->  r  y
     * r b               b a
     *
     * @param y 旋转顶点y
     * */
    private void rightRotate(Node y) {
        Node x = y.left;
        if (y == root) {
            x.parent = null;
            root = x;
        } else {
            addChild(lr(y, y.parent), y.parent, x);
        }
        addChild(LEFT, y, x.right);
        addChild(RIGHT, x, y);
    }

    private void addChild(int lr, Node parent, Node child) {
        notNullNode(parent);
        if (lr == LEFT) {
            parent.left = child;
        } else if (lr == RIGHT) {
            parent.right = child;
        } else {
            throw new IllegalArgumentException("error lr " + lr);
        }
        if (null != child) {
            child.parent = parent;
        }
    }

    /**
     * 删除，如果删除RD位置是黑色，一定是NIl同时兄弟节点一定存在不为Nil的黑色孩子，所以判断NIL在父节点的左侧还是右侧
     * 都是能判断出来的，所以获取兄弟节点的通过lr值来判断没有任何问题
     *
     * case1.删除之前RD是红色，则直接删除，不需要调整平衡
     * case2.删除之前RD是黑色，删除之后RD位置上的节点是红色则改为黑色，实现树的平衡(孩子会被提升上去)
     * case3.删除之后RD位置上是黑色（NIl）则需要进行红黑树删除的平衡修复
     * */
    @Override
    public void delete(K key) {
        if (null == key) {
            throw new IllegalArgumentException("key is not null");
        }
        // 查找节点
        Node needDel = findNode(key, root, FIND);
        if (null == needDel) {
            return;
        }

        // 叶子节点(case1/case3),不会执行case2，叶子节点删除RD位置上一定NIL
        if (isLeaf(needDel)) {
            // 这正删除的节点
            Node rd = needDel;
            if (root == needDel) {
                root = (Node) NIL;
                return;
            }
            int lr = lr(needDel, needDel.parent);
            addChild(lr, needDel.parent, null);
            //case1
            if (isRed(rd)) {
                return;
            }

            /*红黑平衡处理*/
            // case2
            deleteBalanceFix(rd.parent);
            return;
        }
        // 存在一个子树(case2),如果存在一个子树，一定是RD删除之前是黑色，并且只存在一个红色孩子,RD为红色只能有俩不为NIL黑色孩子，RD为黑色可以存在一个红色孩子
        if (justOneChild(needDel)) {
            Node child = needDel.left != null ? needDel.left : needDel.right;
            if (needDel == root) {
                replaceRoot(child);
            }
            int lr = lr(needDel, needDel.parent);
            addChild(lr, needDel.parent, child);

            /*红黑平衡处理*/
            // case2
            setBlack(child);
            return;
        }
        // 存在两个子树(case1/case2/case3)
        if (hasTowChild(needDel)) {
            Node replace = getMinNode(needDel.right);
            Node replaceRChild = replace.right;
            Node rdParent = parentOf(replace) == needDel ? replace : parentOf(replace);
            // 后继右孩子
            if (parentOf(replace) != needDel) {
                // 后继节点在左面，父节点不是D，则一定没有左孩子，只有右孩子，右子树最小，如果有左孩子，肯定还会继续向左找后继
                addChild(LEFT, replace.parent, replace.right);
            }

            // 删除节点左孩子,两种后继节点，无论哪种，D的左孩子链
            addChild(LEFT, replace, needDel.left);

            // 删除节点右孩子
            if (parentOf(replace) != needDel) {
                addChild(RIGHT, replace, needDel.right);
            }

            // 父节点
            if (root != needDel) {
                int lr = lr(needDel, needDel.parent);
                addChild(lr, needDel.parent, replace);
            } else {
                replaceRoot(replace);
            }

            /*红黑平衡处理*/
            // case1
            if (isRed(replace)) {
                return;
            }

            //case2
            if (isRed(replaceRChild)) {
                setBlack(replaceRChild);
                return;
            }
            // case3
            deleteBalanceFix(rdParent);
        }
        setBlack(root);
    }

    /**
     * 红黑树平衡处理(RD为黑色，因为删除之后如果为黑色则全部是NIl，没有红色可以提升的孩子，需要借助兄弟节点处理)
     * 注意，平衡修复的每一步，除了终修复平衡的case其他case改色，旋转都要保证当前的黑色节点个数不变，只有最后一个case处理，才改色旋转,给少的一侧+1
     *
     * case1 RD的兄弟节点是红色（S如果是红色，必定存在两个非NIl的孩子，因为相对于父节点RD删除之前是黑色，则右侧一定存在非Nil的黑色节点来满足平衡）
     * S涂黑，P图红，P旋转
     * case2 RD的兄弟节点是黑色,并且只有一个红色孩子，并且孩子不在同一侧，通过旋转改变到同一侧，为case3的旋转左准备
     * SL/SR涂黑，S涂红，S旋转
     * case3 RD的兄弟节点存在红色孩子，并且有一个红色孩子在同一侧(另一个孩子不关注，可能是黑，也可能是红)
     * S涂父节点颜色，SL/SR涂黑，P涂黑(中间红两边黑，旋转后两边就都平衡了一个黑色孩子,插入则是中间黑两边红),P旋转
     * case4 如果S,SR,SL,为黑色，P为红色
     * S涂红，P图黑(S一侧少黑色，另一侧多黑色，则让另一侧少一个，同时父节点是红色变成黑色右多一个，经过P的黑色个数原来一样，平衡)
     * case5 如果S,SR,SL,P全是黑色
     * S涂红，RD指向P，返回上一层处理(相当于把多黑色那一侧涂红P的这个子树虽然平衡了，但是对于整棵树来讲经过P的子树都少一个节点，所以把P看做是子树少黑色节点的RD节点(很好理解，递归的思想，等同的对换))
     * */
    private void deleteBalanceFix(Node parent) {
        // 第一次处理，如果出现RD为黑色一定是NIL，后续如果当前子树处理不了，RD改成P向上一层回溯，才会是不为NIl的黑色节点
        Node rd = (Node) NIL;
        int rdOfPlr = lr(rd, parent);
        // rd如果一直是黑色，则不断向上回溯，直到root节点
        while (isBlack(rd) && rd != root) {
            // 因为存在旋转，更改rd所以兄弟会根据循会发生变化
            Node sib = brotherOf(rd, parent);
            int sibOfPLr = lr(sib, parent);
            // case1,去出发case2，注意旋转之后sib的变化，有原来的红色sib的孩子变成了sib
            if (isRed(sib)) {
                // 改色，兄弟多红色，改黑，父亲盖红为了保证黑色数量不变，向缺少黑色孩子一侧旋转一个孩子过去，不改变缺少孩子的现状，保持现在的状态
                setBlack(sib);
                setRed(parent);
                // 向缺少黑色孩子的一侧旋转，也就是DR侧，少了个一个黑色孩子
                if (LEFT == rdOfPlr) {
                    leftRotate(parent);
                } else {
                    rightRotate(parent);
                }
                continue;
            }
            // case2
            if (isBlack(sib) && hasRedChild(sib) && isBlack(childOf(sibOfPLr, sib))) {
                // 红色孩子旋转到同一侧，注意改色，保证原有的S还是原来颜色，孩子颜色也还是原来的颜色
                int childLr = sibOfPLr == LEFT ? RIGHT : LEFT;
                Node redChild = childOf(childLr, sib);
                setBlack(redChild);
                setRed(sib);
                if (LEFT == sibOfPLr) {
                    leftRotate(sib);
                } else {
                    rightRotate(sib);
                }
                continue;
            }

            // case3,平衡修复结束处理
            if (isBlack(sib) && isRed(childOf(sibOfPLr, sib))) {
                Node redChild = childOf(sibOfPLr, sib);

                // 中间颜色不管，通红两边，然后P旋转左右黑色平衡
                setColor(sib, colorOf(parent));
                setBlack(parent);
                setBlack(redChild);
                if (sibOfPLr == LEFT) {
                    rightRotate(parent);
                } else {
                    leftRotate(parent);
                }

                // 平衡修复，结束处理
                break;
            }

            // case4
            if (isRed(parent) && isBlack(sib) && isBlack(sib.left) && isBlack(sib.right)) {
                setBlack(parent);
                setRed(sib);
                break;
            }

            // case5,当前处理不了，滚回上一层，儿子处理不了交给老子
            if (isBlack(parent) && isBlack(sib) && isBlack(sib.left) && isBlack(sib.right)) {
                setRed(sib);
                rd = parent;
                parent = parentOf(rd);
                continue;
            }
        }
    }

    private boolean hasRedChild(Node node) {
        return isRed(node.left) || isRed(node.right);
    }

    private Node childOf(int lr, Node parent) {
        if (lr == LEFT) {
            return parent.left;
        } else {
            return parent.right;
        }
    }

    @Override
    public BSNode root() {
        return root;
    }

    private Node leftOf(Node node) {
        return node.left;
    }

    private Node rightOf(Node node) {
        return node.right;
    }

    private Node parentOf(Node node) {
        return node.parent;
    }

    private Node brotherOf(Node child, Node parent) {
        if (child == parent.left) {
            return parent.right;
        } else if (child == parent.right) {
            return parent.left;
        } else {
            throw new IllegalArgumentException("error child and parent ");
        }
    }

    private boolean isRed(Node node) {
        return (node == NIL) ? false : (node.color == RED);
    }

    private boolean isBlack(Node node) {
        return !isRed(node);
    }

    private int colorOf(Node node) {
        return node == NIL ? BLACK : node.color;
    }

    private void setRed(Node node) {
        node.color = RED;
    }

    private void setBlack(Node node) {
        node.color = BLACK;
    }

    private void setColor(Node node, int color) {
        node.color = color;
    }

    private void replaceRoot(Node node) {
        node.parent = null;
        root = node;
    }


    private class Node implements BSNode<K, V> {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private Node parent;
        private int color;

        public Node(K key, V value, int color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public V value() {
            return value;
        }

        @Override
        public BSNode<K, V> left() {
            return left;
        }

        @Override
        public BSNode<K, V> right() {
            return right;
        }

        @Override
        public BSNode<K, V> parent() {
            return parent;
        }

        @Override
        public String toString() {
            String color = this.color == RED ? "R" : "B";
            return color + value.toString();
        }
    }
}

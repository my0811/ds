package com.yang.ds.algorithm.linkedlist;

import java.util.Random;
import java.util.Stack;


/**
 * 跳跃表的实现
 * 完全采用随机，复杂度可以与红黑树向匹敌，logN ,因为每层抽取的概率是1/2,所以越到上层，出现的概率就是1/2*1/2,相当二分的是一样，上一层是下一层的
 * 1/2
 * <p>
 * 感受：，但是不懂跳跃表，代码从知乎上一个哥们写的，自己拿过来看看，恍然大悟,感谢博主的分享，代码自己偷偷收藏
 * <p>
 * 突然感觉特么的，红黑树真是比这个东东复杂不知多少倍啊，跳跃表，只需要掌握链表，栈自己脑子想想代码基本就能弄出来，红黑树得600行代码，而且不能保证
 * 每次写完都没bug
 */
public class SkipList {

    // 拍硬币决定概率，1/2,random 随机 ps:bound the upper bound (exclusive)
    private static final int RATE = 2;
    //顶层头节点
    private Node head;

    //相邻两层元素个数的比例
    private int rate = RATE;

    //跳跃表层数
    private int level;

    //所有层节点个数
    private int size;

    //true表示正序，false表示逆序
    private final boolean order;

    //随机数
    private Random random;

    //保存查询时遍历的节点
    private Stack<Node> stack;

    //节点类
    private static class Node {

        private Comparable comparable;

        //同一层的右边节点
        private Node right;

        //下一层的对应节点
        private Node down;

        public Node(Comparable comparable) {
            this.comparable = comparable;
            this.right = null;
            this.down = null;
        }
    }

    public SkipList(int level, boolean order) {
        this.level = level;
        this.size = 0;
        this.size = 0;
        this.order = order;
        this.random = new Random();
        this.stack = new Stack<Node>();

        // 哨兵模式，初始化一个空头,头节点的值默认为null
        this.head = new Node(null);

        // 生成一个纵向的链,从上到下的链
        Node temp = head;
        for (int i = 1; i < level; i++) {
            temp.down = new Node(null);
            // 下一层链表关联上
            temp = temp.down;
        }
    }

    /**
     * 查询元素，从纵向链表的head开始向右，向下，歌词啊哈哈，进行链表遍历
     * 1. 查找的是插入位置，不判断是否找到，查询方法，自己判断.right是否需指定值相等，如果相等则找到，否则没有找到,(不包含相等元素，相等元素覆盖)
     * 2. 之所以返回相等元素之前的一个元素，是想把重复元素覆盖掉，.right赋值的时候可以直接覆盖(此程序中没有，用的是判断，也可以直接强覆盖)
     * 查询元素，自顶向下
     * 正序时，返回底层【小于】给定值的最大的节点，包含头节点
     * 逆序时，返回底层【大于】给定值的最小的节点，包含头节点
     * <p>
     * ps: 链表的最后一层不放入栈中，栈只保存从第二层链表开始的路径
     */
    private Node search(Comparable comparable) {
        // 每次遍历清空栈，保存新的路径
        stack.clear();

        //从顶层开始
        Node temp = head;
        while (true) {
            while (temp.right != null) {
                /*
                * 正序，从小到大,遇到比目标元素大的停止，开始向下引用的方向走,目标遍历到的node是比当前node小，并且是最大的
                * 这样就可以向下走，就可以找到底层的链表，插入位置了，相当于是红黑树中左子树最大的节点，找到左子树，然后向右递归
                * 找到最大，这个相当于就是一个前继节点，前继节点之后插入
                *
                * */
                if (order && temp.right.comparable.compareTo(comparable) >= 0)
                    break;
                // 逆序，从大到小
                if (!order && temp.right.comparable.compareTo(comparable) <= 0)
                    break;
                // 继续向右移动，右侧要么是比当前大，要不就是比当前小
                temp = temp.right;
            }
            // 上一层链表找到了合适位置开始向下找,最后一层插入的链表，没有down引用
            if (temp.down == null)
                break;

            // 保存遍历路径，除了最后一层真正存储数据的链表
            //
            stack.push(temp);

            // 向下一层移动
            temp = temp.down;
        }
        return temp;
    }

    /**
     * 添加元素
     * 若元素已存在，则返回，保证无重复元素
     */
    public void insert(Comparable comparable) {
        Node temp = search(comparable);
        //元素已存在
        if (temp.right != null && temp.right.comparable.compareTo(comparable) == 0)
            return;
        Node node = new Node(comparable);
        Node other;

        //根据随机数，自底向上添加每层的新节点
        while (true) {
            //当前层添加
            node.right = temp.right;
            temp.right = node;

            // 抛硬币决定上一层是否添加索引,随机数0和1之间产生，每次都是1/2,决定是否向上一层链表插入
            if (random.nextInt(rate) != 0 || stack.isEmpty()) {
                break;
            }

            // 上一层node取出
            temp = stack.pop();

            // 上一层增加一个node，down指向当前层节点
            other = node;
            node = new Node(comparable);
            node.down = other;
        }
        size++;
    }

    /**
     * 查找
     */
    public Comparable get(Comparable cmp) {
        Node result = search(cmp);
        if (result != null && result.right != null && result.right.comparable.compareTo(cmp) == 0) {
            return result.right.comparable;
        }
        return null;
    }

    //删除元素
    //若元素不存在，则返回，否则删除所有层中包含的元素
    public void delete(Comparable comparable) {
        Node temp = search(comparable);
        //元素不存在
        if (temp.right == null || temp.right.comparable.compareTo(comparable) != 0)
            return;
        while (true) {

            //当前层的元素不存在,索引都是随机的很可能没有指定的值，所以索引如果没有了，说明再上一层肯定也没有
            if (temp.right == null || temp.right.comparable.compareTo(comparable) != 0)
                break;

            //从底层开始，依次删除每层的元素
            temp.right = temp.right.right;

            //到达顶层结束
            if (stack.isEmpty())
                break;

            // 继续删除上一层
            temp = stack.pop();
        }
        size--;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        SkipList skipList = new SkipList(4, true);
        skipList.insert(5);
        skipList.insert(7);
        skipList.insert(9);
        skipList.insert(18);
        System.out.println(skipList.get(6));
        System.out.println(new Random().nextInt(2));
    }
}

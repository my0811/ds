package com.yang.ds.algorithm.queue;

/**
 * 双端队列 (两端都可以增加删除的灵活队列，其中一端可以当做栈使用,头添加尾部出队可以作为队列使用)
 * <p>
 * I.灵活使用
 * 1. 一端可以当做栈使用
 * 2. 一端添加另一端删除，当做队列使用
 * II.
 * 1.双端队列入队元素，头部元素(addFirst)头部指针向左移动,删除头部指针右移 --
 * 2.双端队列尾部入队,尾部元素(addLast)尾部指针右移动,删除尾部元素指针左移 -- 互为相反
 * 3.头部下标指向的是队列中的第一位元素，尾部下标指向的是"下一个尾部插入的位置"
 * III.
 * 1. 当元素下标移动到边界的时候，我们要在逻辑上把数组看似成一个环状的结构，也就是可以循环的
 * IIII.
 * 核心算法
 * 1.取模运算,最低四位&运算，实现数组边界循环
 * --------添加元素--------------
 * <p> 1头部添加,2尾部添加
 * >>----------------------->-----┐
 * ┌---------------------<----┐   |
 * ∨                          |   |  ">>"代表数组指针从起始位置向右移动,"<<"代表数组指针向左移动（越界循环到另外一端）
 * ┌>t/h → t → t    h ← h ← h |   |  1. t代表尾部指针，尾部指针一直指向的是下一个尾部需要插入的位置
 * |   0   1   2    3   4   5 |   |  2. 所以如果头部指针和尾部指针相等，1.队列为空，2队列满了
 * |   A   B   []   D   E   F |   |  3. 所以在判断队列满之前，是后判断，因为尾指针一直是指向的下一个插入位置的空元素
 * |                          |   |  4. 这个图老子画的实在是太NB了
 * |  <<----1-h->-------------┘   |
 * |                              |
 * └-------2-t----<---------------┘
 * <p>
 * --------删除元素--------------
 * <p>
 * 1头部删除，2尾部删除
 * ┌-t|h  ←t  ←t   h  →h → h->-┐
 * |┌>0   1    2   3   4   5<--|--┐
 * || A   B   []   D   E   F   |  |
 * ||                          |  |
 * |└-----1-h----<-------------┘  |
 * |                              |
 * └------2-t-------->------------┘
 * <p>
 * --------------扩容-------------------
 * 扩容前
 * t|h  t   t   t|h  h   h                  此时的数组tail=h 队列已经满了(tail 指向的下一个插入的元素,所以我们是后判断tail和head是否相等)
 * 0   1    2   3   4    5                  所以数据出现这种情况是最后一次插入完成了，而此时的数组刚好满了
 * A   B    G   D   E    F                  这也是为什么tail设置为指向下一个插入的元素的位置的巧妙的地方了
 * 扩容后
 * h              t
 * 0  1  2  3  4  5  6  7  8  9  10  11     1.扩容后把头部数据从左到右的顺序放到新数组前面
 * D  E  F  A  B  G  [] [] [] [] []  []     2.然后把尾部数据按照从左到右顺序放到新数组头部数据元素后面
 * <p>                                      3.头指针指向新数组的(第一个位置),尾指针指向(原来数据长度)的(最后一个位置)
 * <p>                                      4.后面空出来的数据又恢复了以前，头部插入还继续向最后面添加，数组扩容完成
 *                                          5.如果原封不动的复制到新数组，head 和tail就不好安置了，原位置不动，head已经和tail重合了，没法玩了
 *                                          6.数组长度-索引=包含开始位置到末尾的所有元素"[]"左右闭区间
 *                                          7.索引+1=从0到索引位置全部的元素"[]"左右闭区间
 * 头部入队(指针左移，初始化的时候循环到数组最后面)
 */

public class DoubleEndedArrayQueue {

    //默认数组容量
    private final int DEFAULT_CAPACITY = 16; //2^4 之所以是2^n是为了取模做&运算使用

    // 扩容基数
    private final int EXPAND_BASE = 2;  // 这个要和2 的n次方不相违背

    // 存放数据数组，用数组实现队列
    private Object[] elements;

    // 数组的最大长度
    private int maxSize;

    // 头指针
    private int head;

    // 尾部下标
    private int tail;

    public DoubleEndedArrayQueue() {
        maxSize = DEFAULT_CAPACITY;
        elements = new Object[maxSize];
        head = 0;
        tail = 0;// 双端队列只有在队列为空的情况下，头指针和尾指针才相等，如果后面又出现相等情况，说明队列满了
    }


    // 实现数组的取模，实现数组下标的循环，行程环状移动
    public int getMod(int index) {
        // 左边越界 -1 循环到数组的最后一个元素的坐标
        if (index < 0) {
            index = index + maxSize;
        }
        // 右边越界，index=elements.length 循环到数组的第一个元素的下标
        if (index >= maxSize) {
            index = index - maxSize;
        }
        return index;
    }

    /**
     * 头部添加
     */
    public void addFirst(Object data) {
        // 头部插入，指针左移动,注意循环和边界,与运算可以参考位运算中的说明 com.yang.ds.algorithm.bit
        head = (head - 1) & (maxSize - 1);
        elements[head] = data;
        // 判断是否需要扩容,头部指针和尾部指针相等，说明队列满了，
        if (head == tail) {
            expend();
        }
    }

    /**
     * 头部删除
     */
    public Object removeFirtst() {
        Object removeObj = null;
        // 判断是否是空队列,因为不可能是满的队列，因为满的队列在添加元素之后就会判断如果队列满了就扩容了
        if (head != tail) {
            removeObj = elements[head];
            elements[head] = null;
            head = (head + 1) & (maxSize - 1);
        }
        return removeObj;
    }

    /**
     * 尾部添加
     */
    public void addLast(Object data) {
        elements[tail] = data;
        // 表示，tail 尾指针是指向的下一个尾部插入的位置
        tail = getMod(tail + 1);
        if (tail == head) { // 扩容
            expend();
        }
    }

    /**
     * 尾部删除
     */
    public Object removeLast() {
        Object removeObj = null;
        if (tail != head) {
            // 因为尾指针指向的是下一个插入的位置，所以要先进行指针移动
            tail = (tail - 1) & (maxSize - 1);
            removeObj = elements[tail];
            elements[tail] = null;
        }
        return removeObj;
    }

    /**
     * peek first
     */
    public Object peekFirst() {
        if (tail != head) {
            return elements[head];
        }
        return null;
    }

    /**
     * peek last
     */
    public Object peekLast() {
        if (tail != head) {
            // 注意尾部指针指向的是尾部下一个需要插入的位置，尾部插入是右移，所以要先-1
            // 注意指针移动一定要考虑边界问题，一定要取模
            // 注意这个里面只是查看元素，不需要删除，一定不要指针变量上直接操作
            return elements[(tail - 1) & (maxSize - 1)];
        }
        return null;
    }

    /**
     * 数组扩容
     * 1. 数组扩容就是
     */
    private void expend() {
        // 扩容大小
        int newSize = maxSize << 1; // 注意位移方向，向左乘2^n，向右除2^n
        Object[] newObjArr = new Object[newSize];
        // 获取前端元素个数 ，计算方式看类注释的图
        int hc = maxSize - head;
        // 获取后端元素个数,计算方式看类注释的图
        int tc = head;
        // 先复制头部数组   原数组,开始索引,新数组,新数组开始位置,复制原数组元素个数
        System.arraycopy(elements, head, newObjArr, 0, hc);// 把前端插入数据复制到新数组的前面部分
        // 先复制尾部数组   原数组,开始索引,新数组,新数组开始位置,复制原数组元素个数
        System.arraycopy(elements, 0, newObjArr, hc, tc);
        // 前端指针指向0
        head = 0;
        tail = maxSize;
        this.maxSize = newSize;
        this.elements = newObjArr;
    }

    private boolean isEmpty() {
        return tail == head;
    }

    /**
     * 这个也是比较牛叉的算法，我们的尾指针-头指针，在取模刚好就是当前元素的个数
     */
    private int size() {
        return (tail - head) & (maxSize - 1);
    }

    private int maxSize() {
        return maxSize;
    }

    public static void main(String[] args) {
        // 栈使用
        stackTest();
        //单向队列使用
        //oneWayQueueTest();
        //扩容
        expendTest();
    }

    private static void stackTest() {
        DoubleEndedArrayQueue queue = new DoubleEndedArrayQueue();
        /**
         * 栈的测试，相当于两边都是栈，遵循LIFO
         * */
        queue.addFirst(1);
        queue.addFirst(2);
        queue.addLast(3);
        queue.addLast(4);
        System.out.println(queue.peekFirst());
        System.out.println(queue.peekLast());
        System.out.println("size:" + queue.size());
    }

    private static void oneWayQueueTest() {
        DoubleEndedArrayQueue queue = new DoubleEndedArrayQueue();
        queue.addFirst(1);
        queue.addFirst(2);
        while (!queue.isEmpty()) {
            System.out.println(queue.removeLast());
        }
    }

    private static void expendTest() {
        DoubleEndedArrayQueue queue = new DoubleEndedArrayQueue();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                queue.addFirst(i);
            } else {
                queue.addLast(i);
            }
            if (i == 14) {
                System.out.println(queue.maxSize());
            }
        }
        System.out.println(queue.maxSize());
    }
}

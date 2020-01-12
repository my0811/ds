package com.yang.ds.algorithm.heap;

import java.util.Arrays;

/**
 * 堆结构对索引的获取
 * 根节点(0)一定是优先级最高的
 * 1.获取[左孩子]为2*index+1
 * 2.获取[右孩子]为2*index+2
 * 3.获取[父节点]为(index-1)/2
 * 4.获取到最后一个非叶子节点 size(总的节点数)/2
 *
 * 完全二叉树转换数组存储的形式表现，左侧是平衡二叉树，从root节点从0开始编号，从左到右，连续读取，不能出现
 * 不连续，如果不连续就不满足平衡二叉树了，也就不满足堆的数据结构,这种数据结构是堆的插入和删除遵循这个规则肯定
 * 会形成这样的完全二叉树，只不过一般堆都是用数组来实现的
 *
 *
 * -----------------------二叉树转换数组存储，实现堆数据结构---------------------------------------
 *                  root节点
 *         9<----------------9|0 -->数组下标
 *        [0]                8|1
 *      8    3     ------->  3|2
 *     [1]  [2]              2|3
 *    2   4    1             4|4
 *  [3]  [4]  [5]            5|5
 *
 * */

// 通过实现Comparable接口，来实现大顶堆，还是小顶堆
public class Heap<K extends Comparable<? super K>, V> {

    private static final int MAX_CAPACITY = 1 << 30;

    // 定义平衡二叉树的对应的转换数组
    private Node<K, V>[] nodes;

    private int size;

    public Heap(int initSize) {
        if (initSize <= 0) {
            throw new IllegalArgumentException("init size is illegal " + size);
        }
        nodes = new Node[initSize];
        size = 0;
    }

    public Heap() {
        this(10);
    }

    /**
     * TODO 扩容
     * */
    private void resize() {
        int minCapacity = size + 1;

        // 扩容，扩容原来数组的两倍长度
        if (minCapacity - nodes.length > 0) {
            int newCap = nodes.length << 1;
            if (newCap - MAX_CAPACITY > 0) {
                throw new IllegalArgumentException("heap is over flow" + newCap);
            }
            nodes = Arrays.copyOf(nodes, newCap);
        }
    }

    /**
     * 添加
     * 1. 直接添加到最后叶子节点的后面，也就是数组的最后一个元素索引+1的位置
     * 2. 然后以插入位置的开始向上筛选，把优先级高的不断向上调整到root节点
     * @param key
     * @param value
     * */
    public void add(K key, V value) {
        // 扩容
        resize();

        // step1 添加到树的叶子节点的最后一个位置，也就是数组存在效值的最后一位
        int curIdx = size;
        nodes[curIdx] = new Node(key, value);

        // step2 向上交换，如果父节点优先级高则上移，以此向上回溯到root节点
        moveUp(curIdx);
        size++;
    }

    /**
     * 获取堆顶元素
     * */
    public V top() {
        return (V) nodes[0];
    }

    public void reset() {
        size = 0;
    }

    /**
     * 删除，删除堆顶的元素，不支持对应的key删除，堆是弱排序，很难查找对应的key
     * 1.把root节点用树的最后的叶子节点替换，也就是数组的最后一个位置的节点
     * 2.然后再用root节点替换的节点，不断向下筛选，把优先级最高的拉取到root节点
     * */
    public V remove() {
        // 空堆判断
        if (size <= 0) {
            return null;
        }
        // 平衡二叉树的最后一个叶子，最右面的叶子替换root节点，就是数组最后一个元素的位置
        Node<K, V> tmp = nodes[0];
        nodes[0] = nodes[size - 1];
        nodes[size - 1] = null;// 移除掉数组最后位置的引用
        // 需要先把数组的大小改变，因为移动交换，是根据我们节点转移删除之后的树进行调整
        size--;
        moveDown(0);
        return tmp.value;
    }

    /**
     * 修改,内部使用，我们需要知道数组的具体索引，然后去修改值，修改时需要
     * 判断，是否需要上移，或者下移，让优先级最高的排在最前面
     * @param idx
     * @param key
     * @param value
     * */
    private void set(int idx, K key, V value) {
        // 空堆的判断
        if (size <= 0) {
            return;
        }
        // 获取修改的node
        Node<K, V> sNode = nodes[idx];
        if (null != sNode) {
            int cmp = key.compareTo(sNode.key);
            nodes[idx] = new Node(key, value);
            // 修改之后的优先级高则上移调整
            if (cmp > 0) {
                moveUp(idx);
            } else if (cmp < 0) {// 如果优先级比原来的低，则下移
                moveDown(idx);
            }
        }
    }

    /**
     * 向上移动，下面优先级比上一层父节点高的向上移动，上层父节点向下移动
     * 不断向上推进，索引
     *@param idx 需要调整的开始位置
     * */
    private void moveUp(int idx) {
        // 对于数组的位置移动，大体的思路都是，先把数据移动，最后空出了一个合适的位置插入即可
        Node<K, V> nNode = nodes[idx];
        int curIdx = idx;
        while (curIdx > 0) {// 如果大于0，则父节点不可能小于0越界
            int pIdx = (curIdx - 1) / 2;
            Node<K, V> parent = nodes[pIdx];
            int cmp = parent.key.compareTo(nNode.key);
            // 父节点如果优先级比自己高，则不需要移动
            if (cmp > 0) {
                break;
            }
            // 移动元素,只要父节点的优先级小于自己，则把父节点数据下移,让优先级更大的节点向上移动留出空位
            nodes[curIdx] = parent;
            curIdx = (curIdx - 1) / 2;
        }
        // 可能curIdx就没有动，还是原来的index，重复赋值也不影响什么，没啥毛线影响
        nodes[curIdx] = nNode;
    }

    private void swap(int idx1, int idx2, Node[] nodes) {
        Node<K, V> node1 = nodes[idx1];
        Node<K, V> node2 = nodes[idx2];
        Node<K, V> tmp = node1;
        nodes[idx1] = node2;
        nodes[idx2] = tmp;
    }

    /**
     * 向下移动，针对于删除的操作，删除只进行在堆顶端，先删除root，把root下面的优先级高的向上移动
     * 不断向上推进，索引
     *@param idx 需要调整的开始位置
     * */
    private void moveDown(int idx) {
        Node<K, V> rNode = nodes[idx];
        int curIdx = idx;
        // 循环临界值，当前节点必须有孩子,size/2是一个没有孩子的节点，是临界值 size/2-1是最后一个非叶子节点
        while (curIdx < size / 2) {
            // 孩子索引获取
            int leftChildIdx = 2 * curIdx + 1;
            int rightChildIdx = 2 * curIdx + 2;
            int maxIdx = leftChildIdx;
            // 一定要加上这个判断，因为可能没有右孩子,右左孩子的情况，但是可能存在孩子没有左孩子的情况
            // 注意不能=size，因为等于没有意义，肯定是空了，因为删除了最后一个元素已经是null了没有意义
            if (rightChildIdx < size && nodes[rightChildIdx] != null) {
                Node<K, V> rightChild = nodes[rightChildIdx];
                Node<K, V> leftChild = nodes[leftChildIdx];
                int cmp = leftChild.key.compareTo(rightChild.key);
                maxIdx = cmp > 0 ? leftChildIdx : rightChildIdx;
            }
            // 如果当前父节点就是最大，则不予处理
            Node<K, V> maxNode = nodes[maxIdx];
            int cmp = rNode.key.compareTo(maxNode.key);
            if (cmp >= 0) {
                break;
            }
            // 获取左孩子或者右孩子优先级高的索引
            nodes[curIdx] = nodes[maxIdx];
            curIdx = maxIdx;
        }
        nodes[curIdx] = rNode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (size <= 0) {
            sb.append("empty");
        }
        for (int i = 0; i < size; i++) {
            sb.append(nodes[i].toString()).append(" ");
        }
        return sb.toString();
    }

    /**
     * 1. 无序数组调整成一个堆
     * 2. 堆顶和堆尾替换
     * 3. 逻辑上隔离堆尾，也就是数组最后一个元素，继续从1开始
     * */
    public void sort(Node<K, V>[] arr) {
        // 无序数组调整成堆结构
        // 从最后一个非叶子节点(至少有一个孩子，如果只有一个孩子一定是左孩子),一直按顺序编号递减到root节点
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            // 需要循环反复的调整，因为调整了最底层的，如果回溯到上一层，调整完，底层的规则可能变化了，所以
            // 每次都要从最新的调整位置向下再调整一遍
            moveDown(i, arr.length, arr);
        }
        // 堆顶的元素和堆位的元素交换位置，也就是数组0和数组最后索引交换位置，这样0位置的是优先级最高的，所以排到了数组
        // 最后的位置，相当于冒泡，找出来了一个最大值
        for (int j = arr.length - 1; j > 0; j--) {// 最后一个不需要调整了，已经有所有数据-1的元素排序完了，最后一个无需操作了就
            // 0和尾交换，堆顶，和堆尾
            swap(0, j, arr);
            // 0尾交换完成，把-1后长度的数组继续调整成合法的堆
            moveDown(0, j, arr);
        }
    }

    /**
     * 1.对结构调整,区别于删除时候的moveDown方法，因为堆添加的时候走的是moveUp()方法,是因为已经是堆的
     * 结构，只需要从插入位置的节点向父节点方向逐层回溯即可，但是如果对于无序数组调整成堆的话，我们调整完
     * 一个位置的同时还可能下面的孩子的位置又出现不满足堆的特性，所以无论调整到哪一层节点，都需要从当前节点
     * 再向下重新调整一下
     * 2. 因为删除走的moveDown也是因为已经是堆结构，只需要从root节点直接走到最后的叶子节点调整一遍即可
     * 不会出现调整完，下一层节点可能不满足堆结构
     *
     * */
    private void moveDown(int idx, int arrLen, Node<K, V>[] arr) {
        Node<K, V> tmp = arr[idx];
        int curIdx = idx;
        // 循环从传入的索引开始，获取左孩子，如果存在右孩子，循环step跳转到右孩子的做孩子，以此类推，从左到右处理
        for (int k = 2 * curIdx + 1; k < arrLen; k = 2 * k + 1) {
            // 如果存在右孩子，才进行左右孩子的比较,否则默认就是左孩子,不能等于size,等于是最后一个最大值元素的边界
            if (k + 1 < arrLen && arr[k + 1] != null) {
                Node<K, V> rightNode = arr[k + 1];
                Node<K, V> leftNode = arr[k];
                int cmp = leftNode.key.compareTo(rightNode.key);
                if (cmp < 0) {// 处理左兄弟节点大，还是右兄弟节点值大,指针k就是哪个
                    k++;
                }
            }
            // 判断是否索引需要向孩子节点方向移动
            Node<K, V> maxNode = arr[k];
            int cmp = tmp.key.compareTo(maxNode.key);
            // 优先级比孩子节点高，无序调整，跳出循环
            if (cmp >= 0) {// 因为调整是从下到上，或者从上到下，除非上一层有调整，否则，下一层已经调整过的，就不会出现不合法的情况
                break;
            }
            arr[curIdx] = arr[k];
            curIdx = k;
        }
        // 因为循环中已经把向下优先级子孩子的数据上提了，所以循环结束，curIdx最终就是tmp需要交换的位置
        // 这样做的好处就是没有每次比较都交换数组两个位置的元素，减少了交换，只在最后一次交换
        arr[curIdx] = tmp;
    }

    public int size() {
        return size;
    }

    // 定义node
    public static class Node<K, V> {
        /**具有比较性的key*/
        private K key;

        /**存储的value值*/
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public String toString() {
            return "[" + key + "," + value + "]";
        }
    }

    public static void main(String[] args) {
        Heap<Integer, String> heap = new Heap<>(5);
        heap.add(5, "y");
        heap.add(2, "y");
        heap.add(3, "y");
        heap.add(8, "y");
        heap.add(4, "y");
        heap.add(1, "y");
        System.out.println(heap);
        heap.set(1, 9, "K");
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);
        heap.remove();
        System.out.println(heap);

        /*堆排序*/
        Node<Integer, String>[] nodes = new Node[4];
        nodes[0] = new Heap.Node<>(1, "y1");
        nodes[1] = new Heap.Node<>(5, "y5");
        nodes[2] = new Heap.Node<>(2, "y2");
        nodes[3] = new Heap.Node<>(3, "y3");
        heap.sort(nodes);
        System.out.println(Arrays.toString(nodes));

    }
}

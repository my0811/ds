package com.yang.ds.algorithm.heap;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 堆结构对索引的获取
 * 根节点(0)一定是优先级最高的
 * 1.获取[左孩子]为2*index+1
 * 2.获取[右孩子]为2*index+2
 * 3.获取[父节点]为(index-1)/2
 * 4.获取到最后一个非叶子节点 size(总的节点数)/2-1
 * 5.添加、删除复杂度基本在O(logN)之间，因为是树的高度
 * 6.堆排序复杂度在O(NlogN)>O(N)
 *
 * 完全二叉树转换数组存储的形式表现，左侧是平衡二叉树，从root节点从0开始编号，从左到右，连续读取，不能出现
 * 不连续，如果不连续就不满足平衡二叉树了，也就不满足堆的数据结构,这种数据结构是堆的插入和删除遵循这个规则肯定
 * 会形成这样的完全二叉树，只不过一般堆都是用数组来实现的
 *
 * [完全二叉树]，不同于满二叉树，完全二叉树要求最后一层和倒数第二层最后一个节点可以不是满一定右左右孩子,但是一定是连续的
 * [满二叉树]，每一层都不能断，每个节点都必须有左右子节点
 *
 * 堆结构，只要从一开始插入的时候，就不断调整符合堆结构，后续在插入是一样的，因为二叉树的最底层已经符合了二叉对的
 * 性质，只要不断的递归到root就可以了，但是对于添加来讲，每次都是从数组的尾部添加O(1),在加上一个二叉树的高度O(logN)
 * 的复杂度元素位置交换,就可以保证了堆的特性，并且root一定是优先级最高的，符合了二叉堆的性质
 * 所以对于数组的扩容来讲，和插入来讲，宏观上就是无序数组的插入，扩容也不用向双端队列那样更改head和tail的索引，直接复制
 * 扩容即可
 *
 * 堆排序复杂度:
 *
 * 1. 如果[给定一个无序数组在无序数组之上]调整成堆N/2logN，然后把堆所有数据都排序到数组中，就是相当于不断的堆顶移除,复杂度是
 * NlogN,所以总体复杂度就是:N/2logN+NlogN 所以总体复杂度就是:NlogN
 *
 * 2. 如果不提供一个数组，自己需要附加一个数组（作为堆），要把之前的所有数据添加到数组中，也就是把所有数据进行堆添加
 * 因为堆添加复杂度是logN，所以所有数据添加到堆就是NlogN,然后添加完，还需要把说有数据堆顶移除，删除操作也是logN，所以全部
 * 数据都移除完就是NlogN,所以总体复杂度就是2NlogN
 * 总结:堆的排序给定数组，和不给定数组复杂度都是NlogN级别，但是前提条件就是必须得有一个数组作为堆，如果不提供数组，自己
 * 得构建一个数组作为堆，所以堆排序虽然复杂度比较低，但是前提得有数组，所以空间复杂度就多了，必须有数组存储排序数据
 *
 *
 *
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
        moveDown(0, size - 1);
        size--;
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
                moveDown(idx, size);
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
            curIdx = pIdx;
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
     * ps: 注意判断条件，<size/2 就满足了最后一个非叶子节点，不用-1，size的长度在外面动态控制，比如
     * 如果是删除，数组长度可以穿最后一个元素的坐标，比数组长度小1，相当于要删除元素，调整完然后size在--
     *
     *
     * 向下移动，针对于删除的操作，删除只进行在堆顶端，先删除root，把root下面的优先级高的向上移动
     * 不断向上推进，索引
     * 就是父节点、两个孩子节点，三者取最大，然后父节点和孩子交换位置，保证父节点优先级比两个孩子高
     *@param idx 需要调整的开始位置
     * */
    private void moveDown(int idx, int len) {
        Node<K, V> rNode = nodes[idx];
        int curIdx = idx;
        // 循环临界值，当前节点必须有孩子,size/2是一个没有孩子的节点的临界值 (size/2-1)是最后一个非叶子节点
        while (curIdx < len / 2) {// 注意最后一个非叶子是size/2-1
            // 孩子索引获取
            int leftChildIdx = 2 * curIdx + 1;
            int rightChildIdx = 2 * curIdx + 2;
            // 获取左孩子或者右孩子中优先级最大的一个孩子
            int maxIdx = leftChildIdx;
            //1. 一定要加上这个判断，因为可能没有右孩子,有左孩子的情况，但是不可能存在左右孩子都没有，因为获取的是没有孩子节点之前的
            //2. 注意不能=size，因为等于没有意义，肯定是空了，因为删除了最后一个元素已经是null了没有意义
            //3. 因为=size也越界了，数组最大的索引是size-1，但是可能存在节点没有右孩子，那么size就不会包括右孩子的索引，所以可能数组越界
            if (rightChildIdx < size && nodes[rightChildIdx] != null) {
                Node<K, V> rightChild = nodes[rightChildIdx];
                Node<K, V> leftChild = nodes[leftChildIdx];
                int cmp = leftChild.key.compareTo(rightChild.key);
                maxIdx = cmp > 0 ? leftChildIdx : rightChildIdx;
            }
            // 如果当前父节点就是最大，则不予处理
            Node<K, V> maxNode = nodes[maxIdx];
            // 当前节点和左右孩子中最大的判断
            int cmp = rNode.key.compareTo(maxNode.key);
            if (cmp >= 0) {
                break;
            }
            // 获取左孩子或者右孩子优先级高的索引(孩子中存在更高的优先级，向跟高优先级的移动)
            // 因为二叉堆的特性是，父节点的优先级一定要高于自己的所有孩子
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
        /**
         * 1.这个循环的处理要用递归的思想去理解，首先第一步调整是从最后一个非叶子节点开始的，只要这个节点把父节点
         * 和两个孩子节点的优先级调整好，就可以保证这个一个三个节点的小子树是符合堆的结构
         *
         * 2. 然后i--,就是说明次用这个节点开始直接到root节点，每层的每个节点的都要用这种策略，把每个子树的平衡调整好，但是有个问题就是当调整到上一层
         * 节点的时候，上一层节点可能存在向删除这样的问题，删除的逻辑是把最小的替换到root，然后从root出发向下比对，但是前提
         * 必须是一个符合二叉堆的特性(每个父节点都比自己的两个孩子大)，所以说调整到上一层的时候，就相当于堆删除的逻辑，必须
         * 从上一层那个操作位置开始向下递归到叶子节点都调整一遍
         *
         * 3. i-- 单表这每一层都是从左到右的方向调整每一个子树，子树然后再递归到叶子处理，最后到root就平衡处理完了
         *
         * */
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            /*
             从最后一个存在孩子的节点，按照顺序从左到右每一层都处理一遍，先处理底层，底层符合堆结构在调整每层
            的每一个存在孩子的节点，保证最一层存在孩子节点符合堆结构，然后每层处理完回溯到上一层调整，更上一层的处理
            就是删除的逻辑，从处理位置递归到底层叶子，因为最底层处理完一定是符合堆结构了,所以这里叫moveDown()就是删除
            逻辑的递归
            * */
            moveDown(i, (arr.length), arr);
        }
        // 堆顶的元素和堆位的元素交换位置，也就是数组0和数组最后索引交换位置，这样0位置的是优先级最高的，所以排到了数组
        // 最后的位置，相当于冒泡，找出来了一个最大值
        for (int j = arr.length - 1; j > 0; j--) {// 最后一个不需要调整了，已经有所有数据-1的元素排序完了，最后一个无需操作了就
            // 0和尾交换，堆顶，和堆尾
            swap(0, j, arr);
            /*
             *  0尾交换完成，把-1后长度的数组继续调整成合法的堆
             *  相当于经过过了第一个调整已经把无序数组转换成了一个堆，swap(0, j, arr)相当于就是
             *  不断删除堆顶，删除堆顶然后对堆进行调整，与普通的删除堆顶操作一样，只不过排序要把
             *  所有的数据都移除，因为要排到数组中,所以逻辑上把数组的长度-1，因为每次排出了一个优先级
             *  最高的在数组尾部
             * */
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
        /*
         * 循环从传入的索引开始，获取左孩子，如果存在右孩子，循环step跳转到右孩子的做孩子，以此类推，(从左到右处理)
         * 相当于与堆删除堆顶的逻辑是一样的，只不过指定了idx，从哪一个位置开始
         * 左孩子2*idx+1,右孩子为(2*idx+1)+1,就k+1，更下一层的左孩子就是2*k+1
         * */
        for (int k = 2 * curIdx + 1; k < (arrLen / 2); k = 2 * k + 1) {
            // 比如是删除，最有一个右孩子可能就是数组的最后一个位置，但是删除之后元素-1，所以要判断小于数组长度
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

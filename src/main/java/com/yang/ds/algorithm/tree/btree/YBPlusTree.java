package com.yang.ds.algorithm.tree.btree;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;
import com.yang.ds.algorithm.utils.YUtils;

import java.io.Serializable;
import java.util.*;

/**
 * B树规则(m阶,m>=2)
 *
 *  1.根结点至少有两个孩子
 *  2.每个中间节点都包含k-1个元素和k个孩子，其中 (m/2 <= k <= m)
 *  3.每一个叶子节点都包含k-1个元素，其中 (m/2 <= k <= m)
 *  4.所有的叶子结点都位于同一层。
 *  5.每个节点中的元素从小到大
 *  排列，节点当中k-1个元素正好是k个孩子包含的元素的值域分划。
 *
 * B+树规则(m阶)
 * B+树只不过是B-树的变种，大体规则还都是B-树的规则，只不过做了一小点调整，B-树的规则限制，比如分裂等一些规则都是适用，B+树主要有一下三点不同:
 *
 *  1.非叶子节点不保存数据，只用来索引，所有数据都保存在叶子节点。
 *  2.所有的叶子结点中包含了全部元素的信息，及指向含这些元素记录的指针，且叶子结点本身依关键字的大小自小而大顺序链接。
 * */
// k 的类型必须要实现比较接口，并且比较接口的泛型类型也是K，super为下边界，extends为上边界，继承可以向下转型，但是不能向上转型，父类引用指向子类实现
// 泛型类返回值不用在用泛型的表示，因为整个类的泛型都是一样的，都已经在声明类的时候声明了
public class YBPlusTree<K extends Comparable<? super K>, V> implements Serializable {

    public enum RangePolicy {
        EXCLUSIVE, INCLUSIVE
    }

    /**默认的B+树的阶为128，这个具体需要
     * */
    private static final int DEFAULT_DEGREE = 5;
    /**B树阶的最小限制为2
     * */
    private static final int MIN_DEGREE = 2;
    /**
     * 分支个数因数，也就是我们说的数的阶
     * */
    private int degree;
    /**
     * B+树，root节点
     */
    private Node root;

    /**
     * 初始化，无参构造初始化为默认的阶 m=DEFAULT_DEGREE
     * */
    public YBPlusTree() {
        this(DEFAULT_DEGREE);
    }

    public YBPlusTree(int degree) {
        // b树的最小阶不能小于2个
        if (degree <= MIN_DEGREE)
            throw new IllegalArgumentException("Illegal degree: " + degree);
        this.degree = degree;
        // 默认初始化是叶子节点，插入只能在叶子节点上进行，叶子节点分裂之后才会形成B树
        root = new LeafNode();
    }

    /**
     * 查找数据，返回数据V对象
     * @param key 查找数据的key
     * @return key所对应的数据
     * */
    public V search(K key) {
        return root.getValue(key);
    }

    /**
     * 范围查找，因为B+树所有的数据都存在到了叶子节点，并且叶子节点是一个有序链表，很方便左范围查找，相比于B-树的中序遍历查找要方便的多
     * */
    public List<V> searchRange(K key1, RangePolicy policy1, K key, RangePolicy policy) {
        return root.getRange(key1, policy1, key, policy);
    }

    /**
     * 数据插入
     * @param  key 数据key
     * @param value 数据值
     * */
    public void insert(K key, V value) {
        root.insertValue(key, value);
    }


    /**
     * 根据指定的key值删除数据
     * @param key
     * */
    public void delete(K key) {
        root.deleteValue(key);
    }

    public String toString() {
        // 队列，队列中的元素为集合，因为一个节点的node里面可能包含着多个children
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof YBPlusTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else
                    sb.append('\n');
            }
            queue = nextQueue;
        }

        return sb.toString();
    }

    private abstract class Node {
        List<K> keys;

        /**
         * 节点分裂(叶子和非叶子)
         * from =keyNumber()/2,to=keyNumber()[数组截取，包含头，不包含尾]
         * (maxIndex+1)/2 =(1/2)*maxIndex+(1/2) =size/2
         * 1.数组长度奇数:多0.5向下取整，为中间索引
         * 2.数组长度为偶数，中间索引为中间两项的后一项,相当于0.5+0.5取整多加1，刚好中间两个元素的第二个元素位置的索引
         */
        abstract Node split();

        abstract V getValue(K key);

        abstract K deleteValue(K key);

        abstract void insertValue(K key, V value);

        abstract K getFirstLeafKey();

        abstract List<V> getRange(K key1, RangePolicy policy1, K key2, RangePolicy policy2);

        abstract void merge(Node parent, Node sibling, BDelIndexer delIndexer);

        /**
         * 左旋，针对右侧兄弟节点
         * @param rightSibling 右侧兄弟节点
         * @param delIndexer 删除索引器
         * */
        abstract void leftRotate(Node parent, Node rightSibling, BDelIndexer delIndexer);

        /**
         * 右旋，针对左侧兄弟节点
         * @param leftSibling 右侧兄弟节点
         * @param delIndexer 删除索引器
         * */
        abstract void rightRotate(Node parent, Node leftSibling, BDelIndexer delIndexer);

        protected int keyNumber() {
            return keys.size();
        }

        protected void setKey(int index, K replaceKey) {
            keys.set(index, replaceKey);
        }

        protected K findKey(int index) {
            return keys.get(index);
        }

        protected void addKey(int index, K newKey) {
            keys.add(index, newKey);
        }

        protected K removeKey(int index) {
            return keys.remove(index);
        }

        protected void mergeKey(Node sibling) {
            keys.addAll(sibling.keys);
        }

        protected void mergeChildren(Node sibling) {
            throw new IllegalArgumentException("method mergeChildren si not realize");
        }

        protected void mergeValue(Node sibling) {
            throw new IllegalArgumentException("mergeValue is not realize");
        }

        protected void addChild(int index, Node child) {
            throw new IllegalArgumentException("addChild is not realize");
        }

        protected Node removeChild(int index) {
            throw new IllegalArgumentException("removeChild is not realize");
        }

        protected void addValue(int index, V value) {
            throw new IllegalArgumentException("addValue is not realize");
        }

        protected V removeValue(int index) {
            throw new IllegalArgumentException("removeValue is not realize");
        }

        /**
         *
         * 根节点分裂，比较简单，创建新的根，同时节点分裂都是一分为2的，所以根节点只需要按照顺序添加两个子节点就满足了
         * */
        Node splitRoot(Node origin, Node sibling) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(sibling.getFirstLeafKey());
            newRoot.children.add(origin);
            newRoot.children.add(sibling);
            return newRoot;
        }

        /**
         * 节点是否上溢，m阶B树的性质 所有叶子节点包含k-1个元素，其中 (m/ <= k <= m)
         * */
        protected boolean isOverflow() {
            return keyNumber() > (degree - 1);
        }

        /**
         * 节点是否下溢，m阶B树的性质 所有叶子节点包含k-1个元素，其中 (m/ <= k <= m)
         * */
        protected boolean isUnderflow() {
            // m阶B树的性质 所有叶子节点包含k-1个元素，其中 (m/ <= k <= m)
            return keyNumber() < (degree + 1) / 2 - 1;
        }

        /**
         * 节点元素是否富裕,m阶B树的性质 所有叶子节点包含k-1个元素，其中 (m/ <= k <= m)
         * */
        protected boolean isAffluent() {
            return keyNumber() > (degree + 1) / 2 - 1;
        }


        public String toString() {
            return keys.toString();
        }
    }

    private class InternalNode extends Node {
        List<Node> children;

        InternalNode() {
            this.keys = new ArrayList<K>();
            this.children = new ArrayList<Node>();
        }

        @Override
        V getValue(K key) {
            // 这个相当于递归处理，用对象来实现递归看着简单易懂
            return getChild(key).getValue(key);
        }

        /**
         *非叶子节点删除最为复杂，需要控制树的平衡，
         * 1.删除非叶子节点数据需要先转换成删除叶子节点数据,因为最终存储所有的索引和数据都在叶子节点中
         * 2.删除元素的节点，存在一个bug就是删除合并的时候key的问题，如果删除了关键字key则会导致bug
         *
         * */
        @Override
        K deleteValue(K key) {
            int loc = YUtils.binarySearch(keys, key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            Node child = getChild(childIdx);
            K replaceKey = child.deleteValue(key);
            // 1.第二层节点进入,出现这种情况就叶子节点删除完节点值之后为空了，没有后继值了，只能从右侧兄弟节点获取，
            // 2.如果没有右侧兄弟节点，则删除key一定是第二层节点的最后一个元素，不需要处理更新后继节点值，会转换成merge情况处理,会把第二层节点对应的key下移删除掉
            BDelIndexer delIndexer = BDelIndexerImpl.newDelIndexer(childIdx, keyNumber());
            if (replaceKey == null && delIndexer.hasRightSib()) {
                replaceKey = getChild(delIndexer.rightSibIdx()).getFirstLeafKey();
            }

            // 更新后继节点，递归"进入阶段"判断了是否key存在非叶子节点中，如果存在需要用来自叶子的后继key替换
            if (loc >= 0) {
                setKey(loc, replaceKey);
            }
            // 出现下溢，需要修复树的平衡
            if (child.isUnderflow()) {
                fix(delIndexer, child);
            }
            return replaceKey;
        }

        private void fix(BDelIndexer delIndexer, Node child) {
            // rotate
            Node leftSib = getChild(delIndexer.leftSibIdx());
            Node rightSib = getChild(delIndexer.rightSibIdx());
            // 判断旋转/合并
            if (delIndexer.hasLeftSib() && leftSib.isAffluent()) {// 右旋
                child.rightRotate(this, leftSib, delIndexer);
            } else if (delIndexer.hasRightSib() && rightSib.isAffluent()) {// 左旋
                child.leftRotate(this, rightSib, delIndexer);
            } else {//合并
                // 合并的左侧孩子，合并的右侧孩子获取
                Node left = getChild(delIndexer.mergeLeftIdx());
                Node right = getChild(delIndexer.mergeRightIdx());
                left.merge(this, right, delIndexer);

                // 如果合并到根节点，需要把根节点替换,根节点不符合元素个数,则把root更换为最后一次合并的左侧节点
                if (root == this && root.keyNumber() == 0) {
                    root = left;
                }
            }
        }

        @Override
        void insertValue(K key, V value) {
            // 下面两行代码构成递归，链式的调用，直接到叶子节点
            Node child = getChild(key);
            child.insertValue(key, value);// 继续调用insertValue,形成递归调用
            // 叶子节点插入之后满了，就需要分裂这里来控制叶子节点的分裂,注意这个是多层递归，最底层叶子节点插入完成不断向上一层节点回溯，直到root节点
            if (child.isOverflow()) {
                Node sibling = child.split();
                insertChild(sibling.getFirstLeafKey(), sibling);
            }
            // 如果到了这里判断根节点root节点的时候，说明递归已经回溯到最上一层，到达了root节点，否则root节点不会满的
            if (root.isOverflow()) {
                Node sibling = split();
                // 重新分裂根节点 [this当前节点]，[当前节点新分裂出来的节点]
                root = splitRoot(this, sibling);
            }
        }

        @Override
        K getFirstLeafKey() {
            // 1.递归调用 children.get(0)是一个对象
            // 2.这里与叶子节点分裂不同，新分裂的节点中没有上提的中间元素了，
            // 但是可以一直获取第一个孩子作为递归方式到叶子层节点，然后调用叶子层节点获取第一个元素，
            // 这个元素就是此时需要上提到上一层父节点的的元素

            return children.get(0).getFirstLeafKey();
        }

        @Override
        List<V> getRange(K key1, RangePolicy policy1, K key2, RangePolicy policy2) {
            return getChild(key1).getRange(key1, policy1, key2, policy2);
        }

        @Override
        void merge(Node parent, Node sibling, BDelIndexer delIndexer) {
            //分裂上提的key还原回分裂之前的节点上，然后合并到一起
            K splitUpKey = parent.findKey(delIndexer.splitUpKeyIdx());
            sibling.addKey(0, splitUpKey);
            mergeKey(sibling);
            mergeChildren(sibling);

            //删除分裂的孩子，和父节点中分裂上提的key
            parent.removeKey(delIndexer.splitUpKeyIdx());
            parent.removeChild(delIndexer.splitUpKeyIdx() + 1);
        }

        @Override
        void leftRotate(Node parent, Node rightSibling, BDelIndexer delIndexer) {
            //父节点的顶点上的key下移插入到数组最后一个位置,后继节点为删除点中最大的元素
            K leftRotateUpKey = parent.findKey(delIndexer.leftRotateUpKeyIdx());
            addKey(keyNumber(), leftRotateUpKey);

            //转移右侧兄弟节点的第一个孩子作为自己的最后一个孩子
            addChild(keyNumber() + 1, rightSibling.removeChild(0));

            //后继节点的第一个key转移到父节点点的顶点位置
            parent.setKey(delIndexer.leftRotateUpKeyIdx(), rightSibling.removeKey(0));
        }

        @Override
        void rightRotate(Node parent, Node leftSibling, BDelIndexer delIndexer) {
            //父节点的顶点key下移，前继key为删除节点中的最小的元素
            K rightRotateUpKey = parent.findKey(delIndexer.rightRotateUpKeyIdx());
            addKey(0, rightRotateUpKey);

            //转移左侧节点的最后一个孩子作为自己的第一个孩子
            addChild(0, leftSibling.removeChild(leftSibling.keyNumber()));

            //前继节点最后一个key转移到父节点的顶点位置
            parent.setKey(delIndexer.rightRotateUpKeyIdx(), leftSibling.removeKey(leftSibling.keyNumber() - 1));
        }

        @Override
        Node split() {
            // 1.size/2可以获取中间值(解决偶数索引取第二个中间值的问题)
            // 2.集合截取方式，含头不含尾
            int from = keyNumber() / 2, to = keyNumber();
            InternalNode sibling = new InternalNode();
            // 不包含中间元素截取
            sibling.keys.addAll(keys.subList(from + 1, to));
            // 1.元素索引对应左侧值域的孩子，元素索引+1对应右侧值域的孩子
            // 2. from+1 取右侧值域的孩子,to+1因为孩子总索引比元素多1，要完全截取到最后
            sibling.children.addAll(children.subList(from + 1, to + 1));

            // 1.subList api操作的还是原始集合，虽然返回的是SubList对象，里面包的集合还是原始的list(看源代码，用的是一个内部类包装原始list，从截取index范围迭代清空原始List)
            // 2. 原始节点清空中间元素之后的所有元素(包含中间元素)作为分裂后的左侧孩子,因为中间元素已经被分裂出去了
            keys.subList(from, to).clear();
            // 清空中间元素右侧值域之后的孩子，因为已经归属到新分裂的节点中了
            children.subList(from + 1, to + 1).clear();

            return sibling;
        }

        private Node getChild(int childIdx) {
            if (childIdx < 0 || childIdx > keyNumber()) {
                return null;
            }
            return children.get(childIdx);
        }

        private Node getChild(K key) {
            int loc = YUtils.binarySearch(keys, key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            return children.get(childIdx);
        }

        private void insertChild(K key, Node child) {
            // 查找插入位置，存在返回相等元素值位置，不存在返回插入位置
            int loc = YUtils.binarySearch(keys, key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            if (loc >= 0) {// 存在替换
                children.set(childIdx, child);
            } else {// 不存在插入
                keys.add(childIdx, key);
                // +1表示孩子的位置应该在此时插入的元素的右侧值域
                children.add(childIdx + 1, child);
            }
        }

        @Override
        protected void addChild(int index, Node child) {
            children.add(index, child);
        }

        @Override
        protected Node removeChild(int index) {
            return children.remove(index);
        }

        @Override
        protected void mergeChildren(Node sibling) {
            children.addAll(((InternalNode) sibling).children);
        }
    }

    /**
     * 叶子节点
     * 1.这里面定义了key结合和value集合，因为这两个集合大小一定是一样的，因为B+树的所有数据都在叶子节点上，并且所有key也会在叶子节点上
     * 2.叶子的所有节点形成一个有序的链表，每个节点都保存下一个节点的引用，我们这里用数组来实现，也就是arrayList，可以方便进行二分查找增加查找效率,
     * 3.key和value是一一对应的
     * */
    private class LeafNode extends Node {
        List<V> values;
        // 下一个节点的引用
        LeafNode next;

        LeafNode() {
            keys = new ArrayList<K>(degree);
            values = new ArrayList<V>();
        }

        @Override
        V getValue(K key) {
            int loc = YUtils.binarySearch(keys, key);
            // 叶子节点不需要递归处理，直接就能获取到对应的数据，二分查找即可
            return loc >= 0 ? values.get(loc) : null;
        }

        @Override
        K deleteValue(K key) {
            // 叶子节点删除数据，不用考虑平衡，直接删除即可，主要控制在非叶子节点上控制
            int loc = YUtils.binarySearch(keys, key);
            if (loc >= 0) {
                keys.remove(loc);
                values.remove(loc);
            }
            // 返回后继key，如果没有元素可以作为后继key，上层节点将会选取右侧兄弟节点的第一个元素作为后继key
            return keys.get(0);
        }

        @Override
        void insertValue(K key, V value) {
            // 二分查找，如果找到了则返回对应的index，如果没有找到返回的是插入位置
            int loc = YUtils.binarySearch(keys, key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            // >=0表示已经存在key，更新值
            if (loc >= 0) {
                values.set(loc, value);
            } else {// 不存在，二分查找返回的插入位置插入key和value
                keys.add(childIdx, key);
                values.add(childIdx, value);
            }
            // 插入后判断是否满了,"初始化第一次"叶子节点插入分裂的时候处理，其余情况都在非叶子节点中处理分裂
            if (root.isOverflow()) {
                // 获取新分裂出来的节点
                Node sibling = split();
                // 分裂根节点
                root = splitRoot(this, sibling);
            }
        }

        @Override
        K getFirstLeafKey() {
            return keys.get(0);
        }

        @Override
        List<V> getRange(K key1, RangePolicy policy1, K key2, RangePolicy policy2) {
            List<V> result = new LinkedList<V>();
            LeafNode node = this;
            while (node != null) {
                Iterator<K> kIt = node.keys.iterator();
                Iterator<V> vIt = node.values.iterator();
                while (kIt.hasNext()) {
                    System.out.println();
                    K key = kIt.next();
                    V value = vIt.next();
                    int cmp1 = key.compareTo(key1);
                    int cmp2 = key.compareTo(key2);
                    if (((policy1 == RangePolicy.EXCLUSIVE && cmp1 > 0) || (policy1 == RangePolicy.INCLUSIVE && cmp1 >= 0))
                            && ((policy2 == RangePolicy.EXCLUSIVE && cmp2 < 0) || (policy2 == RangePolicy.INCLUSIVE && cmp2 <= 0)))
                        result.add(value);
                    else if ((policy2 == RangePolicy.EXCLUSIVE && cmp2 >= 0)
                            || (policy2 == RangePolicy.INCLUSIVE && cmp2 > 0))
                        return result;
                }
                node = node.next;
            }
            return result;
        }

        @Override
        void merge(Node parent, Node sibling, BDelIndexer delIndexer) {
            mergeKey(sibling);
            mergeValue(sibling);
            next = ((LeafNode) sibling).next;
            parent.removeKey(delIndexer.splitUpKeyIdx());
        }

        @Override
        Node split() {
            // 分裂，顾名思义就是把一个节点像细胞分裂一样，1分为2，sibling为需要新分裂出来的节点(叶子节点兄弟节点都是叶子节点)
            LeafNode sibling = new LeafNode();
            // 此节点中间位置的数据项会被提升到父节点中，中间值右面归属于新分裂的节点上，左面值不动，还在此节点上挂着
            int from = keyNumber() / 2, to = keyNumber();
            sibling.keys.addAll(keys.subList(from, to));
            sibling.values.addAll(values.subList(from, to));

            // 清空，keys.subList虽然返回的是一个SubList对象，但是操作的数据还是原来集合的数据，所以,clear也会影响原来集合的数据
            keys.subList(from, to).clear();
            values.subList(from, to).clear();

            // 1.第一次分裂之前，下一个兄弟的指针肯定是null，之后分裂之后下一个兄弟指针才有,所以此节点如果再继续分裂，新的兄弟节点的下一个节点就是上一次的兄弟节点
            // 2."分裂都是向右面方向扩展一个新节点"

            // 新的兄弟节点的下一个引用为上一次分裂的兄弟节点，中间的叶子节点分裂，保持中间位置分裂之后插入数据，需要连接好左面和右面的链接
            sibling.next = next;
            // 当前节点下一个兄弟链接为新分裂的节点
            next = sibling;
            return sibling;
        }

        @Override
        void leftRotate(Node parent, Node rightSibling, BDelIndexer delIndexer) {
            //后继key右侧兄弟节点的第一个key，作为删除元素中的最大元素
            addKey(keyNumber(), rightSibling.removeKey(0));

            //后继value右侧兄弟节点的第一个value，作为删除value中的最大value
            addValue(keyNumber(), rightSibling.removeValue(0));

            //父节点中顶点位置值替换，替换为右侧兄弟此时，移除掉一个key之后的后继key
            parent.setKey(delIndexer.leftRotateUpKeyIdx(), rightSibling.getFirstLeafKey());
        }

        @Override
        void rightRotate(Node parent, Node leftSibling, BDelIndexer delIndexer) {
            //前继key左侧兄弟节点的最后一个key，作为删除元素的第一个最大元素,不同于右旋，因为叶子节点的上一层节点需要保留叶子节点的第一个元素key,也就是保留split key
            parent.setKey(delIndexer.rightRotateUpKeyIdx(), leftSibling.removeKey(leftSibling.keyNumber() - 1));

            // 添加从左侧节点中转移过来的key和value
            addKey(0, parent.findKey(delIndexer.rightRotateUpKeyIdx()));
            addValue(0, leftSibling.removeValue(leftSibling.keyNumber() - 1));
        }

        @Override
        protected V removeValue(int index) {
            return values.remove(index);
        }

        @Override
        protected void addValue(int index, V value) {
            values.add(index, value);
        }

        @Override
        protected void mergeValue(Node sibling) {
            values.addAll(((LeafNode) sibling).values);
        }
    }

    // 测试程序
    public static void main(String[] args) {
        YBPlusTree<Integer, String> bpt = new YBPlusTree<Integer, String>(5);
        bpt.insert(1, "b");
        bpt.insert(3, "b");
        bpt.insert(5, "b");
        bpt.insert(7, "b");
        bpt.insert(9, "b");
        bpt.insert(11, "b");
        bpt.insert(13, "b");
        bpt.insert(15, "b");
        bpt.insert(17, "b");
        bpt.insert(19, "b");
        bpt.insert(21, "b");
        bpt.insert(23, "b");
        bpt.insert(25, "b");
        bpt.insert(14, "q");
        bpt.delete(23);
        bpt.delete(13);
        bpt.delete(21);
        /*bpt.delete(19);*/

        System.out.println(bpt.toString());
    }

    /**
     * 删除测试
     * */
    private static void delTest(YBPlusTree<Integer, String> bpt) {
        bpt.delete(1);
        bpt.delete(3);
        bpt.delete(5);
        bpt.delete(7);
        bpt.delete(9);
        //
        System.out.println(bpt.search(0));
        System.out.println(bpt.search(1));
        System.out.println(bpt.search(2));
        System.out.println(bpt.search(3));
        System.out.println(bpt.search(4));
        System.out.println(bpt.search(5));
        System.out.println(bpt.search(6));
        System.out.println(bpt.search(7));
        System.out.println(bpt.search(8));
        System.out.println(bpt.search(9));
    }

}

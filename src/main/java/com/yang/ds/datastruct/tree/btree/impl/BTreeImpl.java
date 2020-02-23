package com.yang.ds.datastruct.tree.btree.impl;

import com.yang.ds.algorithm.utils.YUtils;
import com.yang.ds.datastruct.tree.btree.BTree;
import com.yang.ds.datastruct.tree.btree.node.BDelIndexer;
import com.yang.ds.datastruct.tree.btree.node.BInnerNode;
import com.yang.ds.datastruct.tree.btree.node.BLeafNode;
import com.yang.ds.datastruct.tree.btree.node.BNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * b树 m阶  查找复杂度结合二分查找在O(logN)的范围,但是树更矮胖，可以用作磁盘上的索引结构降低IO,一次IO处理树的一层，M阶根磁盘页大小有关系
 * 1. 根节点至少有两个孩子
 * 2. 非叶子节点k-1个元素k个孩子(m/2<=k<=m)
 * 3. 叶子节点k-1个元素(m/2<=k<=m)
 * 4. 所有叶子节点都在同一层
 * 5. 非叶子节点当中k-1个元素正好是k个孩子包含的元素的值域分划(元素中间和两边的范围值域内都对应着一个孩子)
 * 6. 每个节点中元素都是从小到大排列的，可以结合二分查找进行插入和查找
 * 7. B树的每一层节点都存储key和value，
 * 8. B树非叶子节点的中的key的索引，对应着自己左侧值域的孩子,key索引+1对应自己右侧值域的孩子
 * 9. 每一次分裂，key上提之后新分裂的孩子都会做上提key的右侧的孩子,所以每个key右侧的孩子，都是上一次分裂添加的
 * 10 B树的查找，不必需要到叶子节点，非叶子节点找到直接返回，因为非叶子节点也存储数据
 *
 *
 *
 * B树分裂
 * 1.分裂都是从左到右,中间元素节点上提
 * 2.二叉树的性质和值域划分都是分裂形成的
 * 3.分裂就是从元素集合中的中间位置进行数据截取向右扩充一个新节点,同时把中间位置对应的孩子也都分配给新的节点
 * 4.数据上提，会出现上提数据的左侧孩子和右侧孩子多余，但是分裂一分为二之后，刚好作为分裂的原始节点的最右侧孩子，和新分裂
 * 出来的孩子的最左侧孩子
 *
 * B树删除
 *
 * 1.删除的key在非叶子节点，需要从叶子节点找到后继的key，转换成删除叶子的问题
 *
 * 2.后继的key选择，选择节点对应的孩子向下递归到叶子节点，判断上层节点是否找到了key，如果找到则转移叶子节点的后继key返回给上层
 * 否则，判断叶子节点是否存在删除的key如果存在则删除，返回后继的key
 *
 *  3.递归返阶段的第二层，判断后继key是否为空如果为空，如果返回的key为空证明，这个后继节点的数据已经删除光了，则继续获取右侧兄弟节点的后继key
 * 但是，如果出现key为空说明删除的key就存在叶子节点中，否则不可能为空,这样做的逻辑只是为了迁就B+树等一些其他B树，他们叶子节点存在所有的key
 * 且这个key可以单独在一个节点中（重复的的存在，删除必定会删除叶子）,所以删除会触发没有后继key的情况,但是B-树则不是，删除只是
 * 找后继的叶子替换，不会删除叶子（一个元素作为一个叶子的情况）也就是说B-树的叶子都是可以作为后继的叶子，不存在先删除叶子对应的key再选择后继的情况
 * (B+树删除需要先删除叶子上对应的key，才会出现后继节点为null的情况)
 *
 * 4.递归逐层判断下一层孩子移除掉后继key是否满足B树的平衡，通过，左旋、右旋、合并来逐层向上处理
 *  4.1 左旋，针对右侧兄弟，父节点对应的旋转顶点key下移,右侧兄弟节点的后继key上移,如果是非叶子节点则右侧的兄弟节点的第一个孩子
 *  作为自己的最后一个孩子
 *  4.1 右旋,父节点对应的顶点key下移，同时左侧兄弟的最后一个key上移到父节点，如果是非叶子节点则左侧兄弟的右侧最后一个孩子作为自己的
 *  第一个孩子
 *  4.3.合并操作，就是分裂的逆反操作，从右向左，两个节点合并到一起，分裂上提的key下移，孩子也合并到一起,如果是叶子节点就不用管孩子的合并了
 * 5. 递归逐层向上返回，如果到了查找到删除key的节点，则用返回的后继节点替换其key值
 *
 * 6. 删除结束
 *
 * */
public class BTreeImpl<K extends Comparable<? super K>, V> implements BTree<K, V> {

    private BNode<K, V> root;
    private int degree;

    public BTreeImpl(int degree) {
        if (degree < MIN_DEGREE) {
            throw new IllegalArgumentException("degree is less than " + MIN_DEGREE + "" + "," + degree);
        }
        this.root = new LeafNode(degree);
        this.degree = degree;
    }

    private BNode splitRoot(BNode origin, BNode<K, V> rightSib) {
        K splitKey = rightSib.keys().remove(0);
        V splitValue = rightSib.values().remove(0);

        BInnerNode newRoot = new InnerNode(degree);
        newRoot.keys().add(splitKey);
        newRoot.values().add(splitValue);

        newRoot.children().add(origin);
        newRoot.children().add(rightSib);
        return newRoot;
    }

    /**
     * 插入
     * @param key 插入的key
     * @param value 插入的value
     * */
    @Override
    public void insert(K key, V value) {
        root.insert(key, value);
    }

    /**
     * 删除
     * @param key 删除的key
     * */
    @Override
    public void delete(K key) {
        root.delete(key, -99);
    }

    /**
     * 查找
     * @param key 查找的key
     * */
    @Override
    public V search(K key) {
        return root.search(key);
    }

    @Override
    public String treeToString() {
        return BtreeStringBuilder.toString(root);
    }

    private class KeyValue<K, V> {
        K key;
        V value;

        public KeyValue(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private class InnerNode<K extends Comparable<? super K>, V> extends AbstractBNode<K, V> implements BInnerNode<K, V> {
        private List<V> values;
        private List<BNode<K, V>> children;

        public InnerNode(int degree) {
            super(degree);
            values = new ArrayList<>(degree);
            children = new ArrayList<>(degree);
        }

        @Override
        public V search(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            // 非叶子节点存储value，如果非叶子节点找到则直接返回值
            if (loc >= 0) {
                return values.get(loc);
            }
            return getChild(childIdx).search(key);
        }

        public BNode<K, V> getChild(int childIdx) {
            if (childIdx < 0 || childIdx > keyNumber()) {
                throw new IllegalArgumentException("child index is out bounder");
            }
            return children.get(childIdx);
        }

        @Override
        public void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer delIndexer) {
            InnerNode<K, V> innerParent = (InnerNode) parent;

            // 还原分裂的key
            K splitKey = innerParent.keys().get(delIndexer.splitUpKeyIdx());
            V splitValue = innerParent.values.get(delIndexer.splitUpKeyIdx());
            sibling.keys().add(0, splitKey);
            sibling.values().add(0, splitValue);

            // 合并key和value到此节点
            keys.addAll(sibling.keys());
            values.addAll(sibling.values());

            // 合并孩子
            children.addAll(((BInnerNode) sibling).children());

            // 删除父节点分裂的key和对应的分裂的孩子
            parent.keys().remove(delIndexer.splitUpKeyIdx());
            parent.values().remove(delIndexer.splitUpKeyIdx());
            ((InnerNode) parent).children().remove(delIndexer.splitUpKeyIdx() + 1);
        }

        @Override
        public void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer delIndexer) {
            // 获取父节点中的旋转key
            K rotateKey = parent.keys().get(delIndexer.leftRotateUpKeyIdx());
            V rotateValue = parent.values().get(delIndexer.leftRotateUpKeyIdx());

            // 旋转key下移
            int keyNumber = keyNumber();
            keys.add(keyNumber, rotateKey);
            values.add(keyNumber, rotateValue);

            // 后继节点key上移到父节点
            K firstLeftKey = rightSib.keys().remove(0);
            V firstLeftValue = rightSib.values().remove(0);
            parent.keys().set(delIndexer.leftRotateUpKeyIdx(), firstLeftKey);
            parent.values().set(delIndexer.leftRotateUpKeyIdx(), firstLeftValue);

            // 添加左侧前继节点的最后一个孩子作为自己的第一个孩子
            BNode<K, V> firstLeftChild = ((BInnerNode<K, V>) rightSib).children().remove(0);
            children.add(firstLeftChild);
        }

        @Override
        public void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer delIndexer) {
            // 旋转key下移
            K rotateKey = parent.keys().get(delIndexer.rightRotateUpKeyIdx());
            V rotateValue = parent.values().get(delIndexer.rightRotateUpKeyIdx());
            keys.add(0, rotateKey);
            values.add(0, rotateValue);

            // 前继key上移到父节点,数组底层删除，会出发数组的复制，索引尽量使用set修改
            int keyNumber = leftSib.keyNumber();
            K lastRightKey = leftSib.keys().remove(keyNumber - 1);
            V lastRightValue = leftSib.values().remove(keyNumber - 1);
            parent.keys().set(delIndexer.rightRotateUpKeyIdx(), lastRightKey);
            parent.values().set(delIndexer.rightRotateUpKeyIdx(), lastRightValue);

            // 前继节点的最后的孩子作为自己的第一个孩子
            BNode<K,V> lastRightChild = ((BInnerNode<K,V>)leftSib).children().remove(keyNumber);
            children.add(0, lastRightChild);
        }

        @Override
        public BNode split() {
            int from = keyNumber() / 2, to = keyNumber();
            InnerNode rightSib = new InnerNode(degree);
            // key分裂
            rightSib.keys().addAll(keys().subList(from, to));
            rightSib.values().addAll(values().subList(from, to));

            // 孩子分裂
            rightSib.children().addAll(children().subList(from + 1, to + 1));

            // 清空 gc
            keys().subList(from, to).clear();
            values().subList(from, to).clear();
            children().subList(from + 1, to + 1).clear();
            return rightSib;
        }

        @Override
        public void insert(K key, V value) {
            // 递归到叶子节点进行插入
            BNode child = getChild(key);
            child.insert(key, value);
            if (child.isOverflow()) {
                insertChild(child.split());
                if (root.isOverflow()) {
                    root = splitRoot(this, split());
                }
            }
        }

        @Override
        public Object delete(K key, int keyLoc) {
            int loc = YUtils.binarySearch(keys, key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            keyLoc = keyLoc >= 0 ? keyLoc : loc;
            // 递归到叶子层处理删除
            BNode<K, V> child = getChild(childIdx);
            KeyValue<K, V> kv = (KeyValue<K, V>) child.delete(key, keyLoc);
            // 删除在非叶子节点，并且已返回删除的后继节点
            if (loc >= 0 && kv != null) {
                keys.set(loc, kv.key);
                values.set(loc, kv.value);
            }
            if (child.isUnderflow()) {
                BDelIndexer indexer = BDelIndexerImpl.newDelIndexer(childIdx, keyNumber());
                BNode newRoot = fixBalance(child, this, indexer);
                // 根分裂
                if (root.keyNumber() == 0 && newRoot != null) {
                    root = newRoot;
                }
            }
            return kv;
        }


        private void insertChild(BNode<K, V> rightSib) {
            K splitKey = rightSib.keys().remove(0);
            V splitValue = rightSib.values().remove(0);
            int loc = YUtils.binarySearch(keys(), splitKey);
            int keyIdx = loc >= 0 ? loc + 1 : -loc - 1;

            keys.add(keyIdx, splitKey);
            values.add(splitValue);

            children.add(keyIdx + 1, rightSib);
        }

        public BNode<K, V> getChild(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            return getChild(childIdx);
        }

        @Override
        public List<BNode<K, V>> children() {
            return children;
        }

        @Override
        public List<V> values() {
            return values;
        }
        @Override
        public String toString() {
            return keys.toString();
        }
    }

    private class LeafNode<K extends Comparable<? super K>, V> extends AbstractBNode<K, V> implements BLeafNode<K, V> {

        private List<V> values;

        protected LeafNode(int degree) {
            super(degree);
            values = new ArrayList<>(degree);
        }

        @Override
        public List<V> values() {
            return values;
        }

        @Override
        public void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer indexer) {
            K splitKey = parent.keys().get(indexer.splitUpKeyIdx());
            V splitValue = parent.values().get(indexer.splitUpKeyIdx());

            sibling.keys().add(0, splitKey);
            sibling.values().add(0, splitValue);

            keys.addAll(sibling.keys());
            values.addAll(sibling.values());

            parent.keys().remove(indexer.splitUpKeyIdx());
            parent.values().remove(indexer.splitUpKeyIdx());
            ((BInnerNode)parent).children().remove(indexer.splitUpKeyIdx() + 1);
        }

        @Override
        public void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer indexer) {
            // 后即key转移到最后一个key的位置上
            K rotateKey = parent.keys().get(indexer.leftRotateUpKeyIdx());
            V rotateValue = parent.values().get(indexer.leftRotateUpKeyIdx());
            int keyNumber = keyNumber();
            keys.add(keyNumber, rotateKey);
            values.add(keyNumber, rotateValue);

            // 旋转key替换,此时后继key已经删除掉一个，更换成下一个了,因为第二层节点包含叶子节点第一个元素的特性
            K firstLeftKey = rightSib.keys().remove(0);
            V firstLeftValue = rightSib.values().remove(0);
            parent.keys().set(indexer.leftRotateUpKeyIdx(), firstLeftKey);
            parent.values().set(indexer.leftRotateUpKeyIdx(), firstLeftValue);
        }

        @Override
        public void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer indexer) {
            // 后即key转移到最后一个key的位置上
            K rotateKey = parent.keys().get(indexer.rightRotateUpKeyIdx());
            V rotateValue = parent.values().get(indexer.rightRotateUpKeyIdx());
            keys.add(0, rotateKey);
            values.add(0, rotateValue);

            // 旋转key替换,
            int keyNumber = leftSib.keyNumber();
            K lastRightKey = leftSib.keys().remove(keyNumber - 1);
            V lastRightValue = leftSib.values().remove(keyNumber - 1);
            parent.keys().set(indexer.rightRotateUpKeyIdx(), lastRightKey);
            parent.values().set(indexer.rightRotateUpKeyIdx(), lastRightValue);
        }

        @Override
        public BNode split() {
            int from = keyNumber() / 2, to = keyNumber();
            // 向右侧分裂一个节点，从中间位置截取数据
            LeafNode rightSib = new LeafNode(degree());

            rightSib.keys().addAll(keys().subList(from, to));
            rightSib.values().addAll(values().subList(from, to));

            // 分裂出去的元素和value 清空
            keys().subList(from, to).clear();
            values().subList(from, to).clear();
            return rightSib;
        }

        @Override
        public void insert(K key, V value) {
            int loc = YUtils.binarySearch(keys(), key);
            int addLoc = loc >= 0 ? loc : -loc - 1;
            // 存在更新value
            if (loc >= 0) {
                values.set(loc, value);
            } else {// 不存在插入
                keys.add(addLoc, key);
                values.add(addLoc, value);
            }
            // 判断是否根分裂
            if (root.isOverflow()) {
                root = splitRoot(this, split());
            }
        }

        @Override
        public Object delete(K key, int keyLoc) {
            // 删除在非叶子节点，直接把叶子节点后继key删除，并且返回到删除节点替换
            if (keyLoc >= 0) {
                return new KeyValue<>(keys.remove(0), values.remove(0));
            }
            int loc = YUtils.binarySearch(keys(), key);
            if (loc >= 0) {
                keys.remove(loc);
                values.remove(loc);
            }
            // 如果删除在叶子节点，或者没有找到对应的key则返回null不予处理
            return null;
        }

        @Override
        public V search(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            return loc >= 0 ? values.get(loc) : null;
        }
        @Override
        public String toString() {
            return keys.toString();
        }
    }
}

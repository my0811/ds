package com.yang.ds.datastruct.tree.btree.impl;

import com.yang.ds.algorithm.utils.YUtils;
import com.yang.ds.datastruct.tree.btree.BPlusTree;
import com.yang.ds.datastruct.tree.btree.node.BDelIndexer;
import com.yang.ds.datastruct.tree.btree.node.BInnerNode;
import com.yang.ds.datastruct.tree.btree.node.BLeafNode;
import com.yang.ds.datastruct.tree.btree.node.BNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * B+树(m阶)
 * 1.根节点至少有两个孩子
 * 2.非叶子节点有k-1个元素和k个孩子(m/2<=k<=m)向上取整
 * 3.叶子节点右k-1个元素(m/2<=k<=m)向上取整
 * 4.所有叶子节点都在同一层
 * 5.非叶子节点中k-1个元素刚好是k个孩子的值域划分
 * 6.每个节点中元素都是从小到大排列的，可以结合二分查找进行插入和查找
 *
 * 7.所有的value都存在叶子节点，非叶子节点不存储value只存储key索引,这个和b-树不一样,叶子节点存储key和value
 * 8.叶子节点包含所有数据的key和value，上层节点的key在叶子节点中都能找到
 * 9.上层节点的key均来自右侧分裂节点向下获取第一个左侧子孩子直到递归到叶子节点的第一个最左侧key
 * 10.第二层非叶子节点的值都是其下面叶子节点分裂后的新节点的最左侧的第一个key
 * 11.底层的叶子节点需要形成一个单项链表，方便范围查找，可以在链表上直接进行，而不再需要通过树的索引来判断
 *
 * 分裂：参考B树分裂一样的原理 不同有一下两点
 * 1. 叶子节点分裂，上提key的同时，右侧新分裂的节点，数组第一个key就是上提的key，所以第二层节点和叶子节点有这特殊的关系
 * 第二层节点的所有key都来自于下面叶子节点新分裂的节点的key数组的第一个key,如果非第二层节点则从key的右侧值域对应的孩子，
 * 递归获取左侧第一个孩子，直到叶子节点的第一个元素，就找到了锁对应的key，因为叶子节点存储所有key，上层key均来自叶子节点
 * 非叶子节点的分裂则与B树相同
 *
 *
 * 删除：同B树一样有一下几点不同
 * 1. 叶子节点的删除无需要再判断上层节点是否包含删除的key，因为叶子节点存储所有key，如果叶子节点能找到，则删除
 * 没有则不删除，同时返回后继key
 * 2. 会出现后继key为null的情况，那说明，删除的key所在叶子节点之后一个关键字key，如果删了，整个节点就没有了值
 * 所以在向上第二层递归处理的时候，会进行一次补救，从右侧兄弟节点获取一个key作为后继key,如果右侧兄弟节点也没有则
 * 不需要特殊处理，说明，删除的key在第二层节点最后一个位置的key，此时右侧没有兄弟，并且，当前叶子删除了元素之后，没有了数据
 * 必然会出发merge操作，会把第二层对应的位置key一起还原掉
 *
 *
 * */
public class BPlusTreeImpl<K extends Comparable<? super K>, V> implements BPlusTree<K, V> {

    // root节点
    private BNode<K, V> root;
    // 树的阶
    private int degree = 5;

    public BPlusTreeImpl(int degree) {
        if (degree < MIN_DEGREE) {
            throw new IllegalArgumentException("degree is less than " + MIN_DEGREE + "" + "," + degree);
        }
        this.degree = degree;
        root = new LeafNode<>(degree);
    }

    @Override
    public void insert(K key, V value) {
        root.insert(key, value);
    }

    @Override
    public void delete(K key) {
        root.delete(key, -99);
    }

    @Override
    public V search(K key) {
        return root.search(key);
    }

    @Override
    public String treeToString() {
        return BtreeStringBuilder.toString(root);
    }

    @Override
    public List<V> searchRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy) {
        return ((Range) root).getRange(beginKey, beginPolicy, endKey, endPolicy);
    }

    private BNode splitRoot(BNode origin, BNode<K, V> rightSib) {
        InnerNode<K, V> newRoot = new InnerNode<>(degree);
        K splitKey = rightSib.keys().get(0);
        delSplitKey(rightSib);

        newRoot.keys.add(splitKey);
        newRoot.children.add(origin);
        newRoot.children.add(rightSib);
        return newRoot;
    }

    /**
     * 统一实现逻辑，分裂的元素包含在右侧新分裂的节点中，作为第一个元素
     * 叶子节点需要保留，非叶子节点需要删除
     * @param bNode
     * */
    private void delSplitKey(BNode bNode) {
        if (bNode instanceof BInnerNode) {
            bNode.keys().remove(0);
        }
    }

    /**
     * 实现范围查找接口，只有B+tree 存在
     * */
    private interface Range<K extends Comparable<? super K>, V> {
        List<V> getRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy);
    }

    private class InnerNode<K extends Comparable<? super K>, V> extends AbstractBNode<K, V> implements Range<K, V>, BInnerNode<K, V> {
        private List<BNode<K, V>> children;

        protected InnerNode(int degree) {
            super(degree);
            this.keys = new ArrayList<>(degree);
            children = new ArrayList<>(degree);
        }

        @Override
        public BNode<K, V> getChild(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            // 存在相等，则从右侧值域查找孩子
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            return children().get(childIdx);
        }

        @Override
        public BNode<K, V> getChild(int childIdx) {
            if (childIdx >= 0 || childIdx <= keyNumber()) {
                return children.get(childIdx);
            }
            return null;
        }

        @Override
        public void insert(K key, V value) {
            BNode<K, V> child = getChild(key);
            child.insert(key, value);
            if (child.isOverflow()) {
                insertChild(child.split());
            }
            if (root.isOverflow()) {
                root = splitRoot(this, split());
            }
        }

        @Override
        public Object delete(K key, int keyLoc) {
            int loc = YUtils.binarySearch(keys, key);
            // 存在相等，则从右侧值域查找孩子
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            BNode<K, V> child = getChild(childIdx);
            K repKey = (K) child.delete(key, keyLoc);
            BDelIndexer indexer = BDelIndexerImpl.newDelIndexer(childIdx, keyNumber());
            // 特殊情况处理，后继key为null的情况，只有B+tree会存在的情况，因为B+Tree需要先从叶子节点删除这个key，且这个key有可能作为一个一个节点中唯一的key
            if (null == repKey) {
            /*
            * 如果存在右侧兄弟则获取，如果没有说明删除的key在第二层，B-tree不会出现替换key为空，B+的话，如果出现null一定是删除的key
             在第二层，无需处理，一定会触发合并操作，删除父节点中上提的key，所以也不用替换
            * */
                if (indexer.hasRightSib()) {
                    repKey = getChild(indexer.rightSibIdx()).keys().get(0);
                }
            }
            if (loc >= 0 && repKey != null) {
                keys.set(loc, repKey);
            }
            // 删除如果下溢出，则进行平衡修复
            if (child.isUnderflow()) {
                BNode newRoot = fixBalance(child, this, indexer);
                // 根分裂
                if (root.keyNumber() == 0 && newRoot != null) {
                    root = newRoot;
                }
            }
            return repKey;
        }

        @Override
        public V search(K key) {
            return getChild(key).search(key);
        }

        private void insertChild(BNode<K, V> rightSib) {
            K splitKey = rightSib.keys().get(0);
            delSplitKey(rightSib);

            int loc = YUtils.binarySearch(keys(), splitKey);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            keys.add(childIdx, splitKey);
            // 分裂的孩子的位置永远在分裂上提元素所在位置的右侧值域，所以+1,左侧值域为元素索引位置
            children().add(childIdx + 1, rightSib);
        }

        @Override
        public void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer delIndexer) {
            // 分裂的key还原,需要删除，因为分裂的时候是增加的,逆反操作
            K splitUpKey = parent.keys().get(delIndexer.splitUpKeyIdx());
            sibling.keys().add(0, splitUpKey);

            // 合并key和children
            keys().addAll(sibling.keys());
            children().addAll(((BInnerNode) sibling).children());

            // 父节点删除分裂的key，并且删除分裂的孩子
            parent.keys().remove(delIndexer.splitUpKeyIdx());
            ((BInnerNode) parent).children().remove(delIndexer.splitUpKeyIdx() + 1);
        }

        @Override
        public void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer delIndexer) {
            // 获取旋转key
            K rotateKey = parent.keys().get(delIndexer.leftRotateUpKeyIdx());
            // 旋转key下移
            int keyNumber = keyNumber();
            keys().add(keyNumber, rotateKey);

            // 后继key上移
            K leftFirstKey = rightSib.keys().remove(0);
            parent.keys().set(delIndexer.leftRotateUpKeyIdx(), leftFirstKey);

            // 后继节点孩子转移作为自己的最后一个孩子
            BNode<K, V> leftFirstChild = ((BInnerNode<K, V>) rightSib).children().remove(0);
            children().add(leftFirstChild);
        }

        @Override
        public void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer delIndexer) {
            // 获取旋转key
            K rotateKey = parent.keys().get(delIndexer.rightRotateUpKeyIdx());

            // 旋转key下移
            keys().add(0, rotateKey);

            // 前继key上移
            int keyNumber = leftSib.keyNumber();
            K rightLastKey = leftSib.keys().remove(keyNumber - 1);
            parent.keys().set(delIndexer.rightRotateUpKeyIdx(), rightLastKey);

            // 前继节点的最后一个孩子作为自己的第一个孩子
            BNode<K, V> rightLastChild = ((BInnerNode<K, V>) leftSib).children().remove(keyNumber);
            children().add(0, rightLastChild);
        }

        @Override
        public BNode split() {
            int from = keyNumber() / 2, to = keyNumber();
            // split new node
            InnerNode<K, V> rightSib = new InnerNode(degree);
            rightSib.keys.addAll(keys().subList(from, to));
            rightSib.children.addAll(children().subList(from + 1, to + 1));
            //GC
            keys.subList(from, to).clear();
            children.subList(from + 1, to + 1).clear();
            return rightSib;
        }

        @Override
        public List<V> getRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy) {
            return ((Range) getChild(beginKey)).getRange(beginKey, beginPolicy, endKey, endPolicy);
        }

        @Override
        public List<BNode<K, V>> children() {
            return children;
        }

        @Override
        public String toString() {
            return keys.toString();
        }
    }


    private class LeafNode<K extends Comparable<? super K>, V> extends AbstractBNode<K, V> implements Range<K, V>, BLeafNode<K, V> {
        private BNode<K, V> next;
        private List<V> values;

        public LeafNode(int degree) {
            super(degree);
            values = new ArrayList<>(degree);
        }

        @Override
        public void insert(K key, V value) {
            int loc = YUtils.binarySearch(keys(), key);
            int addLoc = loc >= 0 ? loc + 1 : -loc - 1;
            if (loc >= 0) {
                values.set(loc, value);
            } else {
                keys.add(addLoc, key);
                values.add(addLoc, value);
            }
            // 判断是否触发分裂
            if (root.isOverflow()) {
                root = splitRoot(this, split());
            }
        }

        @Override
        public Object delete(K key, int keyLoc) {
            // B+树的删除，不关心父节点有没有找到，因为key如果存在一定在叶子节点中也有，同时删除不但要替换掉
            // 非叶子对应的key同时也要把叶子的key删除掉
            int loc = YUtils.binarySearch(keys(), key);
            if (loc >= 0) {
                keys().remove(loc);
                // 叶子节点删除不要忘记删除value
                values().remove(loc);
            }
            // B+tree是在叶子节点中先执行删除，在返回后继
            return keyNumber() == 0 ? null : keys().get(0);
        }

        @Override
        public V search(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            return loc >= 0 ? values.get(loc) : null;
        }

        @Override
        public void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer delIndexer) {
            // merge兄弟节点的所有key
            keys().addAll(sibling.keys());
            values().addAll(sibling.values());

            // 删除父节点的分裂的key
            parent.keys().remove(delIndexer.splitUpKeyIdx());
            ((BInnerNode) parent).children().remove(delIndexer.splitUpKeyIdx() + 1);
            // 从右向左合并，需要把兄弟节点的下一个引用换成自己的
            next = ((LeafNode) sibling).next;
        }

        @Override
        public void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer delIndexer) {

            // 获取后继key,需要删除，然后转移过来,因为第二层节点父节点的key都是叶子分裂节点的第一个元素，存在重复，不能直接使用
            int keyNumber = keyNumber();
            K leftFirstKey = rightSib.keys().remove(0);
            V leftFirstValue = rightSib.values().remove(0);
            keys().add(keyNumber, leftFirstKey);
            values().add(keyNumber, leftFirstValue);

            //父节点顶点key替换为右侧兄弟节点转移之后此时的第一个key
            K nLeftFirstKey = rightSib.keys().get(0);
            parent.keys().set(delIndexer.leftRotateUpKeyIdx(), nLeftFirstKey);
        }

        @Override
        public void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer delIndexer) {
            // 前继key转移，与左旋一样，b+树第二层非叶子节点的key与叶子节点中的key存在重复不能直接下移父节点的key
            int keyNumber = leftSib.keyNumber();
            K rightLastKey = leftSib.keys().remove(keyNumber - 1);
            V rightLastValue = leftSib.values().remove(keyNumber - 1);
            // 叶子节点旋转一定要注意value的值,不要漏掉
            keys().add(0, rightLastKey);
            values().add(0, rightLastValue);
            //父节点旋转key替换为前继key
            parent.keys().set(delIndexer.rightRotateUpKeyIdx(), rightLastKey);
        }

        @Override
        public BNode split() {
            // key截取的开始和结束位置
            int from = keyNumber() / 2, to = keyNumber();

            // new Node
            LeafNode rightSib = new LeafNode(degree);
            rightSib.keys().addAll(keys().subList(from, to));
            rightSib.values().addAll(values().subList(from, to));

            // GC
            keys().subList(from, to).clear();
            values().subList(from, to).clear();
            // 更改右侧兄弟的引用关系
            rightSib.next = next;
            next = rightSib;
            return rightSib;
        }

        @Override
        public List<V> values() {
            return values;
        }

        @Override
        public List<V> getRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy) {
            List<V> result = new LinkedList<V>();
            LeafNode node = this;
            while (node != null) {
                Iterator<K> kIt = node.keys().iterator();
                Iterator<V> vIt = node.values().iterator();
                while (kIt.hasNext()) {
                    K key = kIt.next();
                    V value = vIt.next();
                    int cmp1 = key.compareTo(beginKey);
                    int cmp2 = key.compareTo(endKey);
                    // 在左右边界范围内
                    if (((beginPolicy == RangePolicy.EXCLUSIVE && cmp1 > 0) || (beginPolicy == RangePolicy.INCLUSIVE && cmp1 >= 0))
                            && ((endPolicy == RangePolicy.EXCLUSIVE && cmp2 < 0) || (endPolicy == RangePolicy.INCLUSIVE && cmp2 <= 0)))
                        result.add(value);
                        // 右边界超出
                    else if ((endPolicy == RangePolicy.EXCLUSIVE && cmp2 >= 0)
                            || (endPolicy == RangePolicy.INCLUSIVE && cmp2 > 0))
                        return result;
                }
                // 继续获取下一个节点，进行范围查找
                node = (LeafNode) node.next;
            }
            return result;
        }

        @Override
        public String toString() {
            return keys.toString();
        }
    }

}

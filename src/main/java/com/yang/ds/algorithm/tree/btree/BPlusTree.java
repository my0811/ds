package com.yang.ds.algorithm.tree.btree;

import com.yang.ds.algorithm.tree.btree.index.BDelIndexer;
import com.yang.ds.algorithm.tree.btree.node.*;
import com.yang.ds.algorithm.utils.YUtils;

import java.util.*;

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
public class BPlusTree<K extends Comparable<? super K>, V> {
    public enum RangePolicy {
        EXCLUSIVE, INCLUSIVE
    }

    // root节点
    private BNode<K, V> root;
    // 树的阶
    private int degree = 5;

    public BPlusTree(int degree) {
        this.degree = degree;
        root = new LeafNode<>(degree);
    }

    public void insert(K key, V value) {
        root.insert(key, value);
    }

    public void delete(K key) {
        root.delete(key, -99);
    }

    public V search(K key) {
        return root.search(key);
    }

    public List<V> searchRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy) {
        return ((BPlusNode) root).getRange(beginKey, beginPolicy, endKey, endPolicy);
    }

    public String toString() {
        return BtreeStringBuilder.toString(root);

    }

    private BNode splitRoot(BNode origin, BNode rightSib) {
        InnerNode<K, V> newRoot = new InnerNode<>(degree);
        BKeyValue<K, V> splitKey = null;
        if (rightSib instanceof LeafNode) {
            splitKey = rightSib.getFirstLeftKey();
        } else {
            splitKey = rightSib.delFirstLeftKey();
        }
        newRoot.keys().add(splitKey.key());
        newRoot.children().add(origin);
        newRoot.children().add(rightSib);
        return newRoot;
    }

    private interface BPlusNode<K extends Comparable<? super K>, V> {
        List<V> getRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy);
    }

    private class InnerNode<K extends Comparable<? super K>, V> extends AbstractBInnerNode<K, V> implements BPlusNode<K, V> {
        protected InnerNode(int degree) {
            super(degree);
        }

        @Override
        public BNode<K, V> getChild(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            return children().get(childIdx);
        }

        @Override
        public BNode<K, V> delChild(int index) {
            return children().remove(index);
        }

        @Override
        public BNode<K, V> delFirstLeftChild() {
            return children().remove(0);
        }

        @Override
        public BNode<K, V> delLastRightChild() {
            return children().remove(keyNumber());
        }

        @Override
        public BDelIndexer createDelIndexer(int childIdx) {
            return BDelIndexerImpl.newDelIndexer(childIdx, keyNumber());
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

        private void insertChild(BNode<K, V> rightSib) {
            BKeyValue<K, V> splitKey = null;
            if (rightSib instanceof BLeafNode) {
                splitKey = rightSib.getFirstLeftKey();
            } else {
                splitKey = rightSib.delFirstLeftKey();
            }
            int loc = YUtils.binarySearch(keys(), splitKey.key());
            int childIdx = loc >= 0 ? loc + 1 : -loc - 1;
            insertKey(childIdx, splitKey);
            children().add(childIdx + 1, rightSib);
        }

        @Override
        public List<V> values() {
            throw new UnsupportedOperationException("b+ tree Inner node no values");
        }

        @Override
        public BKeyValue<K, V> getFirstLeftKey() {
            return new BKeyValuePair<>(keys().get(0));
        }

        @Override
        public BKeyValue<K, V> delFirstLeftKey() {
            return new BKeyValuePair<>(keys().remove(0));
        }

        @Override
        public BKeyValue<K, V> getLastRightKey() {
            return new BKeyValuePair<>(keys().get(keyNumber() - 1));
        }

        @Override
        public BKeyValue<K, V> delLastRightKey() {
            return new BKeyValuePair<>(keys().remove(keyNumber() - 1));
        }

        @Override
        public void insertKey(int index, BKeyValue<K, V> newKey) {
            keys().add(index, newKey.key());
        }

        @Override
        public BKeyValue<K, V> delKey(int index) {
            return new BKeyValuePair<>(keys().remove(index));
        }

        @Override
        public void setKey(int index, BKeyValue<K, V> replaceKey) {
            keys().set(index, replaceKey.key());
        }

        @Override
        public BKeyValue<K, V> getKey(int index) {
            return new BKeyValuePair<>(keys().get(index));
        }

        @Override
        public void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer delIndexer) {
            BInnerNode<K, V> innerSib = (BInnerNode<K, V>) sibling;
            BInnerNode<K, V> innerParent = (BInnerNode<K, V>) sibling;
            // 分裂的key还原
            sibling.insertKey(0, parent.getKey(delIndexer.splitUpKeyIdx()));

            // 合并key和children
            keys().addAll(innerSib.keys());
            children().addAll(innerSib.children());

            // 父节点删除分裂的key，并且删除分裂的孩子
            innerParent.delKey(delIndexer.splitUpKeyIdx());
            innerParent.delChild(delIndexer.splitUpKeyIdx() + 1);
        }

        @Override
        public void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer delIndexer) {
            BInnerNode<K, V> innerRightSib = (BInnerNode<K, V>) rightSib;
            // 获取旋转key
            BKeyValue<K, V> rotateKey = parent.getKey(delIndexer.leftRotateUpKeyIdx());
            // 旋转key下移
            insertKey(keyNumber(), rotateKey);

            // 后继key上移
            parent.setKey(delIndexer.leftRotateUpKeyIdx(), rightSib.delFirstLeftKey());

            // 后继节点孩子转移作为自己的最后一个孩子
            children().add(innerRightSib.delFirstLeftChild());
        }

        @Override
        public void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer delIndexer) {
            BInnerNode<K, V> innerLeftSib = (BInnerNode<K, V>) leftSib;

            // 获取旋转key
            BKeyValue<K, V> rotateKey = parent.getKey(delIndexer.rightRotateUpKeyIdx());

            // 旋转key下移
            insertKey(0, rotateKey);

            // 前继key上移
            parent.setKey(delIndexer.rightRotateUpKeyIdx(), leftSib.delLastRightKey());

            // 前继节点的最后一个孩子作为自己的第一个孩子
            children().add(0, innerLeftSib.delLastRightChild());
        }

        @Override
        public BNode split() {
            int from = subKeyFrom(), to = subKeyTo();
            // split new node
            BInnerNode rightSib = new InnerNode(degree);
            rightSib.keys().addAll(keys().subList(from, to));
            rightSib.children().addAll(children().subList(from + 1, to + 1));
            //GC
            keys().subList(from, to).clear();
            children().subList(from + 1, to + 1).clear();
            return rightSib;
        }

        @Override
        public List<V> getRange(K beginKey, RangePolicy beginPolicy, K endKey, RangePolicy endPolicy) {
            return ((BPlusNode) getChild(beginKey)).getRange(beginKey, beginPolicy, endKey, endPolicy);
        }
    }


    private class LeafNode<K extends Comparable<? super K>, V> extends AbstractBNode<K, V> implements BPlusNode<K, V> {
        private List<V> values;
        private BNode<K, V> next;

        public LeafNode(int degree) {
            super(degree);
            values = new ArrayList<>(degree);
        }

        @Override
        public void insert(K key, V value) {
            int loc = YUtils.binarySearch(keys(), key);
            int insertIdx = loc >= 0 ? loc + 1 : -loc - 1;
            if (loc >= 0) {
                values().set(loc, value);
            } else {
                insertKey(insertIdx, new BKeyValuePair<K, V>(key, value));
            }
            // 判断是否触发分裂
            if (root.isOverflow()) {
                root = splitRoot(this, split());
            }
        }

        @Override
        public BKeyValue<K, V> delete(K key, int keyLoc) {
            // B+树的删除，不关心父节点有没有找到，因为key如果存在一定在叶子节点中也有，同时删除不但要替换掉
            // 非叶子对应的key同时也要把叶子的key删除掉
            int loc = YUtils.binarySearch(keys(), key);
            if (loc >= 0) {
                keys().remove(loc);
                values().remove(loc);
            }
            return getFirstLeftKey();
        }


        @Override
        public V search(K key) {
            int loc = YUtils.binarySearch(keys(), key);
            return loc >= 0 ? values().get(loc) : null;
        }

        @Override
        public List<V> values() {
            return values;
        }

        @Override
        public BKeyValue<K, V> getFirstLeftKey() {
            return new BKeyValuePair<>(keys().get(0), values().get(0));
        }

        @Override
        public BKeyValue<K, V> delFirstLeftKey() {
            return new BKeyValuePair<>(keys().get(0), values().get(0));
        }

        @Override
        public BKeyValue<K, V> getLastRightKey() {
            return new BKeyValuePair<>(keys().get(keyNumber() - 1), values().get(keyNumber() - 1));
        }

        @Override
        public BKeyValue<K, V> delLastRightKey() {
            int idx = keyNumber() - 1;
            return new BKeyValuePair<>(keys().remove(idx), values().remove(idx));
        }

        @Override
        public void insertKey(int index, BKeyValue<K, V> newKey) {
            keys().add(index, newKey.key());
            values().add(index, newKey.value());
        }

        @Override
        public BKeyValue<K, V> delKey(int index) {
            return new BKeyValuePair<>(keys().remove(index), values().remove(index));
        }

        @Override
        public void setKey(int index, BKeyValue<K, V> replaceKey) {
            keys().set(index, replaceKey.key());
            values().set(index, replaceKey.value());
        }

        @Override
        public BKeyValue<K, V> getKey(int index) {
            return new BKeyValuePair<>(keys().get(index), values().get(index));
        }

        @Override
        public void merge(BNode<K, V> parent, BNode<K, V> sibling, BDelIndexer delIndexer) {
            LeafNode<K, V> leafSib = (LeafNode<K, V>) sibling;
            // merge兄弟节点的所有key
            keys().addAll(leafSib.keys());

            // merge兄弟节点的所有value
            values().addAll(leafSib.values());

            // 删除父节点的分裂的key
            parent.delKey(delIndexer.splitUpKeyIdx());
            ((BInnerNode) parent).delChild(delIndexer.splitUpKeyIdx() + 1);
            // 从右向左合并，需要把兄弟节点的下一个引用换成自己的
            next = leafSib.next;
        }

        @Override
        public void leftRotate(BNode<K, V> parent, BNode<K, V> rightSib, BDelIndexer delIndexer) {

            // 获取后继key,需要删除，然后转移过来,因为第二层节点父节点的key都是叶子分裂节点的第一个元素，存在重复，不能直接使用
            insertKey(keyNumber(), rightSib.delFirstLeftKey());

            //父节点顶点key替换为右侧兄弟节点转移之后此时的第一个key
            parent.setKey(delIndexer.leftRotateUpKeyIdx(), rightSib.getFirstLeftKey());
        }

        @Override
        public void rightRotate(BNode<K, V> parent, BNode<K, V> leftSib, BDelIndexer delIndexer) {
            // 前继key转移，与左旋一样，b+树第二层非叶子节点的key与叶子节点中的key存在重复不能直接下移父节点的key
            insertKey(0, leftSib.getLastRightKey());

            //父节点旋转key替换为前继key
            parent.setKey(delIndexer.rightRotateUpKeyIdx(), leftSib.delLastRightKey());
        }

        @Override
        public BNode split() {
            // key截取的开始和结束位置
            int from = subKeyFrom(), to = subKeyTo();

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
    }

    public static void main(String[] args) {
        BPlusTree<Integer, String> bpt = new BPlusTree<Integer, String>(5);
        bpt.insert(1, "a");
        bpt.insert(3, "c");
        bpt.insert(5, "d");
        bpt.insert(7, "e");
        bpt.insert(9, "b");
        bpt.insert(11, "b");
        bpt.insert(13, "b");
        bpt.insert(15, "b");
        bpt.insert(17, "b");
        bpt.insert(19, "b");
        bpt.insert(21, "b");
        bpt.insert(23, "b");
        bpt.insert(25, "b");
        System.out.println(bpt.toString());
        List<String> rangeList = bpt.searchRange(1, RangePolicy.INCLUSIVE, 5, RangePolicy.INCLUSIVE);
        System.out.println(Arrays.toString(rangeList.toArray(new String[]{})));
    }
}

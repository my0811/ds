package com.yang.ds.algorithm.hash;

/**
 *链表的实现相比较开放地址发，实现起来简单容易，但是查找因为链表的长度，时间复杂度应该是平均链表的长度m O(m)
 * */
public class YLinkedHashTable<K, V> extends AbstractHashTable<K, V> {

    public YLinkedHashTable(int size) {
        super(size);
    }

    public YLinkedHashTable() {
        super();
    }

    @Override
    void resize() {
        // 判断元素数是否已经大于设定的法制了,默认数组大小的0.75的比例就需要扩充数组了,防止数据过多造成更多的hash冲突,生成更长的链表,链表的查询是O(N)
        if (++size <= threshold) {
            return;
        }

        Object[] oldTab = table;
        int newThr = threshold << 1;
        int newCap = table.length << 1;

        // 阈值和数组的实际大小等比例扩大，保持加载因子的比例不变,同时扩大2倍,2^1,还是满足2的n次方，低位与运算还可以生效
        threshold = newThr >= MAX_CAPACITY ? MAX_CAPACITY : newThr;
        newCap = newCap >= MAX_CAPACITY ? MAX_CAPACITY : newCap;

        // 更改hash表为新扩容的hash表，老的hash表oldTab将在方法结束，会gc掉
        table = new Object[newCap];
        // 老的hash表中的所有数据需要重新再次以新的hash表的大小重新添加
        for (int i = 0; i < oldTab.length; i++) {
            if (oldTab[i] != null) {
                LinkedNode node = (LinkedNode) oldTab[i];
                while (node != null) {
                    putVal(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        // 扩容
        resize();
        putVal(key, value);
        size++;
    }

    /**
     * 添加值
     * @param key   新插入的key
     * @param value 新插入的值
     * */
    private void putVal(K key, V value) {
        int hash = hash(key);
        int idx = hash & table.length - 1;
        LinkedNode node = (LinkedNode) table[idx];
        LinkedNode nNode = new LinkedNode(key, value);
        // 没有则创建一个链表node，采用，链表法来解决hash冲突
        if (node == null) {
            table[idx] = nNode;
        } else if (!set(key, value, node)) {
            table[idx] = nNode;
            nNode.next = node;
        }
        size++;
    }

    /**
     * 修改已经存在的key
     * @param key   新插入的key
     * @param value 新插入的值
     * @param node 存在的链表
     * */
    private boolean set(K key, V value, LinkedNode node) {
        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public void remove(K key) {
        int hash = hash(key);
        int idx = hash & table.length - 1;
        LinkedNode node = (LinkedNode) table[idx];
        while (node != null) {
            if (node.key.equals(key)) {
                node = node.next;
                table[idx] = node;
                size--;
                break;
            }
            node = node.next;
        }
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        int idx = hash & table.length - 1;
        LinkedNode node = (LinkedNode) table[idx];
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            LinkedNode node = (LinkedNode) table[i];
            if (null == table[i]) {
                sb.append("null");
            } else {
                sb.append(node.toString());
            }
            if (i != table.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private class LinkedNode {

        K key;

        V value;

        LinkedNode next;

        private LinkedNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            LinkedNode node = this;
            sb.append("[");
            while (node != null) {
                if (null != node.next) {
                    sb.append(node.key).append(",");
                } else {
                    sb.append(node.key);
                }
                node = node.next;
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        AbstractHashTable<String, Integer> linkedHashTab = new YLinkedHashTable<>(2);
        System.out.println(linkedHashTab.toString());
        linkedHashTab.put("y1", 1);
        System.out.println(linkedHashTab);
        linkedHashTab.put("y2", 2);
        System.out.println(linkedHashTab);
        linkedHashTab.put("y2", 3);
        System.out.println(linkedHashTab);
    }
}

package com.yang.ds.algorithm.hash;

import com.yang.ds.algorithm.utils.YUtils;

/**
 * hash表实现，操作操作复杂度基本定格在O(1)
 * 解决hash冲突的方式采用开放地址的方式，具体实现就是线性探索
 * 这种情况解决hash冲突，会出现数据聚集的情况，所谓数据聚集就是说，冲突了之后，向冲突之后的位置找到没有数据占用的位置插入数据
 * 随着这种情况不断发生，会把很多存在hash值很大间隔的数据对应的索引也都占据了，虽然情况发生的频繁，将会越聚集越多，而其他的位置却可能
 * 和稀疏(就像马路上撞车了，导致一堆人都堵塞到这了)，导致插入和查找都会有很长的遍历才能找到位置，增加了插入和查找的复杂度
 *
 * 1.(线性探测) 如果遇到插入的位置出现元素占用，需要在冲突位置索引+1%size的新位置探测，如果没有数据占据则在此位置插入(再次取模是为了，如果索引越界了，可以循环回来)
 * 会出现数据聚集的情况
 *
 * 2.(二次探测)，对线性探测升级，解决线性探测产生聚集的问题，
 * 思路就是就是出现冲突开始向下一个索引进行探测，但是每次探测的位置不在是索引+1按个向下探测，因为这样会连续位置聚集太多，对线性探测
 * 会有改善，因为存在了间隔,具体实现就是每次探测下一个索引位置=(index+探测的步数^2%size) eg:
 * 第一次冲突,探测index+1
 * 第二次冲突,探测index+2^2
 * 第三次冲突,探测index+3^2
 * .....
 *这种思路又会出现更不好的问题，就是步长会越来越大，没法控制，如果探测步数越多，则越大，把这种现象叫做二次聚集，解决方案，不是最优也，目前最好的方案是再hash
 *
 * 3. 再hash
 * 基于二次探测步长的问题，进行优化，我们没测探测的补偿，不是固定的逐渐增加，而是根据元素的key再算出来一个步长，这样不同元素的步长经过hash都会尽量不一样
 * 从而相当于又对步长进行了一次hash
 * 算法专家，又牛逼了，说如下算法比较好:
 * stepSize=constant-key%size
 * 要求:
 * (质数,非0的整数,除了自己和1整除，没有其他公约数,否则就是合数,1既不是质数也不是合数)
 *
 * 1. 数组的长度必须是质数，因为只有质数，无论补偿是多少，都不会出现整除现象，不出现整除，也就说明不会循环，所谓循环就是不断的进行数组 [0,2,4,6],[0,2,4,6],[0,2,4,6]....
 * 这样循环，导致没有把数组的所有位置遍历到
 *
 * 2. constant 为质数
 *
 * 3 .stepSize不能出0，否则那就index+0的死循环，原地探索了
 *
 * **/
public class YOpenAddrHashTable<K, V> extends AbstractHashTable<K, V> {

    public YOpenAddrHashTable(int size) {
        // 判断size是否是质数，如果不是质数则转换成质数
        int primeSize = tableForPrimeSize(size);
        table = new Node[primeSize];
    }

    /**
     * 生成质数的大小的size作为数组的大小，为了满足再hash算法
     * @param size 传入的制定的size大小，如果不是质数，转换成比此size大的最小质数
     * */
    private int tableForPrimeSize(int size) {
        while (!YUtils.isPrime(size)) {
            if (size > MAX_CAPACITY) {
                throw new IllegalArgumentException("not exit prime size, beyond maxSize");
            }
            size = ++size;
        }
        return size;
    }

    @Override
    void resize() {
        Object[] oldTab = table;
        int newSize = tableForPrimeSize(table.length << 1);
        table = new Object[newSize];
        for (Object n : oldTab) {
            Node<K, V> node = (Node) n;
            if (node != null && !node.isDeleted()) {
                put(node.key, node.data);
            }
        }
        // 循环结束,oldTab gc掉
    }

    @Override
    public void put(K key, V value) {
        // 判断是否需要扩容
        if (isFull()) {
            // 扩容两倍，double
            resize();
        }
        // 计算hash值
        int hash = hash(key);
        // 再hash 获取冲突探测的 step
        int step = reHash(key);
        int idx = hash % table.length;
        Node tmp;
        while ((tmp = (Node) table[idx]) != null && !tmp.isDeleted()) {
            idx = (idx + step) % table.length;
        }
        table[idx] = new Node(key, value);
        size++;
    }

    @Override
    public void remove(K key) {
        Node<K, V> findNode = getNode(key);
        if (null != findNode) {
            findNode.delete();
            size--;
        }
    }

    @Override
    public V get(K key) {
        Node<K, V> findNode = getNode(key);
        return findNode == null ? null : findNode.getData();
    }

    private Node getNode(K key) {
        int hash = hash(key);
        int step = reHash(key);
        int idx = hash % table.length;
        Node<K, V> tmp;
        // 插入和查找的hash算法是一样的，如果移动到了null位置证明没有这个key对应的数据
        while ((tmp = (Node) table[idx]) != null && !tmp.isDeleted()) {
            if (tmp.getKey().equals(key)) {
                return tmp;
            }
            idx = (idx + step) % table.length;
        }
        return null;
    }

    /**
     * 再hash的核心算法
     * @param key 需要再次hash的key
     * */
    private int reHash(K key) {
        int constant = 7;
        return constant - (hash(key) % table.length);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            String str = table[i] == null ? "null" : table[i].toString();
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString();
    }


    /**
     * 数据存储node
     *
     * */
    private class Node<K, V> {
        private int del = 1;
        private V data;
        private K key;

        public Node(K key, V data) {
            this.data = data;
            this.key = key;
        }

        public void delete() {
            this.del = -1;
        }

        public boolean isDeleted() {
            return del == -1;
        }

        public V getData() {
            return data;
        }

        public K getKey() {
            return key;
        }

        public String toString() {
            return "[" + key + "," + data + "," + del + "]";
        }
    }

    public static void main(String[] args) {
        AbstractHashTable<String, String> table = new YOpenAddrHashTable<>(4);
        table.put("y1", "z1");
        table.put("y2", "z2");
        table.put("y3", "z3");
        table.put("y4", "z4");
        table.put("y5", "z5");
        System.out.println(table.toString());
        table.put("y6", "z6");
        /*table.remove("y2");*/
        System.out.println(table.get("y1"));
        System.out.println(table.toString());
    }
}

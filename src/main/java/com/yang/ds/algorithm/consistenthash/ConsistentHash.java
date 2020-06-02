package com.yang.ds.algorithm.consistenthash;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性hash算法实现，hash环大小为2^32,保证hash的分布段位有足够多,2^32方便取模运算
 * 1. 如果这个环太小，会发生所有的服务器的分段太小，聚集在一起，所以hash无论怎么散列最后都会聚集在一起
 *
 * 2. hash 算法的选取优先，分布式服务器ip可能比较连续，如果单纯用普通的hashCode的话，可能导致hash值都在一个范围呢
 * 所以机器和需要存储的违建的key 都会比较聚集都冲突分配的集中的位置
 *
 * 3. 所以说，也就是hash表，必须要保证hash表的长度非常大，这样才能容纳更多的分段的hash值，同时hash值也尽量的散列，这样
 * 才能保证不会总是hash到同一个段位上
 *
 * 4.数据每次存储算出hash值，对这个2^32的hash环取模，找到位置后，顺时针方向寻找服务器
 *
 * 5. 为了防止hash 环倾斜的情况下，可以补充虚拟节点来保证，比如hash环上服务器分布的都在,2^32一半以上的段位上分布，那就导致了
 * 另一半不均匀，可能前一半的所有hash的数据都存储到后一半的第一个服务器上
 *
 * 6. 虚拟节点，就是因为可能hash环倾斜，物理机有限，增加虚拟的节点堆物理机的映射,如果很多个虚拟节点，所以hash将会更均匀
 *
 * FNV1_32_HASH就是FNV 32位散列函数
 * NATIVE_HASH就是java.lang.String.hashCode()方法返回的long取32位的结
 * MD5的散列函数也是默认情况下一致性哈希的推荐算法
 * MURMURHASH, 曾经线上搞过，散列性更好一些
 *
 *
 * 7. 这里面实现的是用红黑树保存hash环上的节点，当然也可以直接用有序数组来搞，然后用数组的二分查找来确定服务器位置
 * 前提是数组的插入是O(N)的操作，插入的时候利用二分查找，返回插入位置，然后变成一个有序数组，因为我们保存hash环上的节点
 * 是初始化操作，不用保证插入的时候复杂度很低，只要差的时候复杂度在logN就好，数组二分查找索引定位更加轻量级
 *
 *
 * 1.如果hash环上的机器挂掉一个，那么不影响继续写入，只影响了挂掉的那个机器的部分读取，因为之前写入的数据都在那个节点上,但是此时数据还是
 * 可以继续向顺时针的方向继续添加数据，机器恢复的时候，读取数据就会存在这样一个问题，相当于短路了，机器挂掉之后的数据
 * 写入了挂掉机器顺时针方向的下一个节点了，但是读取挂掉这个节点之后存储的数据，相当于此时节点恢复了之后被截胡了，因为本来
 * 是要hash到挂掉的这个机器的下一个节点，所以这个时候有两种方案解决，1：如果宕机又恢复的这个节点没有数据，那么一定在他顺时针的下一个节点上
 * 可以做一次转发，另一种思路就是每个node ,都预留着其他机器ip的一个存储，如果hash不是本机ip段就保存到对应的ip段的位置上
 * 当宕机的机器又复活了之后去其他机器上找符合自己ip段的是否存在数据，如果存在就复制过来
 * 1)转发显然是每次请求都要做无谓的消耗，如果经常宕机，可能转发会越来越多，但是正常情下宕机也不会总经常发生，复制只会后台一次
 * 的批量操作，但是读请求确实非常多的发生，所以还是复制比较合算
 * 2)不复制值转发，会让数据不断的各种转发，而且也不是很一致，不是特别好，最后都乱了，每个机器上都存储了hash环上其他机器应该
 * 保存的数据
 * 3）数据在移动的过程可以采用redis的方案，移动过程中没有了，就进行重定向到已经移动的完的数据的node上，如果没移动完还可以在
 * 这个机器上获取数据，但是复制过程中，程序只需要保证静态条件不发生就可以了，比如这个数据还没有被移动,但是在判断的时候，还在移动，导致
 * 再读取没有了，那就sb了
 *
 *
 * */

public class ConsistentHash {
    /**
     * 返回一个0~2^32-1 大小的hash值，相当于对一个长度为2^32长度的数组或者hash环取模
     *  注意hash 环不能出现负数，怎么能对负数取模，除非是负数循环取模，如果是用位的与运算是可以的
     *
     *  下面方法相当于实现了2步：
     *   1.计算hash值，尽量散列
     *   2.对2^32取模
     *  我们普通的实现的hashMap中之所以用hash返回的值又进行了一次取模，是因为我们数组达不到那么大，又进行了一次压缩
     *
     *
     * 这个算法的实现逻辑可以不用管，从网上粘贴的，可以用正规的类库提供的算法直接使用，乱七八糟的位运算不用管了
     *
     */
    public static void main(String[] args) {
        // 没有虚拟节点

        // 存在虚拟节点
        withVirtualNode();
    }

    private static void withoutVirtualNode() {
        WithoutVirtualNode hashRing = new WithoutVirtualNode();
        hashRing.addServer("192.168.1.1");
        hashRing.addServer("192.168.1.2");
        hashRing.addServer("192.168.1.3");
        for (int i = 0; i < 100; i++) {
            String file = UUID.randomUUID().toString().substring(0, 10) + "文件";
            String server = hashRing.getServer(file);
            System.out.printf("%s 被路由到服务器:%s", file, server);
            System.out.println();
        }
    }

    private static void withVirtualNode() {
        WithVirtualNode hashRing = new WithVirtualNode();
        hashRing.addServer("192.168.1.1", 500);
        hashRing.addServer("192.168.1.2", 500);
        hashRing.addServer("192.168.1.3", 500);
        for (int i = 0; i < 100; i++) {
            String file = UUID.randomUUID().toString().substring(0, 10) + "文件";
            Node node = hashRing.getServer(file);
            System.out.printf("%s 被路由到虚拟节点:%s   %s", file, node.key, node.ip);
            System.out.println();
        }
    }


    public static long hashValue(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值,不要产生负数
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }


    /**
     * 没有虚拟节点
     * */
    static class WithoutVirtualNode {
        // 保存hash 环上的节点，
        SortedMap<Long, String> sortedMap = new TreeMap<>();

        public long hash(String key) {
            return hashValue(key);
        }


        public void addServer(String ip) {
            sortedMap.put(hash(ip), ip);
        }

        public String getServer(String key) {

            // 相当于返回红黑树一个比指定key大的第一个元素
            long hash = 0;
            if (sortedMap.tailMap(hash(key)).isEmpty()) {
                hash = sortedMap.firstKey();// 返回整棵树的最小节点
            } else {
                hash = sortedMap.tailMap(hash(key)).firstKey();
            }
            return sortedMap.get(hash);
        }
    }

    static class WithVirtualNode {
        // 保存hash 环上的节点,保存虚拟节点
        SortedMap<Long, Node> virtualMap = new TreeMap<>();

        public long hash(String key) {
            return hashValue(key);
        }

        public void addServer(String ip, int rps) {
            for (int i = 0; i < rps; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(ip).append("-Virtual-").append(i);
                Node node = new Node(ip, sb.toString());
                virtualMap.put(hash(node.key), node);
            }
        }

        public Node getServer(String key) {
            // 相当于返回红黑树一个比指定key大的第一个元素
            long hash = 0;
            if (virtualMap.tailMap(hash(key)).isEmpty()) {
                hash = virtualMap.firstKey();// 返回整棵树的最小节点
            } else {
                hash = virtualMap.tailMap(hash(key)).firstKey();
            }
            return virtualMap.get(hash);
        }
    }

    /**
     * 虚拟节点与真实节点的映射关系
     * */
    static class Node {
        String ip;
        String key;

        public Node(String ip, String key) {
            this.ip = ip;
            this.key = key;
        }
    }
}

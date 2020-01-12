package com.yang.ds.algorithm.linkedlist.iterator;

import java.util.Iterator;

/**
 * 迭代器设计模式，随便看看得啦，做个小练习，应该放到demo里面的
 */
public class LinkedListWithIteratorTest {
    public static void main(String[] args) {
        LinkedListWithIterator<String> linkedListWithIterator = new LinkedListWithIterator();
        linkedListWithIterator.addHead("how");
        linkedListWithIterator.addHead("are");
        linkedListWithIterator.addHead("you");
        Iterator<String> iterator = linkedListWithIterator.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}

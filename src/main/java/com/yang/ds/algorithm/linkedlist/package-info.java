/**
 * 1.链表必须有头指针，或者头和尾的指针
 * 2.双端链表，是有两个指针，头指针，和尾指针，两端都可以删除数据
 * 3.双向链表是每个节点的指针方向是双向的，区别于双端链表
 * 4.双端链表可以两端插入，删除只能从一端删除,同时也只能从一端开始遍历
 * 5.双向链表，两端都可以插入和删除,可以从两端遍历，以为引用链式双向的
 * 6.双向链表可以直接当做队列使用，头部添加尾部删除
 *
 * 双端与双向的区别：
 * 双端：表示可以在两端操作
 * 双向：是指链表的node具有双向指向，即指向头方向，也指向尾方向
 *
 *
 * 复杂度:链表VS数组
 * 1.如果链表的正常添加和删除都是在头和尾进行，则是O(1)的复杂度
 *
 * 2.如果是需要查找到具体的某一个节点删除，则遍历的复杂度是O(N)，数组如果不能根据下标删除，则2O(N),因为遍历+移动,如果
 * 数组是根据根据下标删除也是O(N)因为移动，所以链表的整体删除复杂度比数组低很多
 *
 * 3.链表的查找一定是O(N)
 *
 */
package com.yang.ds.algorithm.linkedlist;
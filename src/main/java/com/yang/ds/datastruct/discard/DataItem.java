package com.yang.ds.datastruct.discard;

/**
 * 234树的数据节项封装
 * */
@Deprecated
public class DataItem {

    // 节点里面具体的数据值
    public int data;

    public DataItem(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void display() {
        System.out.print("/" + data);
    }
}

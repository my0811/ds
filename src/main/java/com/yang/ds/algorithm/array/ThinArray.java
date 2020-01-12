package com.yang.ds.algorithm.array;

/**
 * I. 稀疏数组，浪费存储空间
 * <p>
 * <p>
 * <p>
 * <p>
 * 这种稀疏数组，是9行7列(63)的一个二维数组，但是真正使用到的数据只有5个，浪费了58个空间
 * 0  1  2  3  4  5  6
 * -------------------
 * 0|0  0  0  0  0  0  0
 * 1|0  3  0  0  0  0  0
 * 2|0  0  0  0  0  0  0
 * 3|1  4  0  0  0  0  0
 * 4|0  0  7  0  0  0  0
 * 5|0  0  0  0  0  5  0
 * 6|0  0  0  0  0  0  0
 * 7|0  0  0  0  0  0  0
 * 8|0  0  0  0  0  0  0
 * II .稀疏数组的压缩
 * 1. 这种方式相当于用时间换取空间，如果遍历所有元素打印出来的复杂度为O(N^3)
 * 2. 但是连续的内存空间却只有18个比原来的7*9=63个节省了好多倍连续空间的使用
 * <p>
 * 数  数  使
 * 组  组  用
 * 行  列  个
 * 数  数  数
 * ---------
 * 9   7   5  part1 ----->类似于表头，描述了原始的二维数组行数、列数、使用空间数
 * ---------
 * 1   1   3
 * 3   0   1
 * 3   1   4  part2 ----->开始描述每个元素所在的索引位置(索引从0开始)，以及值
 * 4   2   7
 * 5   5   5
 * -----------
 * 行  列  元
 * 索  索  素
 * 引  引  值
 */
public class ThinArray {
    public static void main(String[] args) {
        // 普通二维数组数组
        int[][] arr = new int[9][7]; //9行7列，每行7列
        arr[1][1] = 3;
        arr[3][0] = 1;
        arr[3][1] = 4;
        arr[4][2] = 7;
        arr[5][5] = 5;
        // 遍历打印普通二维数组
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println("____________________YZK___________________________");
        // 数组压缩(只有五个元素，五行就够了，加上表头的一个描述行)所以只要定义长度为6就可以了
        CompressNode[] compressNodes = new CompressNode[6];
        // 表头描述
        compressNodes[0] = new CompressNode(9, 7, 5);
        // 索引描述
        compressNodes[1] = new CompressNode(1, 1, 3);
        compressNodes[2] = new CompressNode(3, 0, 1);
        compressNodes[3] = new CompressNode(3, 1, 4);
        compressNodes[4] = new CompressNode(4, 2, 7);
        compressNodes[5] = new CompressNode(5, 5, 5);
        // 压缩数组遍历，要求需要同原来二维数组的打印一样的效果
        for (int i = 0; i < compressNodes[0].getRow(); i++) {// 一维遍历（和正常二维数组一样）
            for (int j = 0; j < compressNodes[0].getCol(); j++) {// 二维遍历（和正常二维数组一样）
                int k;
                for (k = 0; k < compressNodes.length; k++) {
                    // 根据一维和二维的坐标(i,j)判断是否能匹配上相同坐标的CompressNode
                    // 找出来数组中有一个node的row和col和当前(i,j)匹配的节点，找不到就证明(i,j)是空元素，用"0"代替(int数组默认值为0)
                    if (compressNodes[k].getRow() == i && compressNodes[k].getCol() == j) {
                        break;
                    }
                }
                if (k < compressNodes.length) {// k最大值为compressNodes.length，跳出循环的最后一次自增=数组长度
                    System.out.print(compressNodes[k].getVal() + "  ");
                } else {
                    System.out.print("0" + "  ");
                }
            }
            System.out.println();
        }
    }
}

/**
 * 用一个对象表示压缩对象的，一行数据的表示
 */
class CompressNode {
    private int row;
    private int col;
    private int val;

    public CompressNode(int row, int col, int val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getVal() {
        return val;
    }
}

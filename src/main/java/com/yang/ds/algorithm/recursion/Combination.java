package com.yang.ds.algorithm.recursion;

/**
 * 组合问题,同理与背包问题，就是排列组合的问题想明白就行了，这个了解一下就可以了
 * (n,k)=(n-1,k-1)+(n-1,k)
 * eg:(5,3)=(4,2)+(4,3)
 */
public class Combination {
    private char[] data; //需要组合的数据
    private boolean[] selects; //被选择数据

    public Combination(char[] data) {
        this.data = data;
        this.selects = new boolean[data.length];
    }

    public void doComb(int selectNum, int index) {
        // 边界值
        if (selectNum == 0) {// 找到了
            for (int i = 0; i < this.selects.length; i++) {
                if (selects[i]) {
                    System.out.print(data[i] + " ");
                }
            }
            System.out.println();
            return;
        }
        if (index >= data.length) { // 所有的组合都找完了
            return;
        }
        selects[index] = true;
        // n-1,k-1
        doComb(selectNum - 1, index + 1);
        selects[index] = false;
        // n-1,k
        doComb(selectNum, index + 1);
    }

    public static void main(String[] args) {
        new Combination(new char[]{'A', 'B', 'C', 'D', 'E'}).doComb(3, 0);
    }
}


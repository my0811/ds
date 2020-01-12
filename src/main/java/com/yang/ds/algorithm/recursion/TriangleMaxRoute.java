package com.yang.ds.algorithm.recursion;

import java.util.Random;

/**
 * 解决数字直角三角形的最大值路径
 * 1.每个数字只有从上到下或者右下方两条路径
 * <p>
 * 1
 * 2 3
 * 4 5 6
 * 7 8 9 10  ------->max =20
 */
public class TriangleMaxRoute {
    // 使用二维数组保存三角形数字
    private int[][] triangleArr = new int[3][3];
    Random random = new Random();

    /**
     * 初始化二维数组，随机数
     */
    public TriangleMaxRoute() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= i; j++) {
                triangleArr[i - 1][j - 1] = random.nextInt(10);
            }
        }
    }

    /**
     * 打印三角形
     */
    public void printTriangle() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= i; j++) {
                if (j != i)
                    System.out.print(triangleArr[i - 1][j - 1] + " ");
                else
                    System.out.print(triangleArr[i - 1][j - 1]);
            }
            System.out.println();
        }
    }

    /**
     * 为了便于理解，行和列的编号都是从
     */
    public int maxSumRoute(int i, int j) {
        if (i >= 2) {// 递归边界条件，数组行数为2行
            return triangleArr[i][j];
        }
        // 注意递归调用是方法的传入形参
        int x = maxSumRoute(i + 1, j); //向下走
        int y = maxSumRoute(i + 1, j + 1);//向右下走
        return Math.max(x, y) + triangleArr[i][j]; // 最大路径的数值之和
    }

    public static void main(String[] args) {
        TriangleMaxRoute triangleMaxRoute = new TriangleMaxRoute();
        triangleMaxRoute.printTriangle();
        int maxSum = triangleMaxRoute.maxSumRoute(0, 0);
        System.out.println("路径最大值为:" + maxSum);
    }
}

package test.ds.recursion;

import java.util.Random;

/**
 * 直角三角形，求最大路径问题
 * */
public class Traingle {

    private final static int ROWS = 3;
    private final static Random RDM = new Random();
    private final static int[][] traingleArr = new int[ROWS][ROWS];


    public Traingle() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j <= i; j++) {
                traingleArr[i][j] = RDM.nextInt(10);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j <= i; j++) {
                if (i != j) {
                    sb.append(traingleArr[i][j]).append(" ");
                } else {
                    sb.append(traingleArr[i][j]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int max(int i, int j) {
        // 递归边界
        if (i >= ROWS - 1 || j >= ROWS - 1) {
            return traingleArr[i][j];
        }

        int x = max(i + 1, j);// 下方
        int y = max(i + 1, j + 1);// 斜下方方向
        return Math.max(x, y) + traingleArr[i][j];
    }

    public int max() {
        return max(0, 0);
    }

    public static void main(String[] args) {

        Traingle traingle = new Traingle();
        System.out.println(traingle);
        System.out.println(traingle.max());
    }
}

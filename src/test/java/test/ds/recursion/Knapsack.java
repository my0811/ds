package test.ds.recursion;

import org.junit.Test;

/**
 *
 * 背包问题
 *
 * 1, 6, 9, 3, 4, 5 ,找出10的背包组合
 *
 *
 * */

public class Knapsack {


    private StringBuilder doKnapsack(int[] data, boolean[] selects, int aim, int idx, StringBuilder sb) {
        // 递归边界
        if (aim != 0 && idx >= data.length) {
            return sb;
        }
        if (aim == 0) {
            for (int i = 0; i < data.length; i++) {
                if (selects[i]) {
                    sb.append(data[i]).append(" ");
                }
            }
            sb.append("\n");
            return sb;
        }
        selects[idx] = true;
        doKnapsack(data, selects, aim - data[idx], idx + 1, sb);
        selects[idx] = false;
        doKnapsack(data, selects, aim, idx + 1, sb);
        return sb;
    }

    @Test
    public void doKnapsackTest() {
        int[] data = new int[]{1, 6, 9, 3, 4, 5};
        boolean[] selects = new boolean[data.length];
        String rs = doKnapsack(data, selects, 10, 0, new StringBuilder()).toString();
        System.out.println(rs);
    }
}

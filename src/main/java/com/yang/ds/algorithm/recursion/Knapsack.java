package com.yang.ds.algorithm.recursion;


/**
 * 递归处理背包问题
 * <p>
 * 解决问题：
 * 把不同重量的东西装进一个背包，且重量满足我们制定的重量
 *
 * 解决思路：采用递归处理
 * 1.目标重量和数组中每一个元素进行累计相减,当减为0说明就是一个合适的组合
 * 2.双递归函数，就是二叉树的思想，每次从数组0开始遇到组合成功的就返回，或者到数组最后都没有匹配到则返回
 * 3.返回到调用点之后，抛出当前索引用当前索引之前的结果再和当前索引的下一个节点继续同样的递归,递归的逻辑
 * 与上递归函数一样，还是从左面向数组的末端递归，同样的逻辑递归完，则第二个递归函数返回到调用点，这个函数退出
 * 返回上一层
 *
 */
public class Knapsack {

    // 用来存储被选择的数据组成的数组
    private int[] data;

    // 这个数组和上面的data数组长度一样，true表示已经选择
    private boolean[] selects;

    public Knapsack(int[] data) {
        this.data = data;
        this.selects = new boolean[data.length];
    }

    /**
     * aim 目标重量，
     * index 索引值
     */
    public void doKnapsack(int aim, int index) {
        // 边界条件
        if (aim != 0 && index >= data.length) {
            return;// 已经把所有的组合方案都测试了，没有找到合适的组合
        }
        if (aim == 0) {//找到了合适的组合
            for (int i = 0; i < selects.length; i++) {
                if (selects[i]) {//被选中的元素
                    System.out.print(data[i] + " ");
                }
            }
            // game over
            System.out.println();
            return;
        }
        selects[index] = true;
        doKnapsack(aim - data[index], index + 1);
        selects[index] = false;// 第一个元素和后面的所有元素都没有组合成功，放弃第一个元素的选择
        doKnapsack(aim, index + 1);// 上面数据放进背包对应的所有组合都没有，刨除掉第一个元素，从下一个开始找
    }

    public static void main(String[] args) {
        int[] arr = new int[]{11, 8, 7, 1};
        Knapsack knapsack = new Knapsack(arr);
        knapsack.doKnapsack(19, 0);
    }
}

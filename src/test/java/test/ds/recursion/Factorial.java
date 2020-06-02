package test.ds.recursion;

import org.junit.Test;

/**
 * 阶乘
 * */
public class Factorial {

    @Test
    public void factorialTest() {
        System.out.println(factorial(4));

    }

    private int factorial(int num) {
        if (num == 0) {
            return 1;
        }
        if (num == 1) {
            return 1;
        }
        return num * factorial(num - 1);
    }
}

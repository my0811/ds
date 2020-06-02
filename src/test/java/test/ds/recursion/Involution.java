package test.ds.recursion;


import org.junit.Test;

/**
 * 乘方的运算x^y =(x^2)^2^y
 *
 * eg: 2^8= (2^2)^4=(4^2)^2=(16^2)^1
 *
 * 采用递归的思想处理，代替循环，递归到y=1的时候则进行返回
 *
 * */
public class Involution {

    @Test
    public void powTest() {
        System.out.println(pow(2, 4));
    }

    /**
     * 递归前进段就已经计算好了，递归返回阶段，只是把递归前进段计算好的值返回来就可以了
     * */
    private int pow(int x, int y) {
        if (y == 0) {
            return 1;
        }
        if (y == 1) {
            return x;
        }
        int mod = y % 2;
        if (mod == 1) {
            return pow(x * x * x, y / 2);
        }
        return pow(x * x, y / 2);
    }
}

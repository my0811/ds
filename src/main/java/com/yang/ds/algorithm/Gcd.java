package com.yang.ds.algorithm;

/**
 * 欧几里得算法，求最大公约数
 * 1. 两个数大的除以小的
 * 2. 然后如果有余数，则除数为余数，被除数为上一次的小的数
 * eg:
 * 1. m/n=2
 * 2. n/2=?
 */
public class Gcd {

    public static void main(String[] args) {
        long m=gcd(15,9);
        System.out.println("9和15最大公约数是: "+m);
    }

    private static long gcd(long m, long n) {
        while (n != 0) {
            long rm = m % n;
            m = n;
            n = rm;
        }
        return m;
    }
}

package com.yang.ds.algorithm.demo;

import java.util.Stack;

/***
 *
 * 解析表达式
 * I.
 * 表达式分类
 * 1. 前缀表达式 -1 -2 -3
 * 2. 中缀表达式 1+2+4
 * 3. 后缀表达式 34+2*5+  计算机处理的方式，这样扫描一遍就可以了
 * 4.了解一下
 * */
public class AnalyticExpression {

    public static void main(String[] args) {
        String ex = "34.6+56.2+58*63-69";
        midToRear(ex);
        /**
         * - 69 + * 63 58 + 56.2 34.6
         * 从右向左看，懒得格式化了，这个就是直接出栈了，计算要从右向左看
         * =(34.6+56.2)(58*63)+69-
         * =(34.6+56.2)+(58*63)69-
         * =(34.6+56.2)+(58*63)-69
         * 运算结束
         * */
    }

    /**
     * 中缀表达式转换成后缀表达式
     */
    private static void midToRear(String input) {
        //step1. 初始化两个栈,运算符栈s1，s2存放中间结果
        Stack<Character> s1 = new Stack<>();
        Stack<Object> s2 = new Stack<>();

        // step2.从左到有扫描表达式
        int len = input.length();
        char c;
        char tmpChar;
        String number;// 临时存放操作数
        int numLastIndex = -1;
        for (int i = 0; i < len; i++) { // 遍历输入字符串
            c = input.charAt(i); // 获取到具体的一个字符
            // step 3.遇到的数字放入到s2
            if (Character.isDigit(c)) { // 判断字符是否是数字类型,表示这是一个操作数
                numLastIndex = getNumLastIndex(input, i);//获取到完整数字之最后的操作符号的索引，然后截取字符串
                number = input.substring(i, numLastIndex);// sustring 为[)左闭右开
                s2.push(number);
                i = numLastIndex - 1;
                // step4操作符的处理 (+,-,*,/)
            } else if (isOperator(c)) {
                // step4 a. 如果s1栈不为空,并且栈顶元素不为'('并且优先级比栈顶元素符号优先级小就弹出，压入s2
                while (!s1.isEmpty() && c != '(' && priorityCompare(c, s1.peek()) <= 0) {
                    s2.push(s1.pop());
                }
                // step4 b 否则压入s1
                s1.push(c);
            }
            // step5 a.遇到左括号直接入栈
            else if (c == '(') {
                s1.push(c);
            }
            // step 5 b.如果遇到的括号是右括号一次弹出s1栈顶元素，压入s2,直到遇到左括号为止，然后将这对括号去掉
            else if (c == ')') {
                while ((tmpChar = s1.pop()) != '(') {
                    s2.push(tmpChar);
                }
            } else {

            }
        }
        // step6. 把s1中剩余的操作符全部添加到s2操作栈中
        while (!s1.isEmpty()) {
            s2.push(s1.pop());
        }
        // last 打印
        for (; !s2.isEmpty(); ) {
            System.out.print(s2.pop() + " ");
        }
    }

    /**
     * 第一位操作运算符优先级大于第二位的话返回1 等于返回0，小于返回-1
     */
    private static int priorityCompare(char opt1, char opt2) {
        if (opt1 == '+' || opt1 == '-') {
            return (opt2 == '*' || opt2 == '/') ? -1 : 0;
        }
        if (opt2 == '+' || opt2 == '-') {
            return (opt1 == '*' || opt1 == '/') ? 1 : 0;
        }
        return 1;
    }

    /**
     * 判断是否是运算符
     */
    private static boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/') {
            return true;
        }
        return false;
    }

    private static int getNumLastIndex(String input, int start) {
        int len = input.length();
        char c;
        int rs = -1;
        for (int i = start; i < len; i++) {
            c = input.charAt(i);
            if (!Character.isDigit(c) && c != '.') {
                rs = i;
                break;
            } else if (i == len - 1) {
                return len;
            }
        }
        return rs;
    }
}

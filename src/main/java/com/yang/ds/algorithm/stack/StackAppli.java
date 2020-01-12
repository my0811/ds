package com.yang.ds.algorithm.stack;


import java.util.Stack;

/**
 * 栈数据结构的应用
 * 1. 主要是使用jdk自带的栈进行
 */
public class StackAppli {

    public static void main(String[] args) {
        strRever();
        String code = " public static void main (String [] args{System.out.println(\"hello world\");List<String> list= new ArrayList<>;}";
        methodCheck(code);
    }

    /**
     * 字符串反转
     */
    private static void strRever() {
        String str = "hello world";
        Stack<Character> cStack = new Stack<>();
        char[] charr = str.toCharArray();
        System.out.println(str);
        for (Character cn : charr) {
            cStack.push(cn);
        }
        for (; !cStack.isEmpty(); ) {
            System.out.print(cStack.pop());
        }
        System.out.println();
    }

    /**
     * 模拟java编译的时候对方法进行语法检查
     * // ({()<><>}
     */

    private static void methodCheck(String code) {

        if (code == null || "".equals(code)) {
            throw new IllegalArgumentException("code is null");
        }
        boolean flag = false;
        // 代码数组
        Stack<Character> codeStack = new Stack<>();
        char[] codeArr = code.toCharArray();
        for (Character cn : codeArr) {
            // 左开入栈
            if (cn == '<' || cn == '{' || cn == '[' || cn == '(') {
                codeStack.push(cn);
            }
            //右开，出栈
            if (cn == '>' || cn == '}' || cn == ']' || cn == ')') {
                Character c = codeStack.pop();
                if ((cn == '>' && c == '<') || (cn == '}' && c == '{') || (cn == ']' && c == '[') || (cn == ')' && c == '(')) {
                    flag = true;
                } else {
                    flag = false;
                    System.out.println("error match !c=" + c + " cn=" + cn);
                }
            }

        }
        // 方法结构有问题，栈如果不为空，代表没有完全匹配上
        if (!codeStack.isEmpty() || flag == false) {
            System.out.println("grammar is error!!");
        } else {
            System.out.println("grammar check is pass");
        }


    }

}

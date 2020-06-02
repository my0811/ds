package com.yang.ds.algorithm.alibaba;


/**
 * 头条，面试题，
 *
 * [1-n]数组长度为n，找出不重复的元素，O(N),空间O(1)
 *
 * */
public class First {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int n = 7;
        int[] a = new int[]{6,2,4,1,2,2,5};
        int i = 0;

        //采用while循环
        while(i < n){
            //由于元素取值范围为[1,N]，因此，可以将（当前元素值-1）作为下标值，找到相应位置处的元素，将其存储的值作为-times，因为原来存储值都为正值，为防止混淆，用负值存储
            int temp = a[i] - 1;
            if(temp < 0){ //表示该元素已经处理过了，跳过
                i++;
                continue;
            } else if(a[temp] > 0){//第一次处理一个值
                a[i] = a[temp];//暂存新元素
                a[temp] = -1;
            } else {//已经不是第一次处理该值了

                a[i] = 0; //没有新的元素要处理，置0
                a[temp]--;
            }
        }

        for(int j = 0; j < n; ++j){
            System.out.print(j+1 + " , " + -a[j] + "\t");
        }
    }
}

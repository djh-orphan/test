package com.djh.test;

import java.util.Scanner;

public class YangHui {
    public static void main(String[] args) {
        //YangHui
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int yanghui[][] = new int[a][];
        for (int j = 0; j < yanghui.length; j++) {
            yanghui[j] = new int[j + 1];
            yanghui[j][0] = 1;
            yanghui[j][j] = 1;
        }
        for (int j = 2; j < yanghui.length; j++) {
            int[] ints = yanghui[j];
            for (int k = 1; k < ints.length - 1; k++) {
                yanghui[j][k] = yanghui[j - 1][k - 1] + yanghui[j - 1][k];
            }

        }
        for (int j = 0; j < yanghui.length; j++) {
            int[] ints = yanghui[j];
            for (int k = 0; k < ints.length; k++) {
                int anInt = ints[k];
                if (j == k)
                    System.out.println(anInt + "\t");
                else
                    System.out.print(anInt + "\t");
            }

        }
    }
}

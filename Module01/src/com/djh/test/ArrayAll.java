package com.djh.test;

import java.util.Arrays;

public class ArrayAll {
    public static void main(String[] args) {
        int a[][] = new int[2][];
        int numa = 0;
        a[0] = new int[]{1, 2, 3, 4, 5};
        a[1] = new int[]{1, 2, 3, 4, 5};
        for (int j = 0; j < a.length; j++) {
            int[] ints = a[j];
            for (int k = 0; k < ints.length; k++) {
                int anInt = ints[k];
                numa += anInt;
            }
        }
        System.out.println("numa=" + numa);
        String s = Arrays.toString(a[0]);
        System.out.println(s.charAt(1));
//        System.out.println(); Arrays.toString(Arrays.parallelSort(a[0]));
        Arrays.sort(a[0]);
        s = Arrays.toString(a[0]);
        System.out.println(s);
    }

}

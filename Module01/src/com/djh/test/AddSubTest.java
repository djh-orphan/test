package com.djh.test;

public class AddSubTest {
    public static void main(String[] args) {
        //自加自减测试
        int i1 = 10;
        int i2 = 20;
        int i = i1++;
        System.out.println("i=" + i);
        System.out.println("i1=" + i1);
        i = ++i1;
        System.out.println("i=" + i);
        System.out.println("i1=" + i1);
        i = i2--;
        System.out.println("i=" + i);
        System.out.println("i2=" + i2);
        i = --i2;
        System.out.println("i=" + i);
        System.out.println("i2=" + i2);

    }
}

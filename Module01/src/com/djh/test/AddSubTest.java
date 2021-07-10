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

        //array 默认值测试
        //测试发现，除char数组之外，其余数组输出首地址，char数组输出第一个元素
        String st[] = new String[20];
        st[0] = new String();
        System.out.println(st[0]);
        char a[] = new char[10];
        System.out.println(a);
        Student s[] = new Student[20];
//        s[0].ID=1;
        System.out.println(s[0]);

    }
}

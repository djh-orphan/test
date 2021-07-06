package com.djh.test;

public class Person {
    String name;
    int age;
    private int sex;

    public void study() {
        System.out.println("studying");
    }

    public int setSex(int i) {
        sex = i;
        return sex;
    }

    public void showAge() {
        System.out.println(age);
    }

    public int addAge(int i) {
        return age = i + 2;
    }

    public int showProof(int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (j == n - 1)
                    System.out.println("*\t");
                else
                    System.out.print("*\t");
            }
        }
        return m * n;
    }
}

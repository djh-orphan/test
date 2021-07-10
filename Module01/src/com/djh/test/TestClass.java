package com.djh.test;


import java.util.Scanner;

public class TestClass {
    public static void main(String[] args) {
        //测试一
//        Person a=new Person();
//        a.age=20;
//        a.name="djh";
//        a.study();
//        a.showAge();
//        a.setSex(0);
//        System.out.println(a.addAge(a.age));
//        System.out.println(a.age);
//        System.out.println(a.showProof(5,6));


        //测试二
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入尺寸大小:\t");
        int size = sc.nextInt();

        Student stu[] = new Student[size];
        for (int i = 0; i < size; i++) {
            stu[i] = new Student();
            stu[i].ID = i + 1;
            /**
             * 生成指定区间整数随机数的办法 [min,max]
             * 1）不是从0开始
             * 使用(int)(Math.raandom()*max%(max-min+1)+min)
             * 2)从0开始
             * 使用(int) Math.round(Math.random()*max%(max-min+1)+min)
             */
            stu[i].score = (int) Math.round(Math.random() * 750 % 751);
            stu[i].state = (int) (Math.random() * 9 % 9 + 1);

        }

        long sta, end;
        Student stu1[] = new Student[stu.length];
        System.arraycopy(stu, 0, stu1, 0, stu.length);


//        冒泡排序
        sta = System.nanoTime();
        bubbleSort(stu);
        end = System.nanoTime();
        System.out.println(end - sta);
        System.out.println("冒泡排序的时间" + (double) (end - sta) + "ns");

//        快速排序

        sta = System.nanoTime();
        quickSort(stu1, 0, stu.length - 1);
        end = System.nanoTime();
        System.out.println(end - sta);
        System.out.println("快速排序的时间" + (double) (end - sta) + "ns");


//        for (int i = 0; i < size; i++) {
//            System.out.println(i + 1 + ":" + stu1[i].ID + ":" + stu1[i].score);
//        }
        //参数传递测试
        Circle c = new Circle();
        PassObject pass = new PassObject();
        pass.printAreas(c, 5);

        //阶乘测试
        int n = jiecheng(3);
        System.out.println(n);

//        Scanner scs = new Scanner(System.in);
//        System.out.println(sc.hasNext());
//        while (!scs.hasNext("exit")){
////            System.out.println("you");
//            String word=scs.nextLine();
//            System.out.println(word);
////            scs=new Scanner(System.in);
//        }

        int i;
        while ((i = sc.nextInt()) != 0) {
            System.out.println(i);
        }
    }


    public static void bubbleSort(Student[] stu) {
        Student st;
        for (int i = 0; i < stu.length; i++) {
            for (int j = 0; j < stu.length - 1 - i; j++) {
                if (stu[j].score < stu[j + 1].score) {
                    st = stu[j];
                    stu[j] = stu[j + 1];
                    stu[j + 1] = st;
                }
            }
        }
    }


    public static void quickSort(Student[] stu, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex)
            return;
        Student st;
        //注意：leftIndex和rightIndex是要排序的起点和终点，不可以直接拿来使用，需要单独赋值后使用
        int left = leftIndex;
        int right = rightIndex;
        st = stu[left];
        while (left < right) {
            while (left < right && stu[right].score >= st.score) {
                right--;
            }
            stu[left] = stu[right];
            while (left < right && stu[left].score <= st.score) {
                left++;
            }
            stu[right] = stu[left];
        }
        stu[left] = st;
        //注意：迭代过程中left和right会改变，需要对left进行存储
        int temp = left;
        quickSort(stu, leftIndex, temp - 1);
        quickSort(stu, temp + 1, rightIndex);
    }

    //阶乘算法的递归实现
    public static int jiecheng(int n) {
        if (n == 1)
            return 1;
        else
            return n * jiecheng(n - 1);
    }


}

package com.djh.test;

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
        Student stu[] = new Student[20];
        for (int i = 0; i < 20; i++) {
            stu[i] = new Student();
            stu[i].ID = i + 1;
            stu[i].score = (int) Math.round(Math.random() * 100 % 101);
            stu[i].state = (int) (Math.random() * 9 % 9 + 1);
//            System.out.print(stu[i].ID+":"+stu[i].score+"\t");
//            if (stu[i].state == 3)
//                System.out.println(stu[i].ID + "\t");
        }
        Student st;
        for (int i = 0; i < stu.length; i++) {
//            int j=i+1;
            for (int j = 0; j < stu.length - 1 - i; j++) {
                if (stu[j].score < stu[j + 1].score) {
                    st = stu[j];
                    stu[j] = stu[j + 1];
                    stu[j + 1] = st;
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            System.out.println(stu[i].ID + ":" + stu[i].score);
        }
    }
}

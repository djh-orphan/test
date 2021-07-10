package com.djh.test;

/**
 * @author duan
 * @create 2021-07-10 9:55
 */
public class PassObject {
    public void printAreas(Circle c, int time) {
        System.out.println("Radius\t\tAreas");
        for (int i = 1; i <= time; i++) {
            c.radius = i;
            double area = c.findArea();
            System.out.println(c.radius + "\t\t" + area);
        }
    }
}

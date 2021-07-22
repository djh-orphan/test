package com.djh.test;

import java.util.Scanner;

/**
 * @author duan
 * @create 2021-07-21 16:30
 */


public class ScannerTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String ss = sc.nextLine();
            System.out.println(ss);
        }
    }
}

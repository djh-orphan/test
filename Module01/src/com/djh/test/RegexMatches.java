package com.djh.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
    public static void main(String[] args) {

        // 按指定模式在字符串查找
//        String line = "This order was placed for QT3000! OK?";
//        String line = "/to     wei   love you!";
////        String pattern = "(\\D*)(\\d+)(.*)";
//        String pattern = "(^/to)(\\s+)(\\S+\\b)(\\s+)(.*)";
//        // 创建 Pattern 对象
//        Pattern r = Pattern.compile(pattern);
//
//        // 现在创建 matcher 对象
//        Matcher m = r.matcher(line);
//        if (m.find()) {
//            System.out.println("Found value: " + m.group(0));
//            System.out.println("Found value: " + m.group(1).length());
//            System.out.println("Found value: " + m.group(3));
//            System.out.println("Found value: " + m.group(5));
//        } else {
//            System.out.println("NO MATCH");
//        }
        String line1 = "//hi      weu ";
//        String pattern1="(^/to)(\\s+)(\\S+\\b)(\\s+)(.*)";
//        String pattern1 = "(^/history)(\\s*)(\\d*)(\\s*)(\\d*)";
        String pattern1 = "(^//\\S+\\b)(\\s*)(\\S*)";

        // 创建 Pattern 对象
        Pattern r1 = Pattern.compile(pattern1);

        // 现在创建 matcher 对象
        Matcher m1 = r1.matcher(line1);
        if (m1.find()) {
            System.out.println("Found value: " + m1.group(0));
            System.out.println("Found value: " + m1.group(1));
            System.out.println(m1.group(2));
            System.out.println("Found value: " + m1.group(3));
            System.out.println(m1.groupCount());
        } else {
            System.out.println("NO MATCH");
        }
    }
}



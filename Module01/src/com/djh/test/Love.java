package com.djh.test;

//基础变量的定义包含声明以及初始化（指赋值）
//引用变量的定义包含声明以及初始化（指引用）
// 基础变量在声明时不会被初始化默认值，需要显式赋值；
// 数组是引用型变量，一旦初始化（指的是创建内存空间并引用）如果没有显式初始化则会对每个元素进行初始化为默认值
// 成员变量会在初始化时被赋予默认值，局部变量一般不会
// 数组的每个元素就相当于成员变量
public class Love {
    public int i;

    public static void main(String[] args) {
// heart

        System.out.print("\t*\t\t\t\t\t\t\t\t*\t\n*\t\t*\t\t" +
                "I Love java\t\t*\t\t*\n" +
                "\t*\t\t\t\t\t\t\t\t*\t\n" +
                "\t  *\t\t\t\t\t\t\t  *\t\n" +
                "\t    *\t\t\t\t\t\t*\t\n" +
                "\t      *\t\t\t\t\t  *\t\n" +
                "\t        *\t\t\t\t*\t\n" +
                "\t          *\t\t\t  *\t\n" +
                "\t            *\t\t*\t\n" +
                "\t              \t*\t\n");

        int a[] = new int[2];
        System.out.println(a[1]);
//        long a=1221323222220222222L;
//        char a = 65;
//        byte b = 12;
//        byte c = 10;
//        c++;
//        byte d = (byte)(b + c);
//        System.out.println(getType(c));
//        System.out.println(c);
//        System.out.println("5+5="+5+5);



//打印数字
        int num;
        int n;
        n = num = 234;
//        n,num=23,34; erro
        num += 1;

        int num1 = num / 100;

        int num2 = num % 100 / 10;
        int num3 = num % 10;
        System.out.println(num1);
        System.out.println(num2);
        System.out.println(num3);

//        int x = 1;
//        int y = 1;
        boolean x = true;
        boolean y = false;
        short z = 42;
        //在if内写y=true 会给y重新赋值true并返回true
        if ((z++ == 42) && (y = true)) {
            z++;
            System.out.println(y);
        }
//        if ((x = false) || (++z == 45)) z++;

        int n1 = 100;
        int n2 = 1223;
        int n3 = 123;
        int max = (n1 >= n2) ? ((n1 >= n3) ? n1 : n3) : ((n2 >= n3) ? n2 : n3);
        System.out.println("z=" + max);
        boolean b = true;
//如果写成if(b=false)可以通过，b=false 给b赋值的同时返回false
        if (b = false)
            System.out.println("a");
        else if (b)
            System.out.println("b");
        else if (!b)
            System.out.println("c");
        else
            System.out.println("d");


//        Scanner sc=new Scanner(System.in);
//        int ww=sc.nextInt();
//        System.out.println(ww);

        int as = 10;
        switch (as) {
            case 9:
                System.out.println(as);
                break;
            case 10:
                System.out.println(3 + 4 + "as");
                break;
            default:
                break;
        }


    }

    public static String getType(Object o) { //获取变量类型方法

        return o.getClass().toString(); //使用int类型的getClass()方法

    }

}

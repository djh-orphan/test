package com.djh.test;


public class Love {
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
        //if(y == true)
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

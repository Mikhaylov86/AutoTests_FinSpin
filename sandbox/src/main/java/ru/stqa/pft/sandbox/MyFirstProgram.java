package ru.stqa.pft.sandbox;

public class MyFirstProgram {


    public static void main(String[] args) {

        hello("world");
        hello("user");
        int len3 = 5;
        System.out.println("Площадь квадрата со стороной " + len3 + "=" + area(len3));


        int a = 2;
        int b = 3;
        System.out.println("Площадь прямоугольника со сторонами:"+a+"и"+b+ " = " + area(a,b));



        double d = 8.2;
        double s = d * d;
        System.out.println("Площадь квадрата со стороной " + d + "=" + s);

    }

    public static void hello(String somebody) {
        System.out.println("Hello, " + somebody + "!");
    }

    public static int area(int a) {
        int s = a * a;
        return s;
    }

    public static int area(int a, int b) {
        int s = a * b;
        return s;
    }

}

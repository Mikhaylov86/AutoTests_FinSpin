package ru.stqa.pft.sandbox;

public class MyFirstProgram {


    public static void main(String[] args) {

        hello("world");
        hello("user");

        Square s = new Square();
        s.len = 5;
        System.out.println("Площадь квадрата со стороной " + s.len + "=" + area(s));

        Rectangle r = new Rectangle();
        r.a = 2;
        r.b = 3;
        System.out.println("Площадь прямоугольника со сторонами:" + r.a + "и" + r.b + " = " + area(r));


        double d = 8.2;
        double p = d * d;
        System.out.println("Площадь квадрата со стороной " + d + "=" + p);

    }

    public static void hello(String somebody) {
        System.out.println("Hello, " + somebody + "!");
    }

    public static double area(Square s) {
        return s.len * s.len;
    }

    public static int area(Rectangle r) {
        return r.a * r.b;
    }

}

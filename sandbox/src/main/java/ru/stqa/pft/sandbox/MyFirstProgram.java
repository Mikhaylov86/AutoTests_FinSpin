package ru.stqa.pft.sandbox;

public class MyFirstProgram {


    public static void main(String[] args) {

        hello("world");
        hello("user");

        Square s = new Square(5);
        System.out.println("Площадь квадрата со стороной " + s.len + "=" + s.area());

        Rectangle r = new Rectangle(2,3);
        System.out.println("Площадь прямоугольника со сторонами:" + r.a + "и" + r.b + " = " + r.area());


        double d = 8.2;
        double p = d * d;
        System.out.println("Площадь квадрата со стороной " + d + "=" + p);

    }

    public static void hello(String somebody) {
        System.out.println("Hello, " + somebody + "!");
    }

}

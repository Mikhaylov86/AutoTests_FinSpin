package ru.stqa.pft.sandbox;

public class Square {
    public double len;
    public Square(double len){
        this.len = len;
    }

    public  double area() {
        return this.len * this.len;
    }
}

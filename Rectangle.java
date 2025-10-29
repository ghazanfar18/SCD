package com.mycompany.rectangle;
//2023F-BSE-103
public class Rectangle {
    private double length = 1.0;
    private double width = 1.0;
    public void setLength(double length) {
        if (length > 0.0 && length < 20.0) {
            this.length = length;
        } else {
            System.out.println("Invalid length. Must be > 0.0 and < 20.0");
        }
    }
    public void setWidth(double width) {
        if (width > 0.0 && width < 20.0) {
            this.width = width;
        } else {
            System.out.println("Invalid width. Must be > 0.0 and < 20.0");
        }
    }
    public double getLength() {
        return length;
    }
    public double getWidth() {
        return width;
    }
    public double calculateArea() {
        return length * width;
    }
    public double calculatePerimeter() {
        return 2 * (length + width);
    }
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle();
        rectangle.setLength(5.5);
        rectangle.setWidth(3.2);

        System.out.println("Length: " + rectangle.getLength());
        System.out.println("Width: " + rectangle.getWidth());
        System.out.println("Area: " + rectangle.calculateArea());
        System.out.println("Perimeter: " + rectangle.calculatePerimeter()); }}
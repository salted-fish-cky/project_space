package com.cky.spring.bean;

public class Car {
    private String brand;
    private String crop;
    private double price;
    private int maxSpeed;

    public Car(String brand, String crop, double price) {
        this.brand = brand;
        this.crop = crop;
        this.price = price;
    }

    public Car(String brand, String crop, int maxSpeed) {
        this.brand = brand;
        this.crop = crop;
        this.maxSpeed = maxSpeed;
    }

    public Car(){}

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", crop='" + crop + '\'' +
                ", price=" + price +
                ", maxSpeed=" + maxSpeed +
                '}';
    }
}

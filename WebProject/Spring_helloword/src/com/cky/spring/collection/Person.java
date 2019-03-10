package com.cky.spring.collection;

import com.cky.spring.bean.Car;

import java.util.List;

public class Person {
    private String name;
    private int age;
    private List<Car> cars;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCar(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCar() {
        return cars;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", car=" + cars +
                '}';
    }
}

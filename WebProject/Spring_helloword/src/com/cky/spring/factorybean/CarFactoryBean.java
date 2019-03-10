package com.cky.spring.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 自定义的FactoryBean
 */
public class CarFactoryBean implements FactoryBean<Car> {

    private String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    //返回bean的对像
    @Override
    public Car getObject() throws Exception {
        return new Car("BMW",500000);
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

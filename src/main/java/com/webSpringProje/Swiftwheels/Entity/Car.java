package com.webSpringProje.Swiftwheels.Entity;

import jakarta.persistence.Entity;

@Entity
public class Car extends Vehicle {
    private String seri;
    private String fuel;

    public Car(String seri, String fuel) {
        this.seri = seri;
        this.fuel = fuel;
    }

    public Car(Long id, String make, String model, Integer year, Integer km, String color, String enginePower, String engineCapacity, Integer price, Integer stock, byte[] image, String seri, String fuel) {
        super(id, make, model, year, km, color, enginePower, engineCapacity, price, stock, image);
        this.seri = seri;
        this.fuel = fuel;
    }

    public Car() {

    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
}
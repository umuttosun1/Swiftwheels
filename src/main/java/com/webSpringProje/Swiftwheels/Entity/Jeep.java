package com.webSpringProje.Swiftwheels.Entity;

import jakarta.persistence.Entity;

@Entity
public class Jeep extends Vehicle {
    private String seri;
    private String fuel;
    private String traction;

    public Jeep(String seri, String fuel, String traction) {
        this.seri = seri;
        this.fuel = fuel;
        this.traction = traction;
    }

    public Jeep(Long id, String make, String model, Integer year, Integer km, String color, String enginePower, String engineCapacity, Integer price, Integer stock, byte[] image, String seri, String fuel, String traction) {
        super(id, make, model, year, km, color, enginePower, engineCapacity, price, stock, image);
        this.seri = seri;
        this.fuel = fuel;
        this.traction = traction;
    }

    public Jeep() {

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

    public String getTraction() {
        return traction;
    }

    public void setTraction(String traction) {
        this.traction = traction;
    }
}

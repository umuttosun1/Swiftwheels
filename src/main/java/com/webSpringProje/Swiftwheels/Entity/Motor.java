package com.webSpringProje.Swiftwheels.Entity;

import jakarta.persistence.Entity;

@Entity
public class Motor extends Vehicle {
    private String type;
    private String cooling;
    private String cylinder;

    public Motor(String type, String cooling, String cylinder) {
        this.type = type;
        this.cooling = cooling;
        this.cylinder = cylinder;
    }

    public Motor(Long id, String make, String model, Integer year, Integer km, String color, String enginePower, String engineCapacity, Integer price, Integer stock, byte[] image, String type, String cooling, String cylinder) {
        super(id, make, model, year, km, color, enginePower, engineCapacity, price, stock, image);
        this.type = type;
        this.cooling = cooling;
        this.cylinder = cylinder;
    }

    public Motor() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCooling() {
        return cooling;
    }

    public void setCooling(String cooling) {
        this.cooling = cooling;
    }

    public String getCylinder() {
        return cylinder;
    }

    public void setCylinder(String cylinder) {
        this.cylinder = cylinder;
    }
}

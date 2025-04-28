package com.webSpringProje.Swiftwheels.DTO;

import org.springframework.web.multipart.MultipartFile;

public class MotorDTO {
    private String make;
    private String model;
    private Integer year;
    private Integer km;
    private String color;
    private String enginePower;
    private String engineCapacity;
    private Integer price;
    private Integer stock;
    private String type;
    private String cooling;
    private String cylinder;
    private MultipartFile image;

    public MotorDTO(String make, String model, Integer year, Integer km, String color, String enginePower, String engineCapacity, Integer price, Integer stock, String type, String cooling, String cylinder, MultipartFile image) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.km = km;
        this.color = color;
        this.enginePower = enginePower;
        this.engineCapacity = engineCapacity;
        this.price = price;
        this.stock = stock;
        this.type = type;
        this.cooling = cooling;
        this.cylinder = cylinder;
        this.image = image;
    }

    public MotorDTO() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}

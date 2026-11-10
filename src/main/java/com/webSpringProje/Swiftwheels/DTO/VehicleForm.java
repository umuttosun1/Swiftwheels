package com.webSpringProje.Swiftwheels.DTO;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Üç araç türü için tek form. Türe özgü alanlar (seri/yakıt/çekiş ya da
 * tip/soğutma/silindir) VehicleService'te türe göre zorunlu tutulur.
 */
public class VehicleForm {

    @NotBlank(message = "Marka zorunlu")
    @Size(max = 40)
    private String make;

    @NotBlank(message = "Model zorunlu")
    @Size(max = 40)
    private String model;

    @NotNull(message = "Yıl zorunlu")
    @Min(value = 1950, message = "1950 ve sonrası olmalı")
    @Max(value = 2100, message = "Geçerli bir yıl girin")
    private Integer year;

    @NotNull(message = "Kilometre zorunlu")
    @PositiveOrZero(message = "Negatif olamaz")
    private Integer km;

    @NotBlank(message = "Renk zorunlu")
    @Size(max = 30)
    private String color;

    @Size(max = 30)
    private String enginePower;

    @Size(max = 30)
    private String engineCapacity;

    @NotNull(message = "Günlük fiyat zorunlu")
    @Positive(message = "0'dan büyük olmalı")
    private Integer price;

    @NotNull(message = "Stok zorunlu")
    @PositiveOrZero(message = "Negatif olamaz")
    @Max(value = 1000, message = "En fazla 1000")
    private Integer stock;

    // araba + jeep
    @Size(max = 40) private String seri;
    @Size(max = 30) private String fuel;
    // jeep
    @Size(max = 20) private String traction;
    // motor
    @Size(max = 30) private String motorType;
    @Size(max = 20) private String cooling;
    @Size(max = 10) private String cylinder;

    private MultipartFile image;
    private boolean removeImage;

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Integer getKm() { return km; }
    public void setKm(Integer km) { this.km = km; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getEnginePower() { return enginePower; }
    public void setEnginePower(String enginePower) { this.enginePower = enginePower; }
    public String getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(String engineCapacity) { this.engineCapacity = engineCapacity; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getSeri() { return seri; }
    public void setSeri(String seri) { this.seri = seri; }
    public String getFuel() { return fuel; }
    public void setFuel(String fuel) { this.fuel = fuel; }
    public String getTraction() { return traction; }
    public void setTraction(String traction) { this.traction = traction; }
    public String getMotorType() { return motorType; }
    public void setMotorType(String motorType) { this.motorType = motorType; }
    public String getCooling() { return cooling; }
    public void setCooling(String cooling) { this.cooling = cooling; }
    public String getCylinder() { return cylinder; }
    public void setCylinder(String cylinder) { this.cylinder = cylinder; }
    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }
    public boolean isRemoveImage() { return removeImage; }
    public void setRemoveImage(boolean removeImage) { this.removeImage = removeImage; }
}

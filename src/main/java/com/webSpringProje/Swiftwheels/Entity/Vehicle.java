package com.webSpringProje.Swiftwheels.Entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // her türün kendi detay tablosu var
public abstract class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private Integer year;
    private Integer km;
    private String color;
    private String enginePower;
    private String engineCapacity;
    private Integer price;
    private Integer stock;
    // VARBINARY + büyük uzunluk: Hibernate bunu PostgreSQL'de bytea, MySQL'de longblob yapar.
    // (Eski "LONGBLOB" tanımı yalnızca MySQL'de çalışıyordu; LONG32VARBINARY ise
    //  PostgreSQL sürücüsünde null değer bağlanırken "Geçersiz Types değeri" hatası veriyordu.)
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "image", length = 10 * 1024 * 1024)
    private byte[] image;

    private String imageContentType;

    /** Adreslerde ve şablonlarda kullanılan tür. */
    public abstract VehicleType getVehicleType();

    /** Türe özgü alanlar, detay sayfasında gösterilmek üzere (etiket -> değer). */
    public abstract java.util.Map<String, String> getDetails();

    /** Boş değerleri atlayarak sıralı bir etiket/değer haritası kurar. */
    protected static java.util.Map<String, String> details(String... labelValuePairs) {
        java.util.Map<String, String> map = new java.util.LinkedHashMap<>();
        for (int i = 0; i + 1 < labelValuePairs.length; i += 2) {
            if (labelValuePairs[i + 1] != null && !labelValuePairs[i + 1].isBlank()) {
                map.put(labelValuePairs[i], labelValuePairs[i + 1]);
            }
        }
        return map;
    }

    public boolean hasImage() {
        return image != null && image.length > 0;
    }

    public String getTitle() {
        return make + " " + model;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Vehicle() {
    }

    public Vehicle(Long id, String make, String model, Integer year, Integer km, String color, String enginePower, String engineCapacity, Integer price, Integer stock, byte[] image) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.km = km;
        this.color = color;
        this.enginePower = enginePower;
        this.engineCapacity = engineCapacity;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

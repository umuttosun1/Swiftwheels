package com.webSpringProje.Swiftwheels.Entity;

import java.util.Arrays;
import java.util.Optional;

/** Araç türleri: adres parçası, Türkçe adlar ve ikon tek yerde. */
public enum VehicleType {
    CAR("cars", "Araba", "Arabalar", "bi-car-front"),
    JEEP("jeeps", "Jeep", "Jeepler", "bi-truck-front"),
    MOTOR("motors", "Motor", "Motorlar", "bi-scooter");

    private final String path;
    private final String label;
    private final String plural;
    private final String icon;

    VehicleType(String path, String label, String plural, String icon) {
        this.path = path;
        this.label = label;
        this.plural = plural;
        this.icon = icon;
    }

    public String getPath() { return path; }
    public String getLabel() { return label; }
    public String getPlural() { return plural; }
    public String getIcon() { return icon; }

    public static Optional<VehicleType> fromPath(String path) {
        return Arrays.stream(values()).filter(t -> t.path.equals(path)).findFirst();
    }
}

package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.VehicleForm;
import com.webSpringProje.Swiftwheels.Entity.*;
import com.webSpringProje.Swiftwheels.Repos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/** Araba, jeep ve motor için ortak araç işlemleri (eskiden üç ayrı servis vardı). */
@Service
public class VehicleService {

    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private final VehicleRepository vehicles;
    private final CarRepository cars;
    private final JeepRepository jeeps;
    private final MotorRepository motors;
    private final RentalRepository rentals;

    public VehicleService(VehicleRepository vehicles, CarRepository cars, JeepRepository jeeps,
                          MotorRepository motors, RentalRepository rentals) {
        this.vehicles = vehicles;
        this.cars = cars;
        this.jeeps = jeeps;
        this.motors = motors;
        this.rentals = rentals;
    }

    @Transactional(readOnly = true)
    public List<? extends Vehicle> list(VehicleType type) {
        return switch (type) {
            case CAR -> cars.findAll();
            case JEEP -> jeeps.findAll();
            case MOTOR -> motors.findAll();
        };
    }

    @Transactional(readOnly = true)
    public List<Vehicle> all() {
        return vehicles.findAll();
    }

    @Transactional(readOnly = true)
    public Map<VehicleType, Long> countsByType() {
        return Map.of(VehicleType.CAR, cars.count(), VehicleType.JEEP, jeeps.count(), VehicleType.MOTOR, motors.count());
    }

    @Transactional(readOnly = true)
    public Vehicle get(Long id) {
        return vehicles.findById(id).orElseThrow(() -> new NoSuchElementException("Araç bulunamadı: " + id));
    }

    /** Adresteki tür ile aracın türü uyuşmuyorsa 404 (ör. /cars/5 bir motor ise). */
    @Transactional(readOnly = true)
    public Vehicle get(VehicleType type, Long id) {
        Vehicle v = get(id);
        if (v.getVehicleType() != type) {
            throw new NoSuchElementException("Araç bulunamadı: " + id);
        }
        return v;
    }

    @Transactional(readOnly = true)
    public List<Vehicle> search(String q) {
        return q == null || q.isBlank() ? List.of() : vehicles.search(q.trim());
    }

    /** Türe özgü zorunlu alanları ve resmi kontrol eder; sorunları form hatası olarak ekler. */
    public void validate(VehicleType type, VehicleForm f, Errors errors) {
        if (type == VehicleType.CAR || type == VehicleType.JEEP) {
            if (isBlank(f.getSeri())) errors.rejectValue("seri", "required", "Seri zorunlu");
            if (isBlank(f.getFuel())) errors.rejectValue("fuel", "required", "Yakıt türü zorunlu");
        }
        if (type == VehicleType.JEEP && isBlank(f.getTraction())) {
            errors.rejectValue("traction", "required", "Çekiş zorunlu");
        }
        if (type == VehicleType.MOTOR) {
            if (isBlank(f.getMotorType())) errors.rejectValue("motorType", "required", "Motor tipi zorunlu");
            if (isBlank(f.getCooling())) errors.rejectValue("cooling", "required", "Soğutma zorunlu");
        }
        if (f.getImage() != null && !f.getImage().isEmpty() && !IMAGE_TYPES.contains(f.getImage().getContentType())) {
            errors.rejectValue("image", "type", "Yalnızca JPG, PNG, WEBP veya GIF yükleyin");
        }
    }

    @Transactional
    public Vehicle create(VehicleType type, VehicleForm f) throws IOException {
        Vehicle v = switch (type) {
            case CAR -> new Car();
            case JEEP -> new Jeep();
            case MOTOR -> new Motor();
        };
        apply(v, f);
        return vehicles.save(v);
    }

    @Transactional
    public Vehicle update(Long id, VehicleForm f) throws IOException {
        Vehicle v = get(id);
        apply(v, f);
        return vehicles.save(v);
    }

    /** Kirada olan araç silinemez; geçmiş kiralamaları olan araç da silinmez (raporlar bozulmasın). */
    @Transactional
    public void delete(Long id) {
        Vehicle v = get(id);
        if (rentals.existsByVehicleAndStatus(v, Rental.Status.ACTIVE)) {
            throw new IllegalStateException("Bu araç şu an kirada, silinemez.");
        }
        if (rentals.existsByVehicleAndStatus(v, Rental.Status.RETURNED)) {
            throw new IllegalStateException("Bu aracın geçmiş kiralamaları var. Silmek yerine stoğunu 0 yapın.");
        }
        vehicles.delete(v);
    }

    /** Düzenleme formunu mevcut değerlerle doldurur. */
    public VehicleForm toForm(Vehicle v) {
        VehicleForm f = new VehicleForm();
        f.setMake(v.getMake());
        f.setModel(v.getModel());
        f.setYear(v.getYear());
        f.setKm(v.getKm());
        f.setColor(v.getColor());
        f.setEnginePower(v.getEnginePower());
        f.setEngineCapacity(v.getEngineCapacity());
        f.setPrice(v.getPrice());
        f.setStock(v.getStock());
        if (v instanceof Car c) { f.setSeri(c.getSeri()); f.setFuel(c.getFuel()); }
        if (v instanceof Jeep j) { f.setSeri(j.getSeri()); f.setFuel(j.getFuel()); f.setTraction(j.getTraction()); }
        if (v instanceof Motor m) { f.setMotorType(m.getType()); f.setCooling(m.getCooling()); f.setCylinder(m.getCylinder()); }
        return f;
    }

    private void apply(Vehicle v, VehicleForm f) throws IOException {
        v.setMake(f.getMake().trim());
        v.setModel(f.getModel().trim());
        v.setYear(f.getYear());
        v.setKm(f.getKm());
        v.setColor(f.getColor().trim());
        v.setEnginePower(trim(f.getEnginePower()));
        v.setEngineCapacity(trim(f.getEngineCapacity()));
        v.setPrice(f.getPrice());
        v.setStock(f.getStock());
        if (v instanceof Car c) { c.setSeri(trim(f.getSeri())); c.setFuel(trim(f.getFuel())); }
        if (v instanceof Jeep j) { j.setSeri(trim(f.getSeri())); j.setFuel(trim(f.getFuel())); j.setTraction(trim(f.getTraction())); }
        if (v instanceof Motor m) { m.setType(trim(f.getMotorType())); m.setCooling(trim(f.getCooling())); m.setCylinder(trim(f.getCylinder())); }

        if (f.getImage() != null && !f.getImage().isEmpty()) {
            v.setImage(f.getImage().getBytes());
            v.setImageContentType(f.getImage().getContentType());
        } else if (f.isRemoveImage()) {
            v.setImage(null);
            v.setImageContentType(null);
        }
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
    private static String trim(String s) { return s == null ? null : s.trim(); }
}

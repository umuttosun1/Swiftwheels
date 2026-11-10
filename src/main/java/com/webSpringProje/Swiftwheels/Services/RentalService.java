package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.RentForm;
import com.webSpringProje.Swiftwheels.Entity.Rental;
import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Entity.Vehicle;
import com.webSpringProje.Swiftwheels.Repos.RentalRepository;
import com.webSpringProje.Swiftwheels.Repos.UserRepository;
import com.webSpringProje.Swiftwheels.Repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RentalService {

    public static final int MAX_DAYS = 60;
    public static final int MIN_AGE = 18;

    private final RentalRepository rentals;
    private final VehicleRepository vehicles;
    private final UserRepository users;
    private final int insuranceFee;
    private final Clock clock;

    public RentalService(RentalRepository rentals, VehicleRepository vehicles, UserRepository users,
                         @Value("${app.rental.insurance-fee:250}") int insuranceFee, Clock clock) {
        this.rentals = rentals;
        this.vehicles = vehicles;
        this.users = users;
        this.insuranceFee = insuranceFee;
        this.clock = clock;
    }

    public int getInsuranceFee() {
        return insuranceFee;
    }

    /** Günlük fiyat × gün + isteğe bağlı sabit sigorta ücreti. */
    public int price(int dailyPrice, long days, boolean insurance) {
        return Math.toIntExact(dailyPrice * days + (insurance ? insuranceFee : 0));
    }

    /** Tarih, yaş ve stok kurallarını kontrol eder; sorunları form hatası olarak ekler. */
    public void validate(RentForm f, Users user, Vehicle vehicle, Errors errors) {
        LocalDate today = LocalDate.now(clock);
        if (f.getStartDate() != null && f.getStartDate().isBefore(today)) {
            errors.rejectValue("startDate", "past", "Başlangıç bugün ya da sonrası olmalı");
        }
        if (f.getStartDate() != null && f.getEndDate() != null) {
            long days = ChronoUnit.DAYS.between(f.getStartDate(), f.getEndDate());
            if (days < 1) errors.rejectValue("endDate", "order", "İade tarihi başlangıçtan en az 1 gün sonra olmalı");
            else if (days > MAX_DAYS) errors.rejectValue("endDate", "max", "En fazla " + MAX_DAYS + " gün kiralanabilir");
        }
        if (user.getBirthDate() != null && Period.between(user.getBirthDate(), today).getYears() < MIN_AGE) {
            errors.reject("age", "Araç kiralamak için " + MIN_AGE + " yaşından büyük olmalısınız.");
        }
        if (vehicle.getStock() == null || vehicle.getStock() <= 0) {
            errors.reject("stock", "Bu aracın stoğu tükendi.");
        }
    }

    @Transactional
    public Rental rent(String userEmail, Long vehicleId, RentForm f) {
        Users user = users.findByEmailIgnoreCase(userEmail).orElseThrow();
        // Satırı kilitle: aynı anda iki kişi son aracı kiralamaya çalışırsa yalnızca biri başarır.
        Vehicle vehicle = vehicles.findByIdForUpdate(vehicleId)
                .orElseThrow(() -> new NoSuchElementException("Araç bulunamadı: " + vehicleId));
        if (vehicle.getStock() == null || vehicle.getStock() <= 0) {
            throw new IllegalStateException("Bu aracın stoğu tükendi.");
        }
        vehicle.setStock(vehicle.getStock() - 1);

        Rental r = new Rental();
        r.setUser(user);
        r.setVehicle(vehicle);
        r.setStartDate(f.getStartDate());
        r.setEndDate(f.getEndDate());
        r.setAddress(f.getAddress().trim());
        r.setInsurance(f.isInsurance());
        r.setDailyPrice(vehicle.getPrice());
        r.setTotalPrice(price(vehicle.getPrice(), ChronoUnit.DAYS.between(f.getStartDate(), f.getEndDate()), f.isInsurance()));
        return rentals.save(r);
    }

    /** Yalnızca kiralayan kişi ya da admin iade edebilir. */
    @Transactional
    public void returnVehicle(Long rentalId, String actorEmail, boolean actorIsAdmin) {
        Rental r = rentals.findById(rentalId).orElseThrow(() -> new NoSuchElementException("Kiralama bulunamadı"));
        if (!actorIsAdmin && !r.getUser().getEmail().equalsIgnoreCase(actorEmail)) {
            throw new AccessDeniedException("Bu kiralama size ait değil");
        }
        if (!r.isActive()) {
            throw new IllegalStateException("Bu kiralama zaten kapatılmış.");
        }
        Vehicle v = vehicles.findByIdForUpdate(r.getVehicle().getId()).orElseThrow();
        v.setStock(v.getStock() + 1);
        r.setStatus(Rental.Status.RETURNED);
        r.setReturnedAt(LocalDateTime.now(clock));
    }

    @Transactional(readOnly = true)
    public List<Rental> rentalsOf(String email) {
        return users.findByEmailIgnoreCase(email).map(rentals::findByUserOrderByCreatedAtDesc).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<Rental> all() {
        return rentals.findAllByOrderByCreatedAtDesc();
    }
}

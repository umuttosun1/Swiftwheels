package com.webSpringProje.Swiftwheels.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Bir kiralama. Kayıt iade edilince silinmez (durumu RETURNED olur),
 * böylece admin panelindeki ciro ve geçmiş doğru kalır.
 */
@Entity
@Table(name = "rentals")
public class Rental {

    public enum Status { ACTIVE, RETURNED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 300)
    private String address;

    private boolean insurance;

    @Column(nullable = false)
    private int dailyPrice;

    @Column(nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Status status = Status.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime returnedAt;

    public long getDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    /** Aktif ve iade tarihi geçmiş. */
    public boolean isOverdue() {
        return isActive() && endDate.isBefore(LocalDate.now());
    }

    public Long getId() { return id; }
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public boolean isInsurance() { return insurance; }
    public void setInsurance(boolean insurance) { this.insurance = insurance; }
    public int getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(int dailyPrice) { this.dailyPrice = dailyPrice; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }
}

package com.webSpringProje.Swiftwheels.Repos;

import com.webSpringProje.Swiftwheels.Entity.Rental;
import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Entity.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @EntityGraph(attributePaths = "vehicle")
    List<Rental> findByUserOrderByCreatedAtDesc(Users user);

    @EntityGraph(attributePaths = {"vehicle", "user"})
    List<Rental> findAllByOrderByCreatedAtDesc();

    long countByStatus(Rental.Status status);

    boolean existsByVehicleAndStatus(Vehicle vehicle, Rental.Status status);

    boolean existsByUserAndStatus(Users user, Rental.Status status);

    @Query("select coalesce(sum(r.totalPrice), 0) from Rental r where r.status <> com.webSpringProje.Swiftwheels.Entity.Rental$Status.CANCELLED")
    long totalRevenue();
}

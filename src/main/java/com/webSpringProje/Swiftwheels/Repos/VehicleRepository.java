package com.webSpringProje.Swiftwheels.Repos;

import com.webSpringProje.Swiftwheels.Entity.Vehicle;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /** Kiralama sırasında stoğu güvenle düşürmek için satırı kilitler. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Vehicle v where v.id = :id")
    Optional<Vehicle> findByIdForUpdate(@Param("id") Long id);

    @Query("""
            select v from Vehicle v
            where lower(v.make) like lower(concat('%', :q, '%'))
               or lower(v.model) like lower(concat('%', :q, '%'))
               or lower(v.color) like lower(concat('%', :q, '%'))
            order by v.make, v.model""")
    List<Vehicle> search(@Param("q") String q);

    @Query("select coalesce(sum(v.stock), 0) from Vehicle v")
    long totalStock();
}

package com.webSpringProje.Swiftwheels.Repos;

import com.webSpringProje.Swiftwheels.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {}

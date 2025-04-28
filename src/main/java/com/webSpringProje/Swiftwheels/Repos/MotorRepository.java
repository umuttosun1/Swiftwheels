package com.webSpringProje.Swiftwheels.Repos;

import com.webSpringProje.Swiftwheels.Entity.Motor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorRepository extends JpaRepository<Motor,Long> {
}

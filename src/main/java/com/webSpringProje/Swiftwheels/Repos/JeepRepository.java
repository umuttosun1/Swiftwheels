package com.webSpringProje.Swiftwheels.Repos;

import com.webSpringProje.Swiftwheels.Entity.Jeep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeepRepository extends JpaRepository<Jeep,Long> {
}

package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.MotorDTO;
import com.webSpringProje.Swiftwheels.Entity.Motor;
import com.webSpringProje.Swiftwheels.Mapper.MotorMapper;
import com.webSpringProje.Swiftwheels.Repos.MotorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MotorServices {

    @Autowired
    private MotorRepository motorRepository;

    @Autowired
    private MotorMapper motorMapper;

    public void saveMotor(MotorDTO dto) throws IOException {
        Motor motor = motorMapper.toEntity(dto);
        motorRepository.save(motor);
    }

    public List<Motor> getAllMotors() {
        return motorRepository.findAll();
    }
    public Motor getMotorById(Long id) {
        return motorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Motor bulunamadı: ID " + id));
    }

}

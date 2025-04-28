package com.webSpringProje.Swiftwheels.Mapper;

import com.webSpringProje.Swiftwheels.DTO.MotorDTO;
import com.webSpringProje.Swiftwheels.Entity.Motor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MotorMapper {

    public Motor toEntity(MotorDTO dto) throws IOException {
        Motor motor = new Motor();
        motor.setMake(dto.getMake());
        motor.setModel(dto.getModel());
        motor.setYear(dto.getYear());
        motor.setKm(dto.getKm());
        motor.setColor(dto.getColor());
        motor.setEnginePower(dto.getEnginePower());
        motor.setEngineCapacity(dto.getEngineCapacity());
        motor.setPrice(dto.getPrice());
        motor.setStock(dto.getStock());
        motor.setType(dto.getType());
        motor.setCooling(dto.getCooling());
        motor.setCylinder(dto.getCylinder());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            motor.setImage(dto.getImage().getBytes());
        }

        return motor;
    }

    public MotorDTO toDTO(Motor motor) {
        MotorDTO dto = new MotorDTO();
        dto.setMake(motor.getMake());
        dto.setModel(motor.getModel());
        dto.setYear(motor.getYear());
        dto.setKm(motor.getKm());
        dto.setColor(motor.getColor());
        dto.setEnginePower(motor.getEnginePower());
        dto.setEngineCapacity(motor.getEngineCapacity());
        dto.setPrice(motor.getPrice());
        dto.setStock(motor.getStock());
        dto.setType(motor.getType());
        dto.setCooling(motor.getCooling());
        dto.setCylinder(motor.getCylinder());
        // Image MultipartFile olduğu için buraya set edilmez
        return dto;
    }
}

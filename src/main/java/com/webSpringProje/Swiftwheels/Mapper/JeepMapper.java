package com.webSpringProje.Swiftwheels.Mapper;

import com.webSpringProje.Swiftwheels.DTO.JeepDTO;
import com.webSpringProje.Swiftwheels.Entity.Jeep;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JeepMapper {

    public Jeep toEntity(JeepDTO dto) throws IOException {
        Jeep jeep = new Jeep();
        jeep.setMake(dto.getMake());
        jeep.setModel(dto.getModel());
        jeep.setYear(dto.getYear());
        jeep.setKm(dto.getKm());
        jeep.setColor(dto.getColor());
        jeep.setEnginePower(dto.getEnginePower());
        jeep.setEngineCapacity(dto.getEngineCapacity());
        jeep.setPrice(dto.getPrice());
        jeep.setStock(dto.getStock());
        jeep.setSeri(dto.getSeri());
        jeep.setFuel(dto.getFuel());
        jeep.setTraction(dto.getTraction());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            jeep.setImage(dto.getImage().getBytes());
        }

        return jeep;
    }

    public JeepDTO toDTO(Jeep jeep) {
        JeepDTO dto = new JeepDTO();
        dto.setMake(jeep.getMake());
        dto.setModel(jeep.getModel());
        dto.setYear(jeep.getYear());
        dto.setKm(jeep.getKm());
        dto.setColor(jeep.getColor());
        dto.setEnginePower(jeep.getEnginePower());
        dto.setEngineCapacity(jeep.getEngineCapacity());
        dto.setPrice(jeep.getPrice());
        dto.setStock(jeep.getStock());
        dto.setSeri(jeep.getSeri());
        dto.setFuel(jeep.getFuel());
        dto.setTraction(jeep.getTraction());
        return dto;
    }
}

package com.webSpringProje.Swiftwheels.Mapper;

import com.webSpringProje.Swiftwheels.DTO.CarDTO;
import com.webSpringProje.Swiftwheels.Entity.Car;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CarMapper {

    public Car toEntity(CarDTO dto) throws IOException {
        Car car = new Car();
        car.setMake(dto.getMake());
        car.setModel(dto.getModel());
        car.setYear(dto.getYear());
        car.setKm(dto.getKm());
        car.setColor(dto.getColor());
        car.setEnginePower(dto.getEnginePower());
        car.setEngineCapacity(dto.getEngineCapacity());
        car.setPrice(dto.getPrice());
        car.setStock(dto.getStock());
        car.setSeri(dto.getSeri());
        car.setFuel(dto.getFuel());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            car.setImage(dto.getImage().getBytes());
        }

        return car;
    }

    public CarDTO toDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.setMake(car.getMake());
        dto.setModel(car.getModel());
        dto.setYear(car.getYear());
        dto.setKm(car.getKm());
        dto.setColor(car.getColor());
        dto.setEnginePower(car.getEnginePower());
        dto.setEngineCapacity(car.getEngineCapacity());
        dto.setPrice(car.getPrice());
        dto.setStock(car.getStock());
        dto.setSeri(car.getSeri());
        dto.setFuel(car.getFuel());
        // Image DTO'da MultipartFile olduğu için geri set edilmez burada
        return dto;
    }
}

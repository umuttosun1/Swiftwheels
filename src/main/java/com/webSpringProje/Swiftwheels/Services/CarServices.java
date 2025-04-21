package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.CarDTO;
import com.webSpringProje.Swiftwheels.Entity.Car;
import com.webSpringProje.Swiftwheels.Repos.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CarServices {

    @Autowired
    private CarRepository carRepository;

    public void saveCar(CarDTO dto) throws IOException {
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

        carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}


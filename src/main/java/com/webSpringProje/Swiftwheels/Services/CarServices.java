package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.CarDTO;
import com.webSpringProje.Swiftwheels.Entity.Car;
import com.webSpringProje.Swiftwheels.Mapper.CarMapper;
import com.webSpringProje.Swiftwheels.Repos.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CarServices {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;

    public void saveCar(CarDTO dto) throws IOException {
        Car car = carMapper.toEntity(dto);
        carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}

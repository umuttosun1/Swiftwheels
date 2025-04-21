package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.DTO.CarDTO;
import com.webSpringProje.Swiftwheels.Entity.Car;
import com.webSpringProje.Swiftwheels.Services.CarServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class CarViewController {

    @Autowired
    private CarServices carService;

    @GetMapping("/cars/new")
    public String showCarForm(Model model) {
        model.addAttribute("car", new CarDTO());
        return "car_form";
    }

    @PostMapping("/cars")
    public String saveCar(@ModelAttribute CarDTO carDTO) throws IOException {
        carService.saveCar(carDTO);
        return "redirect:/cars";
    }

    @GetMapping("/cars")
    public String listCars(Model model) {
        List<Car> cars = carService.getAllCars();

        // her araç için base64 string oluştur
        List<Map<String, Object>> carData = new ArrayList<>();
        for (Car car : cars) {
            Map<String, Object> data = new HashMap<>();
            data.put("car", car);
            if (car.getImage() != null) {
                data.put("imageBase64", Base64.getEncoder().encodeToString(car.getImage()));
            } else {
                data.put("imageBase64", null);
            }
            carData.add(data);
        }

        model.addAttribute("cars", carData);
        return "car_list";
    }
}

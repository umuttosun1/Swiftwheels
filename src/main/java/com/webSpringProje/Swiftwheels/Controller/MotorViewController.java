package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.DTO.MotorDTO;
import com.webSpringProje.Swiftwheels.Entity.Motor;
import com.webSpringProje.Swiftwheels.Services.MotorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class MotorViewController {

    @Autowired
    private MotorServices motorService;

    @GetMapping("/motors/new")
    public String showMotorForm(Model model) {
        model.addAttribute("motor", new MotorDTO());
        return "motor_form";  // motor_form.html dosyasını gösterecek
    }

    @PostMapping("/motors")
    public String saveMotor(@ModelAttribute MotorDTO motorDTO) throws IOException {
        motorService.saveMotor(motorDTO);
        return "redirect:/motors";
    }

    @GetMapping("/motors")
    public String listMotors(Model model) {
        List<Motor> motors = motorService.getAllMotors();

        List<Map<String, Object>> motorData = new ArrayList<>();
        for (Motor motor : motors) {
            Map<String, Object> data = new HashMap<>();
            data.put("motor", motor);
            if (motor.getImage() != null) {
                data.put("imageBase64", Base64.getEncoder().encodeToString(motor.getImage()));
            } else {
                data.put("imageBase64", null);
            }
            motorData.add(data);
        }

        model.addAttribute("motors", motorData);
        return "motor_list";  // motor_list.html dosyasını gösterecek
    }

    // MotorViewController.java içine ekle

    @GetMapping("/motors/{id}")
    public String motorDetail(@PathVariable Long id, Model model) {
        Motor motor = motorService.getMotorById(id);
        model.addAttribute("motor", motor);

        if (motor.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(motor.getImage());
            model.addAttribute("motorImage", base64Image);
        } else {
            model.addAttribute("motorImage", null);
        }

        return "motor_detail";
    }


}

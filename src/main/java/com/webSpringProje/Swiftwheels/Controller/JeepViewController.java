package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.DTO.JeepDTO;
import com.webSpringProje.Swiftwheels.Entity.Jeep;
import com.webSpringProje.Swiftwheels.Services.JeepServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class JeepViewController {

    @Autowired
    private JeepServices jeepService;

    @GetMapping("/jeeps/new")
    public String showJeepForm(Model model) {
        model.addAttribute("jeep", new JeepDTO());
        return "jeep_form";  // jeep_form.html dosyasını gösterecek
    }

    @PostMapping("/jeeps")
    public String saveJeep(@ModelAttribute JeepDTO jeepDTO) throws IOException {
        jeepService.saveJeep(jeepDTO);
        return "redirect:/jeeps";
    }

    @GetMapping("/jeeps")
    public String listJeeps(Model model) {
        List<Jeep> jeeps = jeepService.getAllJeeps();

        List<Map<String, Object>> jeepData = new ArrayList<>();
        for (Jeep jeep : jeeps) {
            Map<String, Object> data = new HashMap<>();
            data.put("jeep", jeep);
            if (jeep.getImage() != null) {
                data.put("imageBase64", Base64.getEncoder().encodeToString(jeep.getImage()));
            } else {
                data.put("imageBase64", null);
            }
            jeepData.add(data);
        }

        model.addAttribute("jeeps", jeepData);
        return "jeep_list";  // jeep_list.html dosyasını gösterecek
    }
}

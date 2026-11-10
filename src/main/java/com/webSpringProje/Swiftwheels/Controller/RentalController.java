package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.DTO.RentForm;
import com.webSpringProje.Swiftwheels.Entity.Rental;
import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Entity.Vehicle;
import com.webSpringProje.Swiftwheels.Services.RentalService;
import com.webSpringProje.Swiftwheels.Services.UserService;
import com.webSpringProje.Swiftwheels.Services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class RentalController {

    private final RentalService rentals;
    private final VehicleService vehicles;
    private final UserService users;

    public RentalController(RentalService rentals, VehicleService vehicles, UserService users) {
        this.rentals = rentals;
        this.vehicles = vehicles;
        this.users = users;
    }

    @GetMapping("/rent/{vehicleId}")
    public String form(@PathVariable Long vehicleId, Model model) {
        RentForm f = new RentForm();
        f.setStartDate(LocalDate.now());
        f.setEndDate(LocalDate.now().plusDays(3));
        return show(vehicles.get(vehicleId), f, model);
    }

    @PostMapping("/rent/{vehicleId}")
    public String rent(@PathVariable Long vehicleId, @Valid @ModelAttribute("rentForm") RentForm form,
                       BindingResult errors, Authentication auth, Model model, RedirectAttributes ra) {
        Vehicle vehicle = vehicles.get(vehicleId);
        Users user = users.findByEmail(auth.getName()).orElseThrow();
        rentals.validate(form, user, vehicle, errors);
        if (errors.hasErrors()) {
            return show(vehicle, form, model);
        }
        try {
            Rental r = rentals.rent(auth.getName(), vehicleId, form);
            ra.addFlashAttribute("success", vehicle.getTitle() + " kiralandı · toplam " + r.getTotalPrice() + " ₺");
            return "redirect:/profile";
        } catch (IllegalStateException e) { // aynı anda başka biri son aracı kiraladıysa
            errors.reject("stock", e.getMessage());
            return show(vehicles.get(vehicleId), form, model);
        }
    }

    @PostMapping("/rentals/{id}/return")
    public String returnVehicle(@PathVariable Long id, Authentication auth, RedirectAttributes ra,
                                @RequestHeader(value = "Referer", required = false) String referer) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        try {
            rentals.returnVehicle(id, auth.getName(), admin);
            ra.addFlashAttribute("success", "Araç iade edildi.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + (referer != null && referer.contains("/admin") ? "/admin/dashboard#rentals" : "/profile");
    }

    private String show(Vehicle vehicle, RentForm form, Model model) {
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("rentForm", form);
        model.addAttribute("insuranceFee", rentals.getInsuranceFee());
        model.addAttribute("maxDays", RentalService.MAX_DAYS);
        return "rent_form";
    }
}

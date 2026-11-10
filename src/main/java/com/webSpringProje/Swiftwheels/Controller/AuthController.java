package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.DTO.UsersDto;
import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Entity.VehicleType;
import com.webSpringProje.Swiftwheels.Services.RentalService;
import com.webSpringProje.Swiftwheels.Services.UserService;
import com.webSpringProje.Swiftwheels.Services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

@Controller
public class AuthController {

    private final UserService userService;
    private final VehicleService vehicles;
    private final RentalService rentals;

    public AuthController(UserService userService, VehicleService vehicles, RentalService rentals) {
        this.userService = userService;
        this.vehicles = vehicles;
        this.rentals = rentals;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/homepage";
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        return auth != null && auth.isAuthenticated() ? "redirect:/homepage" : "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UsersDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UsersDto dto, BindingResult errors) {
        if (userService.emailTaken(dto.getEmail())) {
            errors.rejectValue("email", "taken", "Bu e-posta ile zaten bir hesap var");
        }
        if (errors.hasErrors()) {
            return "register";
        }
        userService.register(dto);
        return "redirect:/login?registerSuccess";
    }

    /** Vitrin: herkese açık. */
    @GetMapping("/homepage")
    public String homepage(Model model) {
        model.addAttribute("counts", vehicles.countsByType());
        // fotoğraflı araçlar önde, sonra en yeniler
        var featured = vehicles.all().stream()
                .filter(v -> v.getStock() != null && v.getStock() > 0)
                .sorted(Comparator.comparing((com.webSpringProje.Swiftwheels.Entity.Vehicle v) -> !v.hasImage())
                        .thenComparing(v -> -v.getId()))
                .limit(6)
                .toList();
        model.addAttribute("featured", featured);
        model.addAttribute("hero", featured.stream().filter(v -> v.hasImage()).findFirst().orElse(null));
        return "homepage";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("q", q);
        model.addAttribute("results", vehicles.search(q));
        return "search";
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        Users me = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("me", me);
        model.addAttribute("rentals", rentals.rentalsOf(auth.getName()));
        return "profile";
    }

    /** Eski bağlantılar için (ör. kayıtlı yer imleri). */
    @GetMapping("/vehicles")
    public String vehiclesIndex() {
        return "redirect:/" + VehicleType.CAR.getPath();
    }
}

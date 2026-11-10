package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.Entity.Rental;
import com.webSpringProje.Swiftwheels.Repos.RentalRepository;
import com.webSpringProje.Swiftwheels.Repos.VehicleRepository;
import com.webSpringProje.Swiftwheels.Services.RentalService;
import com.webSpringProje.Swiftwheels.Services.UserService;
import com.webSpringProje.Swiftwheels.Services.VehicleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final VehicleService vehicles;
    private final RentalService rentals;
    private final UserService users;
    private final RentalRepository rentalRepo;
    private final VehicleRepository vehicleRepo;

    public AdminController(VehicleService vehicles, RentalService rentals, UserService users,
                           RentalRepository rentalRepo, VehicleRepository vehicleRepo) {
        this.vehicles = vehicles;
        this.rentals = rentals;
        this.users = users;
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        List<Rental> all = rentals.all();
        model.addAttribute("vehicleCount", vehicleRepo.count());
        model.addAttribute("totalStock", vehicleRepo.totalStock());
        model.addAttribute("activeRentals", rentalRepo.countByStatus(Rental.Status.ACTIVE));
        model.addAttribute("revenue", rentalRepo.totalRevenue());
        model.addAttribute("overdue", all.stream().filter(Rental::isOverdue).count());
        model.addAttribute("rentals", all);
        model.addAttribute("vehicles", vehicles.all().stream()
                .sorted(Comparator.comparing((com.webSpringProje.Swiftwheels.Entity.Vehicle v) -> v.getVehicleType())
                        .thenComparing(com.webSpringProje.Swiftwheels.Entity.Vehicle::getTitle))
                .toList());
        model.addAttribute("users", users.all());
        return "admin-dashboard";
    }

    @PostMapping("/users/{id}/admin")
    public String setAdmin(@PathVariable Long id, @RequestParam boolean admin, Authentication auth,
                           RedirectAttributes ra) {
        try {
            users.setAdmin(id, admin, auth.getName());
            ra.addFlashAttribute("success", admin ? "Admin yetkisi verildi." : "Admin yetkisi kaldırıldı.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard#users";
    }
}

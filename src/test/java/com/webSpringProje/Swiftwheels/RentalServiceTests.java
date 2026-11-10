package com.webSpringProje.Swiftwheels;

import com.webSpringProje.Swiftwheels.DTO.RentForm;
import com.webSpringProje.Swiftwheels.DTO.UsersDto;
import com.webSpringProje.Swiftwheels.Entity.Rental;
import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Entity.Vehicle;
import com.webSpringProje.Swiftwheels.Repos.RentalRepository;
import com.webSpringProje.Swiftwheels.Repos.VehicleRepository;
import com.webSpringProje.Swiftwheels.Services.RentalService;
import com.webSpringProje.Swiftwheels.Services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class RentalServiceTests {

    @Autowired RentalService rentals;
    @Autowired UserService users;
    @Autowired VehicleRepository vehicles;
    @Autowired RentalRepository rentalRepo;

    private Users newUser(LocalDate birthDate) {
        UsersDto d = new UsersDto();
        d.setFirstName("T");
        d.setLastName("U");
        d.setPassword("gizli-sifre");
        d.setEmail(UUID.randomUUID() + "@test.local");
        d.setBirthDate(birthDate);
        d.setPhoneNumber("05001112233");
        return users.register(d);
    }

    private Vehicle vehicleWithStock(int stock) {
        Vehicle v = vehicles.findAll().stream().filter(x -> x.getStock() > 0).findFirst().orElseThrow();
        v.setStock(stock);
        return vehicles.save(v);
    }

    private static RentForm form(int startOffset, int days, boolean insurance) {
        RentForm f = new RentForm();
        f.setStartDate(LocalDate.now().plusDays(startOffset));
        f.setEndDate(LocalDate.now().plusDays(startOffset + days));
        f.setAddress("Ankara");
        f.setInsurance(insurance);
        f.setAcceptTerms(true);
        return f;
    }

    @Test
    void demoDataAndAdminAreSeeded() {
        assertThat(vehicles.count()).isEqualTo(10);
        assertThat(users.findByEmail("admin@test.local")).get().matches(Users::isAdmin);
    }

    @Test
    void priceIsDailyTimesDaysPlusInsurance() {
        assertThat(rentals.price(1000, 3, false)).isEqualTo(3000);
        assertThat(rentals.price(1000, 3, true)).isEqualTo(3000 + rentals.getInsuranceFee());
    }

    @Test
    void rentAndReturnKeepStockAndRevenueConsistent() {
        Users u = newUser(LocalDate.of(1990, 1, 1));
        Vehicle v = vehicleWithStock(1);
        long revenueBefore = rentalRepo.totalRevenue();

        Rental r = rentals.rent(u.getEmail(), v.getId(), form(0, 2, true));
        assertThat(r.getTotalPrice()).isEqualTo(v.getPrice() * 2 + rentals.getInsuranceFee());
        assertThat(vehicles.findById(v.getId()).orElseThrow().getStock()).isZero();

        assertThatThrownBy(() -> rentals.rent(u.getEmail(), v.getId(), form(0, 1, false)))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("stoğu");

        rentals.returnVehicle(r.getId(), u.getEmail(), false);
        assertThat(vehicles.findById(v.getId()).orElseThrow().getStock()).isEqualTo(1);
        // iade ciroyu düşürmez
        assertThat(rentalRepo.totalRevenue()).isEqualTo(revenueBefore + r.getTotalPrice());

        assertThatThrownBy(() -> rentals.returnVehicle(r.getId(), u.getEmail(), false))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void onlyOwnerOrAdminCanReturn() {
        Users owner = newUser(LocalDate.of(1990, 1, 1));
        Users other = newUser(LocalDate.of(1990, 1, 1));
        Rental r = rentals.rent(owner.getEmail(), vehicleWithStock(3).getId(), form(0, 1, false));

        assertThatThrownBy(() -> rentals.returnVehicle(r.getId(), other.getEmail(), false))
                .isInstanceOf(AccessDeniedException.class);
        rentals.returnVehicle(r.getId(), "admin@test.local", true);
        assertThat(rentalRepo.findById(r.getId()).orElseThrow().getStatus()).isEqualTo(Rental.Status.RETURNED);
    }

    @Test
    void validationRules() {
        Users adult = newUser(LocalDate.of(1990, 1, 1));
        Users minor = newUser(LocalDate.now().minusYears(16));
        Vehicle v = vehicleWithStock(2);

        var past = new BeanPropertyBindingResult(form(-1, 2, false), "rentForm");
        rentals.validate((RentForm) past.getTarget(), adult, v, past);
        assertThat(past.hasFieldErrors("startDate")).isTrue();

        var tooLong = new BeanPropertyBindingResult(form(0, RentalService.MAX_DAYS + 1, false), "rentForm");
        rentals.validate((RentForm) tooLong.getTarget(), adult, v, tooLong);
        assertThat(tooLong.hasFieldErrors("endDate")).isTrue();

        var sameDay = new BeanPropertyBindingResult(form(0, 0, false), "rentForm");
        rentals.validate((RentForm) sameDay.getTarget(), adult, v, sameDay);
        assertThat(sameDay.hasFieldErrors("endDate")).isTrue();

        var underage = new BeanPropertyBindingResult(form(0, 2, false), "rentForm");
        rentals.validate((RentForm) underage.getTarget(), minor, v, underage);
        assertThat(underage.hasGlobalErrors()).isTrue();

        var ok = new BeanPropertyBindingResult(form(0, 2, false), "rentForm");
        rentals.validate((RentForm) ok.getTarget(), adult, v, ok);
        assertThat(ok.hasErrors()).isFalse();
    }
}

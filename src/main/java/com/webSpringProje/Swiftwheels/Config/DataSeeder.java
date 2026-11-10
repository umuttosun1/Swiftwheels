package com.webSpringProje.Swiftwheels.Config;

import com.webSpringProje.Swiftwheels.Entity.*;
import com.webSpringProje.Swiftwheels.Repos.UserRepository;
import com.webSpringProje.Swiftwheels.Repos.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * İlk açılışta:
 *  - app.admin.email hesabı yoksa admin olarak oluşturur (eskiden admin olmanın hiçbir yolu yoktu),
 *  - app.seed-demo-data=true ise ve hiç araç yoksa örnek araçları ekler.
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository users;
    private final VehicleRepository vehicles;
    private final PasswordEncoder encoder;
    private final String adminEmail;
    private final String adminPassword;
    private final boolean seedDemo;

    public DataSeeder(UserRepository users, VehicleRepository vehicles, PasswordEncoder encoder,
                      @Value("${app.admin.email}") String adminEmail,
                      @Value("${app.admin.password}") String adminPassword,
                      @Value("${app.seed-demo-data:false}") boolean seedDemo) {
        this.users = users;
        this.vehicles = vehicles;
        this.encoder = encoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.seedDemo = seedDemo;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws IOException {
        if (!users.existsByEmailIgnoreCase(adminEmail)) {
            Users admin = new Users();
            admin.setFirstName("Admin");
            admin.setLastName("SwiftWheels");
            admin.setEmail(adminEmail.toLowerCase(java.util.Locale.ROOT));
            admin.setPassword(encoder.encode(adminPassword));
            admin.setBirthDate(LocalDate.of(1990, 1, 1));
            admin.setPhoneNumber("05000000000");
            admin.setRoles(new HashSet<>(Set.of("ROLE_USER", "ROLE_ADMIN")));
            users.save(admin);
            log.info("Admin hesabı oluşturuldu: {} (şifreyi ADMIN_PASSWORD ile belirleyin)", adminEmail);
        }

        if (seedDemo && vehicles.count() == 0) {
            vehicles.saveAll(demoVehicles());
            log.info("Örnek araçlar eklendi.");
        }
    }

    private List<Vehicle> demoVehicles() throws IOException {
        return List.of(
                car("Mercedes-Benz", "C 200", 2022, 18500, "Gümüş", "204 HP", "1.5", 2400, 3, "C-Serisi", "Benzin", "mercedes-c200.png"),
                car("Ford", "Focus", 2021, 42000, "Kırmızı", "125 HP", "1.0", 1350, 4, "Hatchback", "Benzin", "ford-focus.png"),
                car("Fiat", "Egea", 2023, 12000, "Beyaz", "95 HP", "1.4", 950, 6, "Sedan", "Dizel", null),
                car("Renault", "Clio", 2022, 30500, "Mavi", "90 HP", "1.0", 900, 5, "Hatchback", "Benzin", null),
                jeep("Jeep", "Compass", 2022, 25000, "Siyah", "130 HP", "1.3", 2100, 2, "Limited", "Benzin", "4x2"),
                jeep("Toyota", "RAV4", 2023, 9000, "Beyaz", "218 HP", "2.5", 2700, 2, "Hybrid", "Hibrit", "4x4"),
                jeep("Dacia", "Duster", 2021, 51000, "Turuncu", "115 HP", "1.5", 1200, 4, "Prestige", "Dizel", "4x4"),
                motor("Yamaha", "YZF-R1", 2022, 6500, "Siyah", "200 HP", "998 cc", 1800, 2, "Sport", "Sıvı", "4", "yamaha-r1.png"),
                motor("Honda", "CB650R", 2021, 14000, "Gri", "95 HP", "649 cc", 1100, 3, "Naked", "Sıvı", "4", null),
                motor("Vespa", "Primavera 125", 2023, 3000, "Yeşil", "11 HP", "125 cc", 450, 5, "Scooter", "Hava", "1", null));
    }

    private Car car(String make, String model, int year, int km, String color, String power, String cap,
                    int price, int stock, String seri, String fuel, String image) throws IOException {
        Car c = new Car();
        base(c, make, model, year, km, color, power, cap, price, stock, image);
        c.setSeri(seri);
        c.setFuel(fuel);
        return c;
    }

    private Jeep jeep(String make, String model, int year, int km, String color, String power, String cap,
                      int price, int stock, String seri, String fuel, String traction) throws IOException {
        Jeep j = new Jeep();
        base(j, make, model, year, km, color, power, cap, price, stock, null);
        j.setSeri(seri);
        j.setFuel(fuel);
        j.setTraction(traction);
        return j;
    }

    private Motor motor(String make, String model, int year, int km, String color, String power, String cap,
                        int price, int stock, String type, String cooling, String cylinder, String image) throws IOException {
        Motor m = new Motor();
        base(m, make, model, year, km, color, power, cap, price, stock, image);
        m.setType(type);
        m.setCooling(cooling);
        m.setCylinder(cylinder);
        return m;
    }

    private void base(Vehicle v, String make, String model, int year, int km, String color, String power,
                      String cap, int price, int stock, String image) throws IOException {
        v.setMake(make);
        v.setModel(model);
        v.setYear(year);
        v.setKm(km);
        v.setColor(color);
        v.setEnginePower(power);
        v.setEngineCapacity(cap);
        v.setPrice(price);
        v.setStock(stock);
        if (image != null) {
            try (InputStream in = new ClassPathResource("seed/" + image).getInputStream()) {
                v.setImage(in.readAllBytes());
                v.setImageContentType("image/png");
            }
        }
    }
}

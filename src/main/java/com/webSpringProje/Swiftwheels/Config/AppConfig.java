package com.webSpringProje.Swiftwheels.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

/**
 * Güvenlik yapılandırmasından ayrı tutulan bean'ler. PasswordEncoder SecurityConfig
 * içinde olunca SecurityConfig → UserDetailsService → UserService → PasswordEncoder
 * döngüsü oluşuyordu.
 */
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Testlerde tarihi sabitleyebilmek için sistem saati bean olarak verilir. */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

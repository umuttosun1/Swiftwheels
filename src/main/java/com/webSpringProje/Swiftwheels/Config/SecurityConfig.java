package com.webSpringProje.Swiftwheels.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomLoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(CustomLoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // herkese açık: vitrin, araç listeleri/detayları, resimler, giriş/kayıt
                        .requestMatchers("/", "/homepage", "/search", "/register", "/login", "/error",
                                "/css/**", "/js/**", "/img/**", "/vehicles/*/image").permitAll()
                        .requestMatchers(HttpMethod.GET, "/{type:cars|jeeps|motors}", "/{type:cars|jeeps|motors}/{id:\\d+}").permitAll()
                        // araç ekleme/düzenleme/silme ve yönetim yalnızca admin
                        .requestMatchers("/admin/**", "/{type:cars|jeeps|motors}/new",
                                "/{type:cars|jeeps|motors}/*/edit", "/{type:cars|jeeps|motors}/*/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/{type:cars|jeeps|motors}", "/{type:cars|jeeps|motors}/*").hasRole("ADMIN")
                        // kiralama ve profil giriş ister
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // Çıkış artık CSRF korumalı POST (eskiden GET bağlantısıydı)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .build();
    }
}

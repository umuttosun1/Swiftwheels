package com.webSpringProje.Swiftwheels;

import com.webSpringProje.Swiftwheels.Repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationTests {

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;

    private ResultActions register(String email, String password) throws Exception {
        return mvc.perform(post("/register").with(csrf())
                .param("firstName", "Test").param("lastName", "Kisi")
                .param("email", email).param("password", password)
                .param("birthDate", "1995-05-05").param("phoneNumber", "05001112233"));
    }

    @Test
    void registerThenLogin() throws Exception {
        register("Yeni@Test.local", "gizli-sifre").andExpect(redirectedUrl("/login?registerSuccess"));
        assertThat(users.findByEmailIgnoreCase("yeni@test.local")).isPresent()
                .get().satisfies(u -> assertThat(u.getPassword()).startsWith("$2")); // BCrypt
        mvc.perform(formLogin("/login").userParameter("email").user("yeni@test.local").password("gizli-sifre"))
                .andExpect(authenticated());
        mvc.perform(formLogin("/login").userParameter("email").user("yeni@test.local").password("yanlis"))
                .andExpect(unauthenticated());
    }

    @Test
    void duplicateEmailIsRejected() throws Exception {
        register("tekrar@test.local", "gizli-sifre").andExpect(status().is3xxRedirection());
        // farklı büyük/küçük harf de aynı adres sayılır
        register("TEKRAR@test.local", "gizli-sifre")
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("user", "email"));
        assertThat(users.findAll().stream().filter(u -> u.getEmail().equalsIgnoreCase("tekrar@test.local"))).hasSize(1);
    }

    @Test
    void invalidInputIsRejected() throws Exception {
        mvc.perform(post("/register").with(csrf())
                        .param("firstName", "").param("lastName", "K").param("email", "not-an-email")
                        .param("password", "123").param("birthDate", "2999-01-01").param("phoneNumber", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("user", "firstName", "email", "password", "birthDate", "phoneNumber"));
    }
}

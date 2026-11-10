package com.webSpringProje.Swiftwheels;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Eskiden giriş yapan herkes araç ekleyebiliyordu; artık yalnızca admin. */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {

    @Autowired
    MockMvc mvc;

    @Test
    void catalogueIsPublic() throws Exception {
        mvc.perform(get("/homepage")).andExpect(status().isOk());
        mvc.perform(get("/cars")).andExpect(status().isOk());
        mvc.perform(get("/jeeps")).andExpect(status().isOk());
        mvc.perform(get("/motors")).andExpect(status().isOk());
        mvc.perform(get("/search").param("q", "ford")).andExpect(status().isOk());
        mvc.perform(get("/")).andExpect(redirectedUrl("/homepage"));
    }

    @Test
    void anonymousIsSentToLoginForProtectedPages() throws Exception {
        mvc.perform(get("/profile")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/login"));
        mvc.perform(get("/rent/1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/login"));
        mvc.perform(get("/cars/new")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void regularUsersCannotManageVehicles() throws Exception {
        var u = user("someone@test.local").roles("USER");
        mvc.perform(get("/cars/new").with(u)).andExpect(status().isForbidden());
        mvc.perform(get("/motors/new").with(u)).andExpect(status().isForbidden());
        mvc.perform(post("/cars").with(u).with(csrf()).param("make", "x")).andExpect(status().isForbidden());
        mvc.perform(post("/cars/1/delete").with(u).with(csrf())).andExpect(status().isForbidden());
        mvc.perform(get("/admin/dashboard").with(u)).andExpect(status().isForbidden());
    }

    @Test
    void adminCanManageVehicles() throws Exception {
        var admin = user("admin@test.local").roles("USER", "ADMIN");
        mvc.perform(get("/cars/new").with(admin)).andExpect(status().isOk());
        mvc.perform(get("/admin/dashboard").with(admin)).andExpect(status().isOk());
    }

    @Test
    void formsRequireCsrfToken() throws Exception {
        mvc.perform(post("/register").param("email", "a@b.com")).andExpect(status().isForbidden());
    }

    @Test
    void unknownVehicleGives404() throws Exception {
        mvc.perform(get("/cars/999999")).andExpect(status().isNotFound());
    }
}

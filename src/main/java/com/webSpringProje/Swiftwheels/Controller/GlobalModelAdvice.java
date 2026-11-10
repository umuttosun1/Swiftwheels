package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.Entity.Users;
import com.webSpringProje.Swiftwheels.Entity.VehicleType;
import com.webSpringProje.Swiftwheels.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

/** Her sayfanın menüsü için gereken veriler ve ortak hata sayfası. */
@ControllerAdvice
public class GlobalModelAdvice {

    private final UserService users;

    public GlobalModelAdvice(UserService users) {
        this.users = users;
    }

    @ModelAttribute("vehicleTypes")
    public VehicleType[] vehicleTypes() {
        return VehicleType.values();
    }

    @ModelAttribute("currentUser")
    public Users currentUser(Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return users.findByEmail(auth.getName()).orElse(null);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(NoSuchElementException e) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("status", 404);
        mv.addObject("message", e.getMessage() != null ? e.getMessage() : "Aradığınız sayfa bulunamadı.");
        mv.addObject("vehicleTypes", VehicleType.values());
        return mv;
    }
}

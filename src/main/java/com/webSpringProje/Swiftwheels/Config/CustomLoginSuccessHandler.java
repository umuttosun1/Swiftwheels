package com.webSpringProje.Swiftwheels.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Giriş yapmadan korumalı bir sayfaya (ör. "Kirala") gidilmişse girişten sonra oraya döner.
 * Doğrudan giriş yapılırsa admin panele, kullanıcı ana sayfaya gider.
 */
@Component
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        if (requestCache.getRequest(request, response) != null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        getRedirectStrategy().sendRedirect(request, response, isAdmin ? "/admin/dashboard" : "/homepage");
    }
}

package com.trading.demo.Controller;

import java.io.IOException;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.demo.model.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class Auth2controller {
    @GetMapping("/login/google")
    public void redirectToGoogle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Redirect to the Google OAuth2 authorization URI
        response.sendRedirect("/login/oauth2/authorization/google");
    }

    // /login/oauth2/code/google
    @GetMapping("/login/oauth2/code/google")
    public Users handleGoogleCallback(@RequestParam(required = false, name = "code") String code,
            @RequestParam(required = false, name = "state") String state,
            OAuth2AuthenticationToken authentication) {

        // Extract user details from the authentication object or access token
        String email = authentication.getPrincipal().getAttribute("email");
        String fullName = authentication.getPrincipal().getAttribute("name");

        // You can extract more details as needed

        Users user = new Users();
        user.setEmail(email);
        user.setFullName(fullName);
        System.out.println("hi oauth2");

        return user;
    }
}

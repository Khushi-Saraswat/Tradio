package com.trading.demo.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.trading.demo.model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class AppConfig {

	@Value("${frontend.url}")
	private String frontendUrl;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // allow
																														// sessions
																														// for
																														// OAuth2
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/**").authenticated()
						.anyRequest().permitAll())
				.oauth2Login(oauth -> oauth
						.loginPage("/login/google")
						.authorizationEndpoint(authorization -> authorization.baseUri("/login/oauth2/authorization"))
						.successHandler(new AuthenticationSuccessHandler() {
							@Override
							public void onAuthenticationSuccess(HttpServletRequest request,
									HttpServletResponse response,
									Authentication authentication) throws IOException, ServletException {

								String email = null; // declare outside the if block
								String token = null;
								if (authentication.getPrincipal() instanceof DefaultOAuth2User userDetails) {
									email = userDetails.getAttribute("email");
									String fullName = userDetails.getAttribute("name");

									boolean emailVerified = Boolean.TRUE
											.equals(userDetails.getAttribute("email_verified"));

									// List<String> allowedEmails = List.of(
									// "khushisaraswat69@gmail.com",
									// "aartisaraswat344@gmail.com"

									// );

									// if (!allowedEmails.contains(email)) {
									// System.out.println("Email not allowed: " + email);
									// response.sendError(HttpServletResponse.SC_FORBIDDEN, "Email not allowed");
									// return;
									// }

									Users user = new Users();
									user.setVerified(emailVerified);
									user.setFullName(fullName);
									user.setEmail(email);

									System.out.println("Google Authenticated User: " + user);
									List<SimpleGrantedAuthority> authorities = List
											.of(new SimpleGrantedAuthority("ROLE_USER"));
									Authentication auth = new UsernamePasswordAuthenticationToken(email, null,
											authorities);
									token = JwtProvider.generateToken(auth);
									System.out.println(token);
								}

								// Generate JWT token

								System.out.println("Google Authenticated User: ");
								// Redirect to frontend with token as query param
								response.sendRedirect(frontendUrl + "/login/success?token=" + token);

							}

						}))
				.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}

	// CORS Configuration
	private CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration cfg = new CorsConfiguration();
			cfg.setAllowedOrigins(Arrays.asList(
					"http://localhost:3000",
					"http://localhost:5173",
					"http://localhost:5174",
					"http://localhost:4200",
					frontendUrl));
			cfg.setAllowedMethods(Collections.singletonList("*"));
			cfg.setAllowCredentials(true);
			cfg.setAllowedHeaders(Collections.singletonList("*"));
			cfg.setExposedHeaders(Collections.singletonList("Authorization"));
			cfg.setMaxAge(3600L);
			return cfg;
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

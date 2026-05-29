package com.example.demo.Security;

import com.example.demo.Config.AppConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private AuthEntryPointJwt authEntryPointJwt;
    private AuthTokenFilter authTokenFilter;

    public SecurityConfig(AuthEntryPointJwt authEntryPointJwt, AuthTokenFilter authTokenFilter) {
        this.authEntryPointJwt = authEntryPointJwt;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF — not needed for stateless REST APIs
                .csrf(csrf -> csrf.disable())

                // CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Route-level authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints — no token needed
                        .requestMatchers(AppConstant.PUBLIC_URL).permitAll()

                        // Admin only
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")



                        // Provider only
                        .requestMatchers("/api/provider/**").hasAnyRole("PROVIDER", "ADMIN")

                        // Everything else requires a valid JWT
                        .anyRequest().authenticated()
                );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt));

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // CORS — allow all origins in dev; restrict in production
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

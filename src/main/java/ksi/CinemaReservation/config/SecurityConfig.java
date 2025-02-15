package ksi.CinemaReservation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ksi.CinemaReservation.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Enable this if using CSRF tokens
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", 
                    "/films", 
                    "/films/**", 
                    "/auth/login", 
                    "/auth/register", 
                    "/register", 
                    "/css/**", 
                    "/images/**",
                    "/static/about",  // Allow access to the "About Us" page
                    "/static/contact" // Allow access to the "Contact" page
                ).permitAll() // Publicly accessible paths
                .requestMatchers("/films/{id}/reserve", "/users/profile/**").hasAnyRole("USER", "ADMIN") // User-specific routes
                .requestMatchers("/reservations/delete/**").hasAnyRole("USER", "ADMIN") // Allow both roles to delete reservations
                .requestMatchers("/reservations/success").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/films/create", "/films/{id}/edit", "/screenings/**", "/reservations/**").hasRole("ADMIN") // Admin-specific routes
                .anyRequest().authenticated() // All other routes require authentication
            )
            .formLogin(form -> form
                .loginPage("/auth/login") // Custom login page
                .defaultSuccessUrl("/users/profile", true) // Redirect on successful login
                .failureUrl("/auth/login?error") // Redirect on login failure
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

}

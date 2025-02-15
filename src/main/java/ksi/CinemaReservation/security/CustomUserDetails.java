package ksi.CinemaReservation.security;

import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.repositories.AdministratorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final AdministratorRepository administratorRepository;

    public CustomUserDetails(User user, AdministratorRepository administratorRepository) {
        this.user = user;
        this.administratorRepository = administratorRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Check if user is an administrator
        String role = administratorRepository.existsByUserId(user.getId()) ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }
}

package ksi.CinemaReservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.repositories.AdministratorRepository;
import ksi.CinemaReservation.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Determine role based on administrator status
        boolean isAdmin = false;
        try {
            isAdmin = administratorRepository.existsByUserId(user.getId());
        } catch (Exception e) {
            throw new IllegalStateException("Error checking admin status for user ID: " + user.getId(), e);
        }

        String role = isAdmin ? "ADMIN" : "USER";

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // Hashed password
                .roles(role)
                .build();
    }
}

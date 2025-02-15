package ksi.CinemaReservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.models.UserDTO;
import ksi.CinemaReservation.repositories.AdministratorRepository;
import ksi.CinemaReservation.repositories.UserRepository;

@Service
public class UserService {

	
    @Autowired
    private AdministratorRepository administratorRepository;

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserDTO userDTO) throws Exception {
        // Check if the user already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new Exception("Email is already registered.");
        }

        // Create a new user entity
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encrypt password
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        // Save the user
        userRepository.save(user);
        
        
        
    }
    

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    public void updateUser(User user) {
        userRepository.save(user);
    }
    public boolean isAdmin(Integer userId) {
        return administratorRepository.existsByUserId(userId);
    }
}

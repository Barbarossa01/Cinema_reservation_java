package ksi.CinemaReservation.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksi.CinemaReservation.models.Reservation;
import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.services.ReservationService;
import ksi.CinemaReservation.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject the PasswordEncoder

    /**
     * Display the user's profile.
     */
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Get the logged-in user's email
            String email = principal.getName();

            // Fetch the user and their reservations
            User user = userService.findUserByEmail(email);
            List<Reservation> reservations = reservationService.getUserReservations(user.getId());

            // Add user and reservations to the model
            model.addAttribute("user", user);
            model.addAttribute("reservations", reservations);

            return "user/profile"; // Render the user's profile page
        } catch (Exception e) {
            // Log the error (optional)
            System.out.println("Error fetching user profile: " + e.getMessage());

            // Redirect to a generic error page or back to home with an error message
            redirectAttributes.addFlashAttribute("error", "Unable to load user profile.");
            return "redirect:/";
        }
    }

    /**
     * Handle profile update, including password changes.
     */
    @PostMapping("/profile/edit")
    public String updateProfile(
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "password", required = false) String password, // Optional password field
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            // Get the logged-in user's email
            String currentEmail = principal.getName();

            // Fetch the existing user
            User user = userService.findUserByEmail(currentEmail);

            // Update user details
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            // If a new password is provided, encode and update it
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            // Save the updated user
            userService.updateUser(user);

            // Add success message
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");

            return "redirect:/users/profile";
        } catch (Exception e) {
            // Log the error
            System.out.println("Error updating user profile: " + e.getMessage());

            // Add error message and redirect back to the profile page
            redirectAttributes.addFlashAttribute("error", "Failed to update profile.");
            return "redirect:/users/profile";
        }
    }
}

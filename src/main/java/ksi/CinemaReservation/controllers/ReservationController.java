package ksi.CinemaReservation.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksi.CinemaReservation.models.Reservation;
import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.services.ReservationService;
import ksi.CinemaReservation.services.UserService;
@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @PostMapping("/{id}/reserve")
    public String processReservation(
            @PathVariable("id") Integer filmId,
            @RequestParam("screeningId") Integer screeningId,
            @RequestParam("seats") Integer seats,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            // Validate the number of seats
            if (seats <= 0) {
                throw new IllegalArgumentException("The number of seats must be greater than 0.");
            }
            // Ensure screening ID and film ID are not null
            if (screeningId == null || filmId == null) {
                throw new IllegalArgumentException("Invalid screening or film ID.");
            }
            // Fetch the logged-in user
            String email = principal.getName();
            User user = userService.findUserByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            // Create a reservation
            reservationService.createReservation(screeningId, user, seats);
            redirectAttributes.addFlashAttribute("success", "Reservation successful!");
            // Redirect to the success page
            return "redirect:/reservations/success";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        // Redirect back to the film's details page in case of an error
        return "redirect:/films/" + filmId;
    }

    @PostMapping("/delete/{id}")
    public String deleteReservation(
            @PathVariable Integer id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        boolean isAdmin = false; // Initialize the variable outside the try block
        try {
            // Fetch the logged-in user
            String email = principal.getName();
            User user = userService.findUserByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }

            // Check if the user is an admin by verifying in the Administrator table
            isAdmin = userService.isAdmin(user.getId());

            // Fetch the reservation
            Reservation reservation = reservationService.getReservationById(id);
            if (reservation == null) {
                throw new IllegalArgumentException("Reservation not found.");
            }

            // For regular users, validate ownership
            if (!isAdmin && !reservation.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("You can only delete your own reservations.");
            }

            // Cancel the reservation
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("success", "Reservation deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }

        // Redirect to the appropriate page
        return isAdmin ? "redirect:/reservations" : "redirect:/users/profile";
    }

    @GetMapping
    public String listAllReservations(ModelMap model) {
        try {
            // Fetch all reservations
            List<Reservation> reservations = reservationService.getAllReservations();
            model.addAttribute("reservations", reservations);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching reservations: " + e.getMessage());
        }

        // Return the reservations view
        return "reservations/index";
    }

}

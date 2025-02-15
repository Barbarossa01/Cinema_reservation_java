package ksi.CinemaReservation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksi.CinemaReservation.models.UserDTO;
import ksi.CinemaReservation.services.UserService;
@Controller
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/register"; // Points to src/main/resources/templates/auth/register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(userDTO);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please log in.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
}

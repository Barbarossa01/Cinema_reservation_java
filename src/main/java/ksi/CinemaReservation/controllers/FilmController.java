package ksi.CinemaReservation.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksi.CinemaReservation.models.Film;
import ksi.CinemaReservation.models.FilmDTO;
import ksi.CinemaReservation.models.Screening;
import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.services.FilmService;
import ksi.CinemaReservation.services.ReservationService;
import ksi.CinemaReservation.services.ScreeningService;
import ksi.CinemaReservation.services.UserService;

@Controller
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private ScreeningService screeningService;
    
    
    
    @Autowired
    private ReservationService reservationService;

    
    @Autowired
    private UserService userService;
    
    
    @GetMapping("/{id}/reserve")
    public String showReservationForm(@PathVariable Integer id, Model model) {
        // Fetch the film and its screenings
        Film film = filmService.getFilmById(id)
                .orElseThrow(() -> new RuntimeException("Film not found"));

        List<Screening> screenings = screeningService.getScreeningsByFilmId(id);

        // Add film and screenings to the model
        model.addAttribute("film", film);
        model.addAttribute("screenings", screenings);

        return "films/reserve"; // Render the reservation form
    }

    
    
    

    @PostMapping("/{id}/reserve")
    public String processReservation(
            @PathVariable(name = "id") Integer filmId, // Explicit parameter name
            @RequestParam(name = "screeningId") Integer screeningId, // Explicit parameter name
            @RequestParam(name = "seats") Integer seats, // Explicit parameter name
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            // Get the logged-in user
            String email = principal.getName();
            User user = userService.findUserByEmail(email);

            // Create the reservation
            reservationService.createReservation(screeningId, user, seats);

            redirectAttributes.addFlashAttribute("success", "Reservation successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/reservations/success";
        }
    
    
    
    
    
    
    // List all films
    @GetMapping
    public String index(Model model, Principal principal) {
        // Fetch the logged-in user
        if (principal != null) {
            String email = principal.getName();
            User user = userService.findUserByEmail(email);
            boolean isAdmin = userService.isAdmin(user.getId()); // Check admin status
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("isAdmin", false); // Default to non-admin for unauthenticated users
        }

        // List all films
        List<Film> films = filmService.getAllFilms();
        model.addAttribute("films", films);

        // Initialize an empty map for filmScreenings
        Map<Integer, List<Screening>> filmScreenings = new HashMap<>();

        // Populate the map only if films exist
        if (films != null && !films.isEmpty()) {
            for (Film film : films) {
                List<Screening> screenings = screeningService.getScreeningsByFilmId(film.getId());
                filmScreenings.put(film.getId(), screenings != null ? screenings : new ArrayList<>());
            }
        }

        model.addAttribute("filmScreenings", filmScreenings);

        return "films/index"; // Points to templates/films/index.html
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        // Fetch the film by ID
        Film film = filmService.getFilmById(id)
                .orElseThrow(() -> new RuntimeException("Film not found"));

        // Fetch screenings associated with the film
        List<Screening> screenings = screeningService.getScreeningsByFilmId(film.getId());

        // Add attributes to the model
        model.addAttribute("film", film);
        model.addAttribute("screenings", screenings);

        return "films/show"; // Render the film details page
    }

    // Display form to create a new film
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("film", new Film());
        return "films/create"; // Points to templates/films/create.html
    }

    // Store a new film

    @PostMapping
    public String store(
            @ModelAttribute FilmDTO filmDTO,
            @RequestParam("image") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Define the directory for uploaded images
            String uploadDir = "/home/s49597/uploaded_images";
            
            // Ensure the directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Handle file upload
            if (!imageFile.isEmpty()) {
                // Generate a unique filename
                String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

                // Construct the full path for the uploaded file
                Path imagePath = uploadPath.resolve(imageName);

                // Save the file
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                // Set the image filename in the Film entity
                Film film = new Film();
                film.setTitle(filmDTO.getTitle());
                film.setDescription(filmDTO.getDescription());
                film.setCategory(filmDTO.getCategory());
                film.setDuration(filmDTO.getDuration());
                film.setImage(imageName); // Save only the filename, not the full path

                // Save the film to the database
                filmService.createOrUpdateFilm(film);

                redirectAttributes.addFlashAttribute("success", "Film created successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No file uploaded.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error uploading image.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred.");
        }

        return "redirect:/films";
    }



    
    
    // Display form to edit an existing film
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<Film> film = filmService.getFilmById(id);
        if (film.isPresent()) {
            model.addAttribute("film", film.get());
            return "films/edit";
        } else {
            return "redirect:/films";
        }
    }

    
    
    
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable("id") Integer id,
            @ModelAttribute Film film,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Retrieve the existing film
            Film existingFilm = filmService.getFilmById(id)
                    .orElseThrow(() -> new RuntimeException("Film not found"));

            // Update the film details
            existingFilm.setTitle(film.getTitle());
            existingFilm.setDescription(film.getDescription());
            existingFilm.setCategory(film.getCategory());
            existingFilm.setDuration(film.getDuration());

            // Handle file upload if a new image is provided
            if (!imageFile.isEmpty()) {
                // Define the directory for uploaded images
                String uploadDir = "/home/s49597/uploaded_images";

                // Ensure the directory exists
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename
                String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

                // Construct the full path for the uploaded file
                Path imagePath = uploadPath.resolve(imageName);

                // Save the file
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                // Update the film's image field
                existingFilm.setImage(imageName);
            }

            // Save the updated film
            filmService.createOrUpdateFilm(existingFilm);

            redirectAttributes.addFlashAttribute("success", "Film updated successfully.");
            return "redirect:/films";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error uploading image.");
            return "redirect:/films";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred.");
            return "redirect:/films";
        }
    }


    
    
    

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            filmService.deleteFilmById(id);
            redirectAttributes.addFlashAttribute("success", "Film deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while deleting film: " + e.getMessage());
        }
        return "redirect:/films";
    }


}

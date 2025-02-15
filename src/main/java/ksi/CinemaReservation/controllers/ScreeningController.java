package ksi.CinemaReservation.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksi.CinemaReservation.models.Film;
import ksi.CinemaReservation.models.Screening;
import ksi.CinemaReservation.services.FilmService;
import ksi.CinemaReservation.services.ScreeningService;


@Controller
@RequestMapping("/screenings")
public class ScreeningController {
    private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private FilmService filmService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screenings/index";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("screening", screeningService.getScreeningById(id).orElseThrow(() -> new RuntimeException("Screening not found")));
        return "screenings/details";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("screening", new Screening());
        model.addAttribute("films", filmService.getAllFilms()); // Pass list of films to the view
        return "screenings/create";
    }

    @PostMapping
    public String store(@ModelAttribute Screening screening, RedirectAttributes redirectAttributes) {
        try {
            // Save the screening
            screeningService.createOrUpdateScreening(screening);
            redirectAttributes.addFlashAttribute("success", "Screening added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while adding screening: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/screenings";
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Screening screening = screeningService.getScreeningById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found"));
        model.addAttribute("screening", screening);
        model.addAttribute("films", filmService.getAllFilms());
        return "screenings/edit";
    }




    
    
    @PutMapping("/{id}")
    public String update(
        @PathVariable("id") Integer id,
        @ModelAttribute("screening") Screening screening,
        @RequestParam(name = "filmId", required = false) Integer filmId,
        RedirectAttributes redirectAttributes
    ) {
        try {
            System.out.println("Request Payload: id=" + id + ", filmId=" + filmId + ", screening=" + screening);
            screening.setId(id);

            Film film = filmService.getFilmById(filmId)
                                   .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + filmId));
            screening.setFilm(film);

            screeningService.createOrUpdateScreening(screening);

            redirectAttributes.addFlashAttribute("success", "Screening updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while updating screening: " + e.getMessage());
        }
        return "redirect:/screenings";
    }


    
    
    @DeleteMapping("/{id}")
    public String destroy(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        logger.debug("Attempting to delete screening with ID: {}", id);
        try {
            screeningService.deleteScreeningById(id);
            redirectAttributes.addFlashAttribute("success", "Screening deleted successfully.");
            logger.info("Successfully deleted screening with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting screening with ID: {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error while deleting screening: " + e.getMessage());
        }
        return "redirect:/screenings";
    }

}

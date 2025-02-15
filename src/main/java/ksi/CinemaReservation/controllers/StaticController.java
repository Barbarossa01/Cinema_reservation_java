package ksi.CinemaReservation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/static/about")
    public String about() {
        return "static/about";
    }

    @GetMapping("/static/contact")
    public String contact() {
        return "static/contact";
    }
}

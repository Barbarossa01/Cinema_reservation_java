package ksi.CinemaReservation.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ksi.CinemaReservation.models.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
    // Custom query to find films by category
    List<Film> findByCategory(String category);
}

package ksi.CinemaReservation.services;

import ksi.CinemaReservation.models.Film;
import ksi.CinemaReservation.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public Optional<Film> getFilmById(Integer id) {
        return filmRepository.findById(id);
    }

    public List<Film> getFilmsByCategory(String category) {
        return filmRepository.findByCategory(category);
    }

    public Film createOrUpdateFilm(Film film) {
        System.out.println("Persisting film: " + film);
        return filmRepository.save(film);
    }

    public void deleteFilmById(Integer id) {
        filmRepository.deleteById(id);
    }

    public Page<Film> getFilmsWithPagination(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }
}

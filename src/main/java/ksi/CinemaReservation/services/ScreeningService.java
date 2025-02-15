package ksi.CinemaReservation.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ksi.CinemaReservation.models.Screening;
import ksi.CinemaReservation.repositories.ScreeningRepository;


@Service
public class ScreeningService {
    private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScreeningRepository screeningRepository;

    // Fetch all screenings
    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    // Fetch screening by ID
    public Optional<Screening> getScreeningById(Integer id) {
        return screeningRepository.findById(id);
    }

 // Fetch screening by ID with pessimistic lock
    public Optional<Screening> getScreeningByIdWithLock(Integer id) {
        return screeningRepository.findScreeningWithLockById(id);
    }


    // Fetch screenings by Film ID
    public List<Screening> getScreeningsByFilmId(Integer filmId) {
        return screeningRepository.findByFilmId(filmId);
    }

    // Create or update screening
    public Screening createOrUpdateScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    // Delete screening by ID
    public void deleteScreeningById(Integer id) {
        logger.debug("Attempting to delete screening with ID: {}", id);
        try {
            screeningRepository.deleteById(id);
            logger.info("Successfully deleted screening with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error while deleting screening with ID: {}: {}", id, e.getMessage(), e);
            throw e; // Re-throw to propagate the error
        }
    }
}

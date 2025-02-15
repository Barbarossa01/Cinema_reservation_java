package ksi.CinemaReservation.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import ksi.CinemaReservation.models.Screening;


@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    // Find screenings by Film ID
    List<Screening> findByFilmId(Integer filmId);

    // Fetch screening by ID with a pessimistic write lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Screening s WHERE s.id = ?1")
    Optional<Screening> findScreeningWithLockById(Integer id);

    }


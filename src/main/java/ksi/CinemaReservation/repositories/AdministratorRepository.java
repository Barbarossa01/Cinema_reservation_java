package ksi.CinemaReservation.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ksi.CinemaReservation.models.Administrator;


@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    
    // Find an Administrator by the User ID
    Optional<Administrator> findByUserId(Integer userId);

    // Check if an Administrator exists by the User ID
    boolean existsByUserId(Integer userId);
}

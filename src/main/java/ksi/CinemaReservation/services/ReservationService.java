package ksi.CinemaReservation.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ksi.CinemaReservation.models.Reservation;
import ksi.CinemaReservation.models.Screening;
import ksi.CinemaReservation.models.User;
import ksi.CinemaReservation.repositories.ReservationRepository;


@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ScreeningService screeningService;

    // Fetch all reservations made by a specific user
    public List<Reservation> getUserReservations(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }
    @Transactional
    public Reservation createReservation(Integer screeningId, User user, Integer seats) {
        // Log the starting of the reservation creation process
        System.out.println("Starting createReservation: screeningId=" + screeningId + ", user=" + user.getEmail() + ", seats=" + seats);

        // Fetch the screening by ID with pessimistic lock
        Screening screening = screeningService.getScreeningByIdWithLock(screeningId)
                .orElseThrow(() -> {
                    System.out.println("Error: Screening not found for ID: " + screeningId);
                    return new RuntimeException("Screening not found");
                });

        // Log the fetched screening details
        System.out.println("Fetched screening details: " + screening);

        // Check if enough seats are available for reservation
        if (screening.getAvailableSeats() < seats) {
            System.out.println("Error: Not enough seats available. Requested: " + seats + ", Available: " + screening.getAvailableSeats());
            throw new RuntimeException("Not enough available seats.");
        }

        // Deduct the number of reserved seats from available seats
        int updatedAvailableSeats = screening.getAvailableSeats() - seats;
        screening.setAvailableSeats(updatedAvailableSeats);
        screeningService.createOrUpdateScreening(screening);
        System.out.println("Updated screening with new available seats: " + updatedAvailableSeats);

        // Create a new reservation object
        Reservation reservation = new Reservation();
        reservation.setScreening(screening);
        reservation.setUser(user);
        reservation.setSeats(seats);
        System.out.println("Prepared reservation object: " + reservation);

        // Save the reservation to the database
        Reservation savedReservation = reservationRepository.save(reservation);
        System.out.println("Successfully saved reservation to database. Reservation ID: " + savedReservation.getId());

        return savedReservation;
    }
    
    // Cancel an existing reservation
    public void cancelReservation(Integer reservationId) {
        // Fetch reservation by ID
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Update available seats in the screening
        Screening screening = reservation.getScreening();
        screening.setAvailableSeats(screening.getAvailableSeats() + reservation.getSeats());
        screeningService.createOrUpdateScreening(screening);

        // Delete the reservation
        reservationRepository.deleteById(reservationId);
    }
    
    public Reservation getReservationById(Integer id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

}


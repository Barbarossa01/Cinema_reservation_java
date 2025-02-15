package ksi.CinemaReservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ksi.CinemaReservation.repositories")
public class CinemaReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaReservationApplication.class, args);
    }
}

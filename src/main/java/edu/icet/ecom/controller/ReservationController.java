package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ReservationDTO;
import edu.icet.ecom.service.ReservationService;
import edu.icet.ecom.util.Status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/save")
    public ResponseEntity<ReservationDTO> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO savedReservation = reservationService.saveReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        try {
            List<ReservationDTO> reservations = reservationService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Integer id) {
        try {
            ReservationDTO reservation = reservationService.searchReservation(id);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReservation(@PathVariable Integer id,@RequestBody ReservationDTO reservationDTO) {
        try {
            boolean updated = reservationService.updateReservation(id, reservationDTO);
            if (updated) {
                return ResponseEntity.ok("Reservation updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error updating reservation with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update reservation: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        try {
            reservationService.deleteReservationById(id);
            return ResponseEntity.ok("Reservation deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete reservation: " + e.getMessage());
        }
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<String> updateReservationStatus(@PathVariable Integer id, @RequestParam Status status) {
        try {
            boolean updated = reservationService.updateStatus(id, status);
            if (updated) {
                return ResponseEntity.ok("Reservation status updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update reservation status: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByStatus(@PathVariable Status status) {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsByStatus(status);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsByDate(date);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationDTO>> searchReservationsByName(@RequestParam String name) {
        try {
            List<ReservationDTO> reservations = reservationService.searchReservationsByName(name);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


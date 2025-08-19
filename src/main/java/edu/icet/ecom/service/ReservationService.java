package edu.icet.ecom.service;

import edu.icet.ecom.dto.ReservationDTO;
import edu.icet.ecom.util.Status;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationDTO saveReservation(ReservationDTO reservationDTO);
    List<ReservationDTO> getAllReservations();
    ReservationDTO searchReservation(Integer id);
    boolean updateReservation(Integer id, ReservationDTO reservationDTO);
    void deleteReservationById(Integer id);
    boolean updateStatus(Integer id, Status newStatus);
    List<ReservationDTO> getReservationsByStatus(Status status);
    List<ReservationDTO> getReservationsByDate(LocalDate date);
    List<ReservationDTO> searchReservationsByName(String name);
}

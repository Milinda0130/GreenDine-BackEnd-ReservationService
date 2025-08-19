package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ReservationDTO;
import edu.icet.ecom.entity.ReservationEntity;
import edu.icet.ecom.repository.ReservationRepository;
import edu.icet.ecom.service.ReservationService;
import edu.icet.ecom.util.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReservationDTO saveReservation(ReservationDTO reservationDTO) {
        try {
            // Map DTO to Entity (Fixed the mapping issue)
            ReservationEntity reservationEntity = modelMapper.map(reservationDTO, ReservationEntity.class);

            // Set default status if not provided
            if (reservationEntity.getStatus() == null) {
                reservationEntity.setStatus(Status.PENDING);
            }

            ReservationEntity savedEntity = reservationRepository.save(reservationEntity);
            log.info("Reservation saved successfully with ID: {}", savedEntity.getId());

            return modelMapper.map(savedEntity, ReservationDTO.class);
        } catch (Exception e) {
            log.error("Error saving reservation: {}", e.getMessage());
            throw new RuntimeException("Failed to save reservation: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        try {
            List<ReservationEntity> reservations = reservationRepository.findAll();
            return reservations.stream()
                    .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching all reservations: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch reservations: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDTO searchReservation(Integer id) {
        try {
            Optional<ReservationEntity> reservationOpt = reservationRepository.findById(id);

            if (reservationOpt.isEmpty()) {
                log.warn("Reservation not found with ID: {}", id);
                return null;
            }

            return modelMapper.map(reservationOpt.get(), ReservationDTO.class);
        } catch (Exception e) {
            log.error("Error searching reservation with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to search reservation: " + e.getMessage());
        }
    }

    @Override
    public boolean updateReservation(Integer id, ReservationDTO reservationDTO) {
        try {
            Optional<ReservationEntity> existingReservationOpt = reservationRepository.findById(id);

            if (existingReservationOpt.isEmpty()) {
                log.warn("Reservation not found for update with ID: {}", id);
                return false;
            }

            ReservationEntity existingReservation = existingReservationOpt.get();

            // Update fields
            if (reservationDTO.getName() != null) existingReservation.setName(reservationDTO.getName());
            if (reservationDTO.getPhone() != null) existingReservation.setPhone(reservationDTO.getPhone());
            if (reservationDTO.getDate() != null) existingReservation.setDate(reservationDTO.getDate());
            if (reservationDTO.getNumberOfGuests() != null) existingReservation.setNumberOfGuests(reservationDTO.getNumberOfGuests());
            if (reservationDTO.getTime() != null) existingReservation.setTime(reservationDTO.getTime());
            if (reservationDTO.getTableNumber() != null) existingReservation.setTableNumber(reservationDTO.getTableNumber());
            if (reservationDTO.getStatus() != null) existingReservation.setStatus(reservationDTO.getStatus());
            if (reservationDTO.getNotes() != null) existingReservation.setNotes(reservationDTO.getNotes());

            reservationRepository.save(existingReservation);
            log.info("Reservation updated successfully with ID: {}", id);

            return true;
        } catch (Exception e) {
            log.error("Error updating reservation with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update reservation: " + e.getMessage());
        }
    }

    @Override
    public void deleteReservationById(Integer id) {
        try {
            if (!reservationRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent reservation with ID: {}", id);
                throw new RuntimeException("Reservation not found with id: " + id);
            }

            reservationRepository.deleteById(id);
            log.info("Reservation deleted successfully with ID: {}", id);

        } catch (Exception e) {
            log.error("Error deleting reservation with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete reservation: " + e.getMessage());
        }
    }

    @Override
    public boolean updateStatus(Integer id, Status newStatus) {
        try {
            Optional<ReservationEntity> reservationOpt = reservationRepository.findById(id);

            if (reservationOpt.isEmpty()) {
                log.warn("Reservation not found for status update with ID: {}", id);
                return false;
            }

            ReservationEntity reservation = reservationOpt.get();
            reservation.setStatus(newStatus);
            reservationRepository.save(reservation);

            log.info("Reservation status updated to {} for ID: {}", newStatus, id);
            return true;

        } catch (Exception e) {
            log.error("Error updating status for reservation with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update reservation status: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByStatus(Status status) {
        try {
            List<ReservationEntity> reservations = reservationRepository.findByStatus(status);
            return reservations.stream()
                    .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching reservations by status {}: {}", status, e.getMessage());
            throw new RuntimeException("Failed to fetch reservations by status: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByDate(LocalDate date) {
        try {
            List<ReservationEntity> reservations = reservationRepository.findByDate(date);
            return reservations.stream()
                    .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching reservations by date {}: {}", date, e.getMessage());
            throw new RuntimeException("Failed to fetch reservations by date: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> searchReservationsByName(String name) {
        try {
            List<ReservationEntity> reservations = reservationRepository.findByNameContainingIgnoreCase(name);
            return reservations.stream()
                    .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error searching reservations by name {}: {}", name, e.getMessage());
            throw new RuntimeException("Failed to search reservations by name: " + e.getMessage());
        }
    }
}
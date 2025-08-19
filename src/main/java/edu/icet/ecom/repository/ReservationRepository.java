package edu.icet.ecom.repository;

import edu.icet.ecom.entity.ReservationEntity;
import edu.icet.ecom.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

    List<ReservationEntity> findByStatus(Status status);

    List<ReservationEntity> findByDate(LocalDate date);

    @Query("SELECT r FROM ReservationEntity r WHERE r.date = :date AND r.tableNumber = :tableNumber")
    List<ReservationEntity> findByDateAndTableNumber(@Param("date") LocalDate date, @Param("tableNumber") Integer tableNumber);

    List<ReservationEntity> findByNameContainingIgnoreCase(String name);
}
package edu.icet.ecom.dto;

import edu.icet.ecom.util.Status;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationDTO {
    private Integer id;
    private String name;
    private String phone;
    private LocalDate date;
    private Integer numberOfGuests;
    private String time;
    private Integer tableNumber;
    private Status status;
    private String notes;
}
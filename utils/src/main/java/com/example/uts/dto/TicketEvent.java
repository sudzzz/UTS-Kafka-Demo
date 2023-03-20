package com.example.uts.dto;

import com.example.uts.enums.BookingStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TicketEvent {
    private Integer id;
    private String ticketNumber;
    private String sourceStation;
    private String destinationStation;
    private Integer numberOfPassengers;
    private Double amount;
    private Date journeyDate;
    private Date lastModified;
    private BookingStatus bookingStatus;
}

package com.example.uts.request;

import com.example.uts.enums.BookingStatus;
import com.example.uts.model.BookingJournal;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookTicketRequest {
    @NotBlank
    private String sourceStation;
    @NotBlank
    private String destinationStation;
    @NotBlank
    private Integer numberOfPassengers;
    @NotBlank
    private Double amount;
    private Date journeyDate;
    @NotBlank
    private BookingStatus bookingStatus;

    public BookingJournal to(){
        return BookingJournal.builder()
                .ticketNumber(UUID.randomUUID().toString())
                .sourceStation(sourceStation)
                .destinationStation(destinationStation)
                .numberOfPassengers(numberOfPassengers)
                .amount(amount)
                .bookingStatus(bookingStatus)
                .build();
    }
}

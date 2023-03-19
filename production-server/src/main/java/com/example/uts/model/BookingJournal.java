package com.example.uts.model;

import com.example.uts.enums.BookingStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "booking_journal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookingJournal {
    private Integer id;
    private String ticketNumber;
    private String sourceStation;
    private String destinationStation;
    private Integer numberOfPassengers;
    private Double amount;
    private Date journeyDate;
    private BookingStatus bookingStatus;
}

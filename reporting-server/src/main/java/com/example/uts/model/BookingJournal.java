package com.example.uts.model;

import com.example.uts.enums.BookingStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BookingJournal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,nullable = false)
    private String ticketNumber;
    private String sourceStation;
    private String destinationStation;
    private Integer numberOfPassengers;
    private Double amount;
    @CreationTimestamp
    private Date journeyDate;
    @UpdateTimestamp
    private Date lastModified;
    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;
}

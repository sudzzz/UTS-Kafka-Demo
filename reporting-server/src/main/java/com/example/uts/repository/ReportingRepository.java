package com.example.uts.repository;

import com.example.uts.enums.BookingStatus;
import com.example.uts.model.BookingJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface ReportingRepository extends JpaRepository<BookingJournal,Integer> {
    BookingJournal findByTicketNumber(String ticketNumber);

    @Modifying
    @Query("update BookingJournal b set b.sourceStation = ?2,b.destinationStation = ?3,b.numberOfPassengers = ?4,b.amount = ?5,b.bookingStatus = ?6 where b.ticketNumber = ?1")
    @Transactional
    int updateTicketDetails(String ticketNumber, String sourceStation, String destinationStation, Integer numberOfPassengers, Double amount, BookingStatus bookingStatus);
}

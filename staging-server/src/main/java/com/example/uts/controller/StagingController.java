package com.example.uts.controller;

import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import com.example.uts.service.StagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class StagingController {

    @Autowired
    private StagingService stagingService;

    @GetMapping("/booking/{id}")
    public ResponseEntity<BookingJournal> getTicketDetails(@PathVariable("id") Integer id) throws TicketNotFoundException {
        BookingJournal bookingJournal = stagingService.getTicketDetails(id);
        if(Objects.isNull(bookingJournal)){
            throw new TicketNotFoundException("Ticket with given id is not present in the database");
        }
        return new ResponseEntity<>(bookingJournal, HttpStatus.ACCEPTED);
    }

    @PutMapping("/booking/{ticketNumber}")
    public ResponseEntity<BookingJournal> updateTicketDetails(@PathVariable("ticketNumber") String ticketNumber, @RequestBody BookingJournal bookingJournal) throws TicketNotFoundException {
        return new ResponseEntity<>(stagingService.updateTicketDetails(ticketNumber,bookingJournal),HttpStatus.ACCEPTED);
    }
}

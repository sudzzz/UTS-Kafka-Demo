package com.example.uts.controller;

import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import com.example.uts.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
public class ProductionController {
    @Autowired
    private ProductionService productionService;

    @GetMapping("/booking/{id}")
    public ResponseEntity<BookingJournal> getTicketDetails(@PathVariable("id") Integer id) throws TicketNotFoundException {
        BookingJournal bookingJournal = productionService.getTicketDetails(id);
        if(Objects.isNull(bookingJournal)){
            throw new TicketNotFoundException("Ticket with given id is not present in the database");
        }
        return new ResponseEntity<>(bookingJournal, HttpStatus.ACCEPTED);
    }

    @PutMapping("/booking/{ticketNumber}")
    public ResponseEntity<BookingJournal> saveTicketDetails(@PathVariable("ticketNumber") String ticketNumber, @RequestBody BookingJournal bookingJournal) throws TicketNotFoundException {
        return new ResponseEntity<>(productionService.updateTicketDetails(ticketNumber,bookingJournal),HttpStatus.CREATED);
    }


}

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

    @PostMapping("/booking")
    public ResponseEntity<BookingJournal> saveTicketDetails(@RequestBody BookingJournal bookingJournal){
        return new ResponseEntity<>(productionService.saveTicketDetails(bookingJournal),HttpStatus.CREATED);
    }
}

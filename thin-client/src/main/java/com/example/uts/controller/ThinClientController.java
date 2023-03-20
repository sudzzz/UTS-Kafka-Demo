package com.example.uts.controller;

import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import com.example.uts.request.BookTicketRequest;
import com.example.uts.service.ThinClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class ThinClientController {

    @Autowired
    private ThinClientService thinClientService;

    @GetMapping("/ticketDetails")
    public ResponseEntity<BookingJournal> getTicketDetails(@RequestParam("id") int id) throws TicketNotFoundException {
        BookingJournal bookingJournal = thinClientService.getTicketDetails(id);
        if(Objects.isNull(bookingJournal)){
            throw new TicketNotFoundException("Ticket with given id is not present in the database");
        }
        return new ResponseEntity<>(bookingJournal, HttpStatus.ACCEPTED);
    }

    @PostMapping("/book-ticket")
    public ResponseEntity<BookingJournal> bookTicket(@RequestBody BookTicketRequest bookTicketRequest) throws JsonProcessingException {
       return new ResponseEntity<>(thinClientService.saveTicketDetails(bookTicketRequest),HttpStatus.CREATED);
    }
}

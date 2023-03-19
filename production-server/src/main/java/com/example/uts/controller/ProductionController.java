package com.example.uts.controller;

import com.example.uts.model.BookingJournal;
import com.example.uts.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProductionController {
    @Autowired
    private ProductionService productionService;

    @GetMapping("/booking/{id}")
    public ResponseEntity<Optional<BookingJournal>> getTicketDetails(@PathVariable("id") Integer id){
        return new ResponseEntity<>(productionService.getTicketDetails(id), HttpStatus.ACCEPTED);
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingJournal> saveTicketDetails(@RequestBody BookingJournal bookingJournal){
        return new ResponseEntity<>(productionService.saveTicketDetails(bookingJournal),HttpStatus.CREATED);
    }
}

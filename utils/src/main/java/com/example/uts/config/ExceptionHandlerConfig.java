package com.example.uts.config;

import com.example.uts.exception.TicketNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity handleTicketNotFound(TicketNotFoundException ex){
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

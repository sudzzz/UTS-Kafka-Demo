package com.example.uts.service;

import com.example.uts.model.BookingJournal;

import java.util.Optional;

public interface ProductionService {
    BookingJournal getTicketDetails(Integer id);
    BookingJournal saveTicketDetails(BookingJournal bookingJournal);
}

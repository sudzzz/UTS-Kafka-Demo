package com.example.uts.service;

import com.example.uts.model.BookingJournal;
import com.example.uts.request.BookTicketRequest;

public interface ThinClientService {
    BookingJournal getTicketDetails(int id);
    BookingJournal saveTicketDetails(BookTicketRequest bookTicketRequest);
}

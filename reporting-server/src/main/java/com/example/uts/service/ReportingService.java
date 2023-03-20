package com.example.uts.service;

import com.example.uts.constants.CommonConstants;
import com.example.uts.dto.TicketEvent;
import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import org.springframework.kafka.annotation.KafkaListener;

public interface ReportingService {
    BookingJournal getTicketDetails(int id);
    BookingJournal updateTicketDetails(String ticketNumber, BookingJournal bookingJournal) throws TicketNotFoundException;
    @KafkaListener(topics = CommonConstants.BOOKING_PRODUCTION_SERVER,groupId = "reporting")
    void saveTicketDetails(TicketEvent ticketEvent) throws TicketNotFoundException;
}

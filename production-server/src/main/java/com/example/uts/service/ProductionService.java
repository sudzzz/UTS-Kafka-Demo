package com.example.uts.service;

import com.example.uts.constants.CommonConstants;
import com.example.uts.dto.TicketEvent;
import com.example.uts.model.BookingJournal;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

public interface ProductionService {
    BookingJournal getTicketDetails(Integer id);
    BookingJournal saveTicketDetails(BookingJournal bookingJournal);
    @KafkaListener(topics = CommonConstants.BOOKING_THIN_CLIENT,groupId = "production")
    void saveTicketDetails(TicketEvent ticketEvent);
}

package com.example.uts.service.impl;

import com.example.uts.dto.TicketEvent;
import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.StagingRepository;
import com.example.uts.service.StagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class StagingServiceImpl implements StagingService {

    private static final Logger logger = LoggerFactory.getLogger(StagingServiceImpl.class);

    @Autowired
    private StagingRepository stagingRepository;
    @Override
    public BookingJournal getTicketDetails(int id) {
        return stagingRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal updateTicketDetails(String ticketNumber, BookingJournal bookingJournal) throws TicketNotFoundException {
        BookingJournal ticketDetails = stagingRepository.findByTicketNumber(ticketNumber);
        if(Objects.isNull(ticketDetails)){
            throw new TicketNotFoundException("Ticket with given id is not present in the database");
        }
        stagingRepository.updateTicketDetails(ticketNumber,ticketDetails.getSourceStation(),ticketDetails.getDestinationStation(),ticketDetails.getNumberOfPassengers(),ticketDetails.getAmount(),ticketDetails.getBookingStatus());
        BookingJournal updatedTicket = stagingRepository.findByTicketNumber(ticketNumber);
        return updatedTicket;
    }

    @Override
    public void saveTicketDetails(TicketEvent ticketEvent) {
        logger.info("Ticket Event received in staging-server - {}",ticketEvent.toString());
        BookingJournal ticketDetails = stagingRepository.findByTicketNumber(ticketEvent.getTicketNumber());
        if (Objects.isNull(ticketDetails)) {
            BookingJournal bookingJournal = getDataFromTicketEvent(ticketEvent);
            stagingRepository.save(bookingJournal);
        } else {
            stagingRepository.updateTicketDetails(ticketEvent.getTicketNumber(), ticketEvent.getSourceStation(), ticketEvent.getDestinationStation(), ticketEvent.getNumberOfPassengers(), ticketEvent.getAmount(), ticketEvent.getBookingStatus());
        }
    }
    private BookingJournal getDataFromTicketEvent(TicketEvent ticketEvent){
        return BookingJournal.builder()
                .id(ticketEvent.getId())
                .ticketNumber(ticketEvent.getTicketNumber())
                .sourceStation(ticketEvent.getSourceStation())
                .destinationStation(ticketEvent.getDestinationStation())
                .numberOfPassengers(ticketEvent.getNumberOfPassengers())
                .amount(ticketEvent.getAmount())
                .journeyDate(ticketEvent.getJourneyDate())
                .lastModified(ticketEvent.getLastModified())
                .bookingStatus(ticketEvent.getBookingStatus())
                .build();
    }
}

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
        ticketDetails.setSourceStation(bookingJournal.getSourceStation());
        ticketDetails.setDestinationStation(bookingJournal.getDestinationStation());
        ticketDetails.setNumberOfPassengers(bookingJournal.getNumberOfPassengers());
        ticketDetails.setAmount(bookingJournal.getAmount());
        ticketDetails.setTicketNumber(bookingJournal.getTicketNumber());
        ticketDetails.setJourneyDate(bookingJournal.getJourneyDate());
        ticketDetails.setLastModified(bookingJournal.getLastModified());
        ticketDetails.setBookingStatus(bookingJournal.getBookingStatus());

        return stagingRepository.save(ticketDetails);
    }

    @Override
    public void saveTicketDetails(TicketEvent ticketEvent) {
        logger.info("Ticket Event received in staging-server - {}",ticketEvent.toString());
        BookingJournal ticketDetails = stagingRepository.findByTicketNumber(ticketEvent.getTicketNumber());
        if (Objects.isNull(ticketDetails)) {
            BookingJournal bookingJournal = getDataFromTicketEvent(ticketEvent);
            stagingRepository.save(bookingJournal);
        } else {
            ticketDetails.setSourceStation(ticketEvent.getSourceStation());
            ticketDetails.setDestinationStation(ticketEvent.getDestinationStation());
            ticketDetails.setNumberOfPassengers(ticketEvent.getNumberOfPassengers());
            ticketDetails.setAmount(ticketEvent.getAmount());
            ticketDetails.setTicketNumber(ticketEvent.getTicketNumber());
            ticketDetails.setJourneyDate(ticketEvent.getJourneyDate());
            ticketDetails.setLastModified(ticketEvent.getLastModified());
            ticketDetails.setBookingStatus(ticketEvent.getBookingStatus());

            stagingRepository.save(ticketDetails);
            //stagingRepository.updateTicketDetails(ticketEvent.getTicketNumber(), ticketEvent.getSourceStation(), ticketEvent.getDestinationStation(), ticketEvent.getNumberOfPassengers(), ticketEvent.getAmount(), ticketEvent.getBookingStatus());
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

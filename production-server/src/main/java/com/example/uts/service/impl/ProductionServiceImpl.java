package com.example.uts.service.impl;

import com.example.uts.constants.CommonConstants;
import com.example.uts.dto.TicketEvent;
import com.example.uts.enums.BookingStatus;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ProductionRepository;
import com.example.uts.service.ProductionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProductionServiceImpl implements ProductionService {
    private static final Logger logger = LoggerFactory.getLogger(ProductionServiceImpl.class);

    @Autowired
    private ProductionRepository productionRepository;

    @Override
    public BookingJournal getTicketDetails(Integer id) {
       return productionRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal saveTicketDetails(BookingJournal bookingJournal) {
        return productionRepository.save(bookingJournal);
    }

    @Override
    public void saveTicketDetails(TicketEvent ticketEvent) {
        logger.info("Ticket Event received in production-server - {}",ticketEvent.toString());
        BookingJournal bookingJournal = getDataFromTicketEvent(ticketEvent);
        productionRepository.save(bookingJournal);
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

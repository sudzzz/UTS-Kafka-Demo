package com.example.uts.service.impl;

import com.example.uts.constants.CommonConstants;
import com.example.uts.dto.TicketEvent;
import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ProductionRepository;
import com.example.uts.service.ProductionService;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class ProductionServiceImpl implements ProductionService {
    private static final Logger logger = LoggerFactory.getLogger(ProductionServiceImpl.class);

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private NewTopic topic;

    @Autowired
    private KafkaTemplate<String,TicketEvent> kafkaTemplate;

    @Override
    public BookingJournal getTicketDetails(Integer id) {
       return productionRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal updateTicketDetails(String ticketNumber, BookingJournal bookingJournal) throws TicketNotFoundException {
        BookingJournal ticket = productionRepository.findByTicketNumber(ticketNumber);
        if(Objects.isNull(ticket)){
            throw new TicketNotFoundException("Ticket with given ticket number is not present in the database");
        }
        BookingJournal savedTicket =  productionRepository.save(bookingJournal);
        TicketEvent ticketEvent = getTicketEvent(savedTicket);
        sendMessage(ticketEvent);
        return savedTicket;
    }

    @Override
    public void saveTicketDetails(TicketEvent ticketEvent) {
        logger.info("Ticket Event received in production-server - {}",ticketEvent.toString());
        BookingJournal bookingJournal = getDataFromTicketEvent(ticketEvent);
        productionRepository.save(bookingJournal);
        sendMessage(ticketEvent);
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

    private TicketEvent getTicketEvent(BookingJournal savedTicket){
        return TicketEvent.builder()
                .id(savedTicket.getId())
                .ticketNumber(savedTicket.getTicketNumber())
                .sourceStation(savedTicket.getSourceStation())
                .destinationStation(savedTicket.getDestinationStation())
                .numberOfPassengers(savedTicket.getNumberOfPassengers())
                .amount(savedTicket.getAmount())
                .journeyDate(savedTicket.getJourneyDate())
                .lastModified(savedTicket.getLastModified())
                .bookingStatus(savedTicket.getBookingStatus())
                .build();
    }

    private void sendMessage(TicketEvent ticketEvent){
        logger.info("Ticket event produced for reporting and staging server--> {}",ticketEvent.toString());
        //create message
        Message<TicketEvent> message = MessageBuilder
                .withPayload(ticketEvent)
                .setHeader(KafkaHeaders.TOPIC,topic.name()).build();

        kafkaTemplate.send(message);
    }
}

package com.example.uts.service.impl;

import com.example.uts.dto.TicketEvent;
import com.example.uts.exception.TicketNotFoundException;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ReportingRepository;
import com.example.uts.service.ReportingService;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ReportingServiceImpl implements ReportingService {

    private static final Logger logger = LoggerFactory.getLogger(ReportingServiceImpl.class);

    @Autowired
    private NewTopic topic;

    @Autowired
    private KafkaTemplate<String,TicketEvent> kafkaTemplate;

    @Autowired
    private ReportingRepository reportingRepository;
    @Override
    public BookingJournal getTicketDetails(int id) {
        return reportingRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal updateTicketDetails(String ticketNumber, BookingJournal bookingJournal) throws TicketNotFoundException {
        BookingJournal ticketDetails = reportingRepository.findByTicketNumber(ticketNumber);
        if(Objects.isNull(ticketDetails)){
            throw new TicketNotFoundException("Ticket with given ticket number is not present in the database");
        }
        ticketDetails.setSourceStation(bookingJournal.getSourceStation());
        ticketDetails.setDestinationStation(bookingJournal.getDestinationStation());
        ticketDetails.setNumberOfPassengers(bookingJournal.getNumberOfPassengers());
        ticketDetails.setAmount(bookingJournal.getAmount());
        ticketDetails.setTicketNumber(bookingJournal.getTicketNumber());
        ticketDetails.setJourneyDate(bookingJournal.getJourneyDate());
        ticketDetails.setLastModified(bookingJournal.getLastModified());
        ticketDetails.setBookingStatus(bookingJournal.getBookingStatus());

        BookingJournal updatedTicket = reportingRepository.save(ticketDetails);
        TicketEvent ticketEvent = getTicketEvent(updatedTicket);
        sendMessage(ticketEvent);
        return updatedTicket;
    }

    @Override
    public void saveTicketDetails(TicketEvent ticketEvent) throws TicketNotFoundException {
        logger.info("Ticket Event received in reporting-server - {}", ticketEvent.toString());
        BookingJournal ticketDetails = reportingRepository.findByTicketNumber(ticketEvent.getTicketNumber());
        if (Objects.isNull(ticketDetails)) {
            BookingJournal bookingJournal = getDataFromTicketEvent(ticketEvent);
            reportingRepository.save(bookingJournal);
        } else {
            ticketDetails.setSourceStation(ticketEvent.getSourceStation());
            ticketDetails.setDestinationStation(ticketEvent.getDestinationStation());
            ticketDetails.setNumberOfPassengers(ticketEvent.getNumberOfPassengers());
            ticketDetails.setAmount(ticketEvent.getAmount());
            ticketDetails.setTicketNumber(ticketEvent.getTicketNumber());
            ticketDetails.setJourneyDate(ticketEvent.getJourneyDate());
            ticketDetails.setLastModified(ticketEvent.getLastModified());
            ticketDetails.setBookingStatus(ticketEvent.getBookingStatus());

            reportingRepository.save(ticketDetails);
            //reportingRepository.updateTicketDetails(ticketEvent.getTicketNumber(), ticketEvent.getSourceStation(), ticketEvent.getDestinationStation(), ticketEvent.getNumberOfPassengers(), ticketEvent.getAmount(), ticketEvent.getBookingStatus());
        }
    }

    @Transactional
    private BookingJournal updateAndReturn(String ticketNumber, BookingJournal bookingJournal){
        reportingRepository.updateTicketDetails(ticketNumber,bookingJournal.getSourceStation(),bookingJournal.getDestinationStation(),bookingJournal.getNumberOfPassengers(),bookingJournal.getAmount(),bookingJournal.getBookingStatus());
        BookingJournal updatedTicket = reportingRepository.findByTicketNumber(ticketNumber);
        TicketEvent ticketEvent = getTicketEvent(updatedTicket);
        sendMessage(ticketEvent);
        return updatedTicket;
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
        logger.info("Ticket event produced for staging server--> {}",ticketEvent.toString());
        //create message
        Message<TicketEvent> message = MessageBuilder
                .withPayload(ticketEvent)
                .setHeader(KafkaHeaders.TOPIC, topic.name()).build();

        kafkaTemplate.send(message);
    }

}

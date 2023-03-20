package com.example.uts.service.impl;

import com.example.uts.dto.TicketEvent;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ThinClientRepository;
import com.example.uts.request.BookTicketRequest;
import com.example.uts.service.ThinClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ThinClientServiceImpl implements ThinClientService {

    private static final Logger logger = LoggerFactory.getLogger(ThinClientServiceImpl.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, TicketEvent> kafkaTemplate;

    @Autowired
    private NewTopic topic;
    @Autowired
    private ThinClientRepository thinClientRepository;

    @Override
    public BookingJournal getTicketDetails(int id) {
        return thinClientRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal saveTicketDetails(BookTicketRequest bookTicketRequest) {
        BookingJournal bookingJournal = bookTicketRequest.to();
        BookingJournal savedTicket = thinClientRepository.save(bookingJournal);
        TicketEvent ticketEvent = getTicketEvent(savedTicket);
        sendMessage(ticketEvent);
        return savedTicket;
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

    public void sendMessage(TicketEvent ticketEvent) {
        logger.info("Ticket event produced for production-server --> {}",ticketEvent.toString());

        //create message
        Message<TicketEvent> message = MessageBuilder
                .withPayload(ticketEvent)
                .setHeader(KafkaHeaders.TOPIC,topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}

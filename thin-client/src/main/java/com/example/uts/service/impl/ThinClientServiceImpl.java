package com.example.uts.service.impl;

import com.example.uts.constants.CommonConstants;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ThinClientRepository;
import com.example.uts.request.BookTicketRequest;
import com.example.uts.service.ThinClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ThinClientServiceImpl implements ThinClientService {

    private static final Logger logger = LoggerFactory.getLogger(ThinClientServiceImpl.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ThinClientRepository thinClientRepository;

    @Override
    public BookingJournal getTicketDetails(int id) {
        return thinClientRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal saveTicketDetails(BookTicketRequest bookTicketRequest) throws JsonProcessingException {
        BookingJournal bookingJournal = bookTicketRequest.to();
        BookingJournal savedTicket = thinClientRepository.save(bookingJournal);
        JSONObject jsonObject = getTicketEvent(savedTicket);
        sendMessage(jsonObject);
        return savedTicket;
    }

    private JSONObject getTicketEvent(BookingJournal savedTicket){
        JSONObject data = new JSONObject();
        data.put(CommonConstants.ID,savedTicket.getId());
        data.put(CommonConstants.TICKET_NUMBER,savedTicket.getTicketNumber());
        data.put(CommonConstants.SOURCE_STATION,savedTicket.getSourceStation());
        data.put(CommonConstants.DESTINATION_STATION,savedTicket.getDestinationStation());
        data.put(CommonConstants.NUMBER_OF_PASSENGERS,savedTicket.getNumberOfPassengers());
        data.put(CommonConstants.AMOUNT,savedTicket.getAmount());
        data.put(CommonConstants.JOURNEY_DATE,savedTicket.getJourneyDate());
        data.put(CommonConstants.LAST_MODIFIED,savedTicket.getLastModified());
        data.put(CommonConstants.BOOKING_STATUS,savedTicket.getBookingStatus());
        return data;
    }

    public void sendMessage(JSONObject jsonObject) throws JsonProcessingException {
        logger.info("Ticket event --> {}",jsonObject.toString());

        //create message
        kafkaTemplate.send(CommonConstants.BOOKING_THIN_CLIENT,objectMapper.writeValueAsString(jsonObject));
    }
}

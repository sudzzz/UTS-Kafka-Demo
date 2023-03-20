package com.example.uts.service.impl;

import com.example.uts.constants.CommonConstants;
import com.example.uts.enums.BookingStatus;
import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ProductionRepository;
import com.example.uts.service.ProductionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public BookingJournal getTicketDetails(Integer id) {
       return productionRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal saveTicketDetails(BookingJournal bookingJournal) {
        return productionRepository.save(bookingJournal);
    }

    @Override
    public void saveTicketDetails(String message) throws ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(message);
        BookingJournal bookingJournal = getDataFromJsonObject(data);
        productionRepository.save(bookingJournal);
    }

    private BookingJournal getDataFromJsonObject(JSONObject data){
        return BookingJournal.builder()
                .id((Integer) data.get(CommonConstants.ID))
                .ticketNumber((String) data.get(CommonConstants.TICKET_NUMBER))
                .sourceStation((String) data.get(CommonConstants.SOURCE_STATION))
                .destinationStation((String) data.get(CommonConstants.DESTINATION_STATION))
                .numberOfPassengers((Integer) data.get(CommonConstants.NUMBER_OF_PASSENGERS))
                .amount((Double) data.get(CommonConstants.AMOUNT))
                .journeyDate((Date) data.get(CommonConstants.JOURNEY_DATE))
                .lastModified((Date) data.get(CommonConstants.LAST_MODIFIED))
                .bookingStatus(BookingStatus.valueOf((String) data.get(CommonConstants.BOOKING_STATUS)))
                .build();
    }
}

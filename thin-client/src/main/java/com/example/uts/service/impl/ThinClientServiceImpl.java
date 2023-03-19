package com.example.uts.service.impl;

import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ThinClientRepository;
import com.example.uts.request.BookTicketRequest;
import com.example.uts.service.ThinClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThinClientServiceImpl implements ThinClientService {
    @Autowired
    private ThinClientRepository thinClientRepository;

    @Override
    public BookingJournal getTicketDetails(int id) {
        return thinClientRepository.findById(id).orElse(null);
    }

    @Override
    public BookingJournal saveTicketDetails(BookTicketRequest bookTicketRequest) {
        BookingJournal bookingJournal = bookTicketRequest.to();
        return thinClientRepository.save(bookingJournal);
    }
}

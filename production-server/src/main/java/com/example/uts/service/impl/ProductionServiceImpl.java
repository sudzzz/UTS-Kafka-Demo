package com.example.uts.service.impl;

import com.example.uts.model.BookingJournal;
import com.example.uts.repository.ProductionRepository;
import com.example.uts.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductionServiceImpl implements ProductionService {
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
}

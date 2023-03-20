package com.example.uts.repository;

import com.example.uts.model.BookingJournal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRepository extends MongoRepository<BookingJournal,Integer> {
    BookingJournal findByTicketNumber(String ticketNumber);
}

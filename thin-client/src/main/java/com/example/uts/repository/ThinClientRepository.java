package com.example.uts.repository;

import com.example.uts.model.BookingJournal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThinClientRepository extends JpaRepository<BookingJournal,Integer> {
}

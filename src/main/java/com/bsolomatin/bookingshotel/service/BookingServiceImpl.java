package com.bsolomatin.bookingshotel.service;

import com.bsolomatin.bookingshotel.domain.Booking;
import com.bsolomatin.bookingshotel.repository.BookingsRepository;
import org.joda.time.Interval;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingsRepository bookingsRepository;

    @Override
    public Booking findById(Long id) {
        return bookingsRepository.getOne(id);
    }

    @Override
    public List<Booking> findAll() {
        return bookingsRepository.findAll();
    }



    @Override
    public void deleteById(Long id) {
        bookingsRepository.deleteById(id);

    }

    public boolean canReserve(Long id, LocalDate d1, LocalDate d2) {
        return bookingsRepository.bookingListById(id,d1,d2).size() == 0;
    }


}

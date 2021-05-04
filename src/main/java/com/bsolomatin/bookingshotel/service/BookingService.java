package com.bsolomatin.bookingshotel.service;

import com.bsolomatin.bookingshotel.domain.Booking;
import java.util.List;

public interface BookingService {
    Booking findById(Long id);
    List<Booking> findAll();
    void deleteById(Long id);
}

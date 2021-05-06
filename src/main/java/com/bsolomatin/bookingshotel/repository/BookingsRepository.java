package com.bsolomatin.bookingshotel.repository;

import com.bsolomatin.bookingshotel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long> {
    @Query(value = "Select * From hotel.booking Where room_id=:id and (check_in, check_out) OVERLAPS (:d1, :d2)", nativeQuery = true)
    List<Booking> bookingListById(
            @Param("id") Long id,
            @Param("d1") LocalDate d1,
            @Param("d2") LocalDate d2);

    @Query(value = "Select * From hotel.booking Where id = :ids", nativeQuery = true)
    Booking findByBookingId(@Param("ids") Long id);
}
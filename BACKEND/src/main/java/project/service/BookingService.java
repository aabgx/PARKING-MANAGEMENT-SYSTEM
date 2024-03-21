package project.service;

import project.DTO.BookingDTO;
import project.entities.BookingEntity;


import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    int save(BookingEntity booking);

    void delete(int bookingId) throws Exception;

    void update(BookingEntity bookingEntity);

    BookingDTO getById(int bookingId) throws Exception;

    List<BookingDTO> getAllBookingsByDatesAndSection(List<LocalDate> dates, String sectionName);

    BookingEntity findById(int bookingId) throws Exception;

    List<BookingEntity> getAllBookings();

    void deleteBookingsWithParkingId(int id) throws Exception;
}

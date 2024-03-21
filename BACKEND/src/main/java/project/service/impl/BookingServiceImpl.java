package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DTO.BookingDTO;
import project.email.EmailSender;
import project.entities.BookingEntity;
import project.repositories.BookingRepository;
import project.service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public int save(BookingEntity booking) {
        BookingEntity createdBooking = this.bookingRepository.save(booking);
        String to = createdBooking.getUserEntity().getEmail();
        String subject = "Parking Management - Booking Info";
        String body = "Parking space number: " + createdBooking.getParkingSpaceEntity().getId() +"\n" +
                "Parking type: " + createdBooking.getParkingSpaceEntity().getType() + "\n" +
                "Date: " + createdBooking.getDate();
        EmailSender.sendEmail(to, subject, body);
        return createdBooking.getId();
    }

    @Override
    public void delete(int bookingId) throws Exception {
        boolean existsBooking = bookingRepository.existsBooking(bookingId);
        if (existsBooking) {
            this.bookingRepository.deleteById(bookingId);
            return;
        }
        throw new Exception("Booking not found.");
    }

    @Override
    public void update(BookingEntity booking) {
        bookingRepository.save(booking);
    }

    @Override
    public BookingDTO getById(int bookingId) throws Exception {
        BookingDTO booking = bookingRepository.getBookingById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found.");
        }

        return booking;
    }

    @Override
    public List<BookingDTO> getAllBookingsByDatesAndSection(List<LocalDate> dates, String sectionName) {
        return this.bookingRepository.getAllBookingsByDatesAndSection(dates, sectionName);
    }

    @Override
    public BookingEntity findById(int bookingId) throws Exception {
        Optional<BookingEntity> optionalBookingEntity = bookingRepository.findById(bookingId);
        if (optionalBookingEntity.isPresent()) {
            return optionalBookingEntity.get();
        }
        throw new Exception("Booking not found.");
    }

    @Override
    public List<BookingEntity> getAllBookings() {
        return this.bookingRepository.findAll();
    }

    @Override
    public void deleteBookingsWithParkingId(int id) throws Exception {
        for(BookingEntity booking : bookingRepository.findAll()){
            delete(booking.getId());
        }
    }


}

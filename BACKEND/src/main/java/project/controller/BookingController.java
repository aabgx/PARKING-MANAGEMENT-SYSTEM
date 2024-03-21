package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DTO.BookingDTO;
import project.DTO.BookingUpdateDTO;
import project.DTO.SectionDatesBookingRequest;
import project.config.JwtService;
import project.entities.*;
import project.service.BookingService;
import project.service.ParkingSpaceService;
import project.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @PostMapping("/save")
    public @ResponseBody ResponseEntity<?> saveBooking(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody BookingDTO bookingDTO) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            BookingEntity booking;
            UserEntity user;
            ParkingSpaceEntity parkingSpace;
            String userName;
            int parkingSpaceId;
            LocalDate date;

            booking = new BookingEntity();
            userName = bookingDTO.getUserName();
            user = userService.getByUsername(userName);
            parkingSpaceId = bookingDTO.getParkingSpaceId();
            parkingSpace = parkingSpaceService.getById(parkingSpaceId).get();
            date = bookingDTO.getDate();


            booking.setUserEntity(user);
            booking.setParkingSpaceEntity(parkingSpace);
            booking.setDate(date);

            int bookingId = bookingService.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/get") 
    public @ResponseBody ResponseEntity<?> getAllBookings(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            List<BookingDTO> bookings = new ArrayList<>();
            for(BookingEntity booking : bookingService.getAllBookings()){
                BookingDTO bookingDTO = new BookingDTO(booking.getUserEntity().getUsername(), booking.getParkingSpaceEntity().getId(), booking.getDate());
                bookings.add(bookingDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(bookings);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/get/booking")
    public @ResponseBody ResponseEntity<?> getBookingById(@RequestHeader("Authorization") String authorizationHeader,
                                                          @RequestBody String id) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            BookingDTO booking = bookingService.getById(Integer.parseInt(id));
            return ResponseEntity.status(HttpStatus.OK).body(booking);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/get/by-dates-and-section")
    public @ResponseBody ResponseEntity<?> getAllBookingsByDatesAndSection(@RequestHeader("Authorization") String authorizationHeader,
                                                                           @RequestBody SectionDatesBookingRequest sectionDatesBookingRequest) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }

            List<BookingDTO> bookings = bookingService.getAllBookingsByDatesAndSection(sectionDatesBookingRequest.getDates(), sectionDatesBookingRequest.getSectionName());
            return ResponseEntity.status(HttpStatus.OK).body(bookings);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<?> deleteBooking(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestBody BookingDTO bookingDTO) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }

            // Find the booking to delete
            List<BookingEntity> allBookings = bookingService.getAllBookings();
            BookingEntity bookingToDelete = allBookings.stream()
                    .filter(b -> b.getParkingSpaceEntity().getId() == bookingDTO.getParkingSpaceId()
                            && b.getDate().isEqual(bookingDTO.getDate()))
                    .findFirst()
                    .orElse(null);

            if (bookingToDelete == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
            }

            // Delete the booking
            bookingService.delete(bookingToDelete.getId());
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/update")
    public @ResponseBody ResponseEntity<?> updateBooking(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestBody BookingUpdateDTO bookingUpdateDTO) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            BookingEntity booking;
            UserEntity user;
            ParkingSpaceEntity parkingSpace;
            int parkingSpaceId;
            String username;
            LocalDate date;

            booking = bookingService.findById(bookingUpdateDTO.getBookingId());
            parkingSpaceId = bookingUpdateDTO.getParkingSpaceId();
            parkingSpace = parkingSpaceService.getById(parkingSpaceId).get();
            username = bookingUpdateDTO.getUsername();
            user = userService.getByUsername(username);
            date = bookingUpdateDTO.getDate();

            booking.setUserEntity(user);
            booking.setParkingSpaceEntity(parkingSpace);
            booking.setDate(date);

            bookingService.update(booking);
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}

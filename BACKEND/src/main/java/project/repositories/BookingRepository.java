package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.DTO.BookingDTO;
import project.entities.BookingEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
//    @Query("select b.userEntity.username as userName, b.parkingSpaceEntity.id as parkingSpaceId, b.date as date from BookingEntity b")
//    List<BookingDTO> getAllBookings();

    @Query("select b.userEntity.username as userName, b.parkingSpaceEntity.id as parkingSpaceId, b.date as date from BookingEntity b" +
    " where b.date in :dates and b.parkingSpaceEntity.gridItemEntity.sectionEntity.name = :sectionName")
    List<BookingDTO> getAllBookingsByDatesAndSection(@Param("dates") List<LocalDate> dates, @Param("sectionName") String sectionName);
    @Query("select b.userEntity.username as userName, b.parkingSpaceEntity.id as parkingSpaceId, b.date as date from BookingEntity b" +
            " where b.id = :bookingId")
    BookingDTO getBookingById(@Param("bookingId") int bookingId);

    @Query("select count(b.id) > 0 from BookingEntity b where b.id = :bookingId")
    boolean existsBooking(@Param("bookingId") int bookingId);

    @Query("delete from BookingEntity b where b.parkingSpaceEntity.id = :id")
    List<BookingEntity> deleteBookingByParkingSpace(@Param("id") int id);
}

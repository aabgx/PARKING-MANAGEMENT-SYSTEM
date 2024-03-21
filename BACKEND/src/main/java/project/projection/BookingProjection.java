package project.projection;

import java.time.LocalDate;

public interface BookingProjection {
    int getUserId();

    int getParkingSpaceId();

    LocalDate getDate();
}

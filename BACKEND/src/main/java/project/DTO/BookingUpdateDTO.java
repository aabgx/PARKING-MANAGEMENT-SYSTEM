package project.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingUpdateDTO {
    private int bookingId;

    private String username;

    private int parkingSpaceId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}

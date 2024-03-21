package project.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.Locale;

@Data
@AllArgsConstructor
public class BookingDTO {
    private String userName;

    private int parkingSpaceId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}

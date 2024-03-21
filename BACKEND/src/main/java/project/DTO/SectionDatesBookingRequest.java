package project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class SectionDatesBookingRequest {

    private List<LocalDate> dates;

    private String sectionName;
}

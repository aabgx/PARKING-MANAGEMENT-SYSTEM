package project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ParkingSpacesWraperDTO {
    private List<ParkingSpaceGridDTO> parkingSpaces;
}

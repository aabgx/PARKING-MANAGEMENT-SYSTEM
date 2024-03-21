package project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ParkingSpaceDTO {
    private int id;
    private int gridItemId;
    private String type;
}

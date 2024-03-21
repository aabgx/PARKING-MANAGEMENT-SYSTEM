package project.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParkingSpaceGridDTO {
    private int id;
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;
    private String orientation;
    private String section;
}

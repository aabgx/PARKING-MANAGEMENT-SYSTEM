package project.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class RoadDTO {
    int id;
    int cols;
    int rows;
    int y;
    int x;
    String type;
    String section;
    String orientation;
}

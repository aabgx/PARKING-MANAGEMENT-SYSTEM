package project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SectionUpdateDTO {
    int id;
    String name;
    int height;
    int width;

}

package project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserSaveDTO {
    String username;
    String password;
    String email;
    String phone;
    String userType;
}

package project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyDTO {
    String username;
    String phone;
    String email;
    String newPassword;
    String confirmNewPassword;
}

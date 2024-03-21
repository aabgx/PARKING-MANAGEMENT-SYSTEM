package project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usertable")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username",unique=true)
    private String username;
    @Column(name = "password")
    private String password;

    @Column(name = "phone",unique=true)
    private String phone;

    @Column(name = "email",unique=true)
    private String email;

    @Column(name = "userrole")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<BookingEntity> bookings = new ArrayList<>();
    public boolean isValidData(String phone, String email){
        if(phone.length() != 10){
            return false;
        }
        //phone policy
        boolean isPhoneValid = phone.matches("\\d+");
        //email standard
        boolean isEmailValid = email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        return  isPhoneValid && isEmailValid;
    }
}
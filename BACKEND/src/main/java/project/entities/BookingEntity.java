package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "bookingtable")
public class BookingEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity userEntity;

    @OneToOne
    @JoinColumn(name = "parkingspaceid")
    private ParkingSpaceEntity parkingSpaceEntity;

    @Column(name = "date")
    private LocalDate date;
}

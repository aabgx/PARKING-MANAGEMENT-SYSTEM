package project.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "parkingspacetable")
public class ParkingSpaceEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "griditemid")
    private GridItemEntity gridItemEntity;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ParkingSpaceType type;
}

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
@Table(name = "roadtable")
public class RoadEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name =  "griditemid")
    private GridItemEntity gridItemEntity;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;
}

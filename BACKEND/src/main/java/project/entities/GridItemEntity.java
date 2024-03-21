package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "griditemtable")
public class GridItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @ManyToOne
    @JoinColumn(name = "sectionid")
    private SectionEntity sectionEntity;

    @Column(name = "orientation")
    @Enumerated(EnumType.STRING)
    private Orientation orientation;

    @OneToMany(mappedBy = "gridItemEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ParkingSpaceEntity> parkingSpaces = new ArrayList<>();

    public GridItemEntity(int id, int x, int y, int width, int height, SectionEntity sectionEntity, Orientation orientation) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sectionEntity = sectionEntity;
        this.orientation = orientation;
    }
}

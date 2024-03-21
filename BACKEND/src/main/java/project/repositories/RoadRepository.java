package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.RoadEntity;

@Repository
public interface RoadRepository extends JpaRepository<RoadEntity, Integer> {
}

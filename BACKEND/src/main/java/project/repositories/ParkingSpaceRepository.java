package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.ParkingSpaceEntity;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, Integer> {
}

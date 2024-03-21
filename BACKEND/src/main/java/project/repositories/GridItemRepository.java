package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.GridItemEntity;

@Repository
public interface GridItemRepository extends JpaRepository<GridItemEntity, Integer> {
}

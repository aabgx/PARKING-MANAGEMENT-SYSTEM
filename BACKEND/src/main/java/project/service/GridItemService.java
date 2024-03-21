package project.service;

import project.entities.GridItemEntity;
import java.util.Optional;

public interface GridItemService {
    Optional<GridItemEntity> getById(long id) throws Exception;
    GridItemEntity getGridItemEntity(int x, int y, int width, int height, String orientation) throws  Exception;
    GridItemEntity saveGridItemEntity(GridItemEntity gridItemEntity) throws Exception;
    void deleteAllGrids() throws Exception;
    void deleteById(int id);
}

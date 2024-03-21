package project.service;

import org.springframework.stereotype.Service;
import project.entities.GridItemEntity;
import project.entities.ParkingSpaceEntity;

import java.util.List;
import java.util.Optional;

public interface ParkingSpaceService {
    ParkingSpaceEntity add(ParkingSpaceEntity parkingSpace) throws Exception;
    Optional<ParkingSpaceEntity> getById(int id) throws Exception;
    void update(ParkingSpaceEntity parkingSpace) throws Exception;
    void deleteById(int parkingSpaceId) throws Exception;
    List<ParkingSpaceEntity> getAll() throws Exception;
    List<ParkingSpaceEntity> getBySectionName(String name) throws Exception;
    void overrideParkingSpaces(List<ParkingSpaceEntity> parkingSpaceEntities) throws Exception;
}

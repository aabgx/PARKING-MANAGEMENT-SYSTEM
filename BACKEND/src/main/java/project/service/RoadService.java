package project.service;

import project.entities.RoadEntity;

import java.util.List;

public interface RoadService {
    List<RoadEntity> getAllRoads();
    List<RoadEntity> addAllRoads(List<RoadEntity> roads);
    void deleteAllRoads();
}

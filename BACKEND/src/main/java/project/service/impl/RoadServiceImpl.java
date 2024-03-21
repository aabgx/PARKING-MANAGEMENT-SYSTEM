package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DTO.RoadDTO;
import project.entities.GridItemEntity;
import project.entities.ParkingSpaceEntity;
import project.entities.RoadEntity;
import project.entities.Type;
import project.repositories.RoadRepository;
import project.repositories.SectionRepository;
import project.service.RoadService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoadServiceImpl implements RoadService {
    @Autowired
    RoadRepository roadRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Override
    public List<RoadEntity> getAllRoads() {
        return roadRepository.findAll();
    }

    @Override
    public List<RoadEntity> addAllRoads(List<RoadEntity> roads) {
        return roadRepository.saveAll(roads);
    }

    @Override
    public void deleteAllRoads() {
        roadRepository.deleteAll();
    }

}
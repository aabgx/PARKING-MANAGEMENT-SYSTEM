package project.service;

import org.springframework.dao.EmptyResultDataAccessException;
import project.entities.ParkingSpaceEntity;
import project.entities.SectionEntity;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    void add(SectionEntity sectionEntity) throws Exception;
    Optional<SectionEntity> getById(int id) throws Exception;
    void update(SectionEntity sectionEntity) throws Exception;
    void delete(int sectionEntityId) throws Exception;
    List<SectionEntity> getAllSections() throws Exception;
    SectionEntity getByName(String name) throws Exception;
}

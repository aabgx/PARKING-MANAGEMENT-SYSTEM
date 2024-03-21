package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.entities.*;
import project.repositories.GridItemRepository;
import project.repositories.ParkingSpaceRepository;
import project.repositories.SectionRepository;
import project.repositories.UserRepository;
import project.service.GridItemService;
import project.service.SectionService;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GridItemRepository gridItemRepository;
    @Autowired
    ParkingSpaceRepository parkingSpaceRepository;

    @Override
    public void add(SectionEntity sectionEntity) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.getByUsername(username);
        if(!user.getUserType().equals(UserType.ADMIN))
            throw new Exception("Not allowed.");
        sectionRepository.save(sectionEntity);
    }

    @Override
    public Optional<SectionEntity> getById(int id) throws Exception {
        return sectionRepository.findById(id);
    }

    @Override
    public void update(SectionEntity sectionEntity) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.getByUsername(username);
        if(!user.getUserType().equals(UserType.ADMIN))
            throw new Exception("Not allowed.");
        sectionRepository.save(sectionEntity);
    }

    private List<GridItemEntity> getGridItems(int sectionId){
        List<GridItemEntity> gridItemEntities = new ArrayList<>();
        for(GridItemEntity gridItemEntity : gridItemRepository.findAll()){
            if(gridItemEntity.getSectionEntity().getId() == sectionId){
                gridItemEntities.add(gridItemEntity);
            }
        }
        return  gridItemEntities;
    }

    private List<ParkingSpaceEntity> getParkingSpaces(int sectionId){
        List<ParkingSpaceEntity> gridItemEntities = new ArrayList<>();
        for(ParkingSpaceEntity gridItemEntity : parkingSpaceRepository.findAll()){
            if(gridItemEntity.getGridItemEntity().getId() == sectionId){
                gridItemEntities.add(gridItemEntity);
            }
        }
        return  gridItemEntities;
    }
    @Override
    public void delete(int sectionEntityId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.getByUsername(username);
        if(!user.getUserType().equals(UserType.ADMIN))
            throw new Exception("Not allowed.");
        Optional<SectionEntity> sectionEntityOptional = sectionRepository.findById(sectionEntityId);
            if (sectionEntityOptional.isPresent()) {
                SectionEntity sectionEntity = sectionEntityOptional.get();
                for (GridItemEntity gridItemEntity : getGridItems(sectionEntity.getId())) {
                    parkingSpaceRepository.deleteAll(getParkingSpaces(gridItemEntity.getId()));
                    gridItemRepository.delete(gridItemEntity);
                }
                sectionRepository.delete(sectionEntity);
            }
    }

    @Override
    public List<SectionEntity> getAllSections() throws Exception {
        return sectionRepository.findAll();
    }

    @Override
    public SectionEntity getByName(String name) throws Exception {
        return sectionRepository.getByName(name);
    }
}

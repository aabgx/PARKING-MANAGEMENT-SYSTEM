package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.entities.GridItemEntity;
import project.entities.ParkingSpaceEntity;
import project.entities.UserType;
import project.repositories.GridItemRepository;
import project.repositories.UserRepository;
import project.service.GridItemService;

import java.util.Optional;

@Service
public class GridItemServiceImpl implements GridItemService {
    @Autowired
    private GridItemRepository gridItemRepo;
    @Autowired
    private UserRepository userRepository;
    public Optional<GridItemEntity> getById(long id) throws Exception {
        return gridItemRepo.findById(Math.toIntExact(id));
    }
    @Override
    public GridItemEntity getGridItemEntity(int x, int y, int width, int height, String orientation) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
        for(GridItemEntity gridItemEntity : gridItemRepo.findAll()){
            if(gridItemEntity.getX() == x && gridItemEntity.getY() == y && gridItemEntity.getHeight() == height && gridItemEntity.getWidth() == width && gridItemEntity.getOrientation().toString().equals(orientation))
                return gridItemEntity;
        }
        return null;
    }

    @Override
    public GridItemEntity saveGridItemEntity(GridItemEntity gridItemEntity) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
        try {
            return gridItemRepo.save(gridItemEntity);
        }catch(Exception e){
            throw new Exception("Failed to save in database grid item " + e.getMessage());
        }
    }

    @Override
    public void deleteAllGrids() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
        try {
           gridItemRepo.deleteAll();
        }catch(Exception e){
            throw new Exception("Failed to delete from database grid item " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id){
        gridItemRepo.deleteById(id);
    }
}

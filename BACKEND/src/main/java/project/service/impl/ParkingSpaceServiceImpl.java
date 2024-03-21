package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import project.entities.*;
import project.repositories.GridItemRepository;
import project.repositories.ParkingSpaceRepository;
import project.repositories.UserRepository;
import project.service.BookingService;
import project.service.ParkingSpaceService;
import project.service.SectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GridItemRepository gridItemRepo;
    @Autowired
    SectionService sectionService;
    @Autowired
    BookingService bookingService;


    @Override
    public ParkingSpaceEntity add(ParkingSpaceEntity parkingSpace) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
        return parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public Optional<ParkingSpaceEntity> getById(int parkingSpaceId) throws Exception {
        Optional<ParkingSpaceEntity> parkingSpaceEntityOptional = parkingSpaceRepository.findById(parkingSpaceId);
        if (parkingSpaceEntityOptional.isPresent()) {
            return parkingSpaceEntityOptional;
        }

        throw new Exception("Parking space not found.");
    }


    @Override
    public void update(ParkingSpaceEntity parkingSpace) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
        parkingSpaceRepository.save(parkingSpace);
    }


    @Override
    public void deleteById(int parkingSpaceId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }

        bookingService.deleteBookingsWithParkingId(parkingSpaceId);

        parkingSpaceRepository.deleteById(Math.toIntExact(parkingSpaceId));

    }

    @Override
    public List<ParkingSpaceEntity> getAll() throws Exception {
        return parkingSpaceRepository.findAll();
    }

    @Override
    public List<ParkingSpaceEntity> getBySectionName(String name) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        project.entities.UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
        List<ParkingSpaceEntity> parkingSpaceEntities = new ArrayList<>();
        SectionEntity sectionEntity = sectionService.getByName(name);
        for(GridItemEntity gridItemEntity : sectionEntity.getGridItems()){
            parkingSpaceEntities.addAll(gridItemEntity.getParkingSpaces());
        }
        return parkingSpaceEntities;
    }

    @Override
    public void overrideParkingSpaces(List<ParkingSpaceEntity> parkingSpaceEntities) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }

        for(ParkingSpaceEntity p : getAll()){
            bookingService.deleteBookingsWithParkingId(p.getId());
            deleteById(p.getId());
        }

        for(ParkingSpaceEntity parkingSpace : parkingSpaceEntities){
            add(parkingSpace);
        }
    }


}

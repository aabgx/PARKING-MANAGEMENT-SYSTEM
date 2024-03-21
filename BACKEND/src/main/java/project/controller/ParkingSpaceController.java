package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DTO.ParkingSpaceDTO;
import project.DTO.ParkingSpaceGridDTO;
import project.DTO.ParkingSpacesWraperDTO;
import project.config.JwtService;
import project.entities.*;
import project.repositories.SectionRepository;
import project.service.GridItemService;
import project.service.ParkingSpaceService;
import project.service.SectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parkingSpace")
@CrossOrigin
public class ParkingSpaceController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ParkingSpaceService parkingSpaceService;
    @Autowired
    private GridItemService gridItemService;
    @Autowired
    private SectionService sectionService;

    @GetMapping("/get")
    public @ResponseBody ResponseEntity<?> getAllParkingSpaces(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            List<ParkingSpaceEntity> parkingSpaces;
            List<ParkingSpaceGridDTO> parkingSpaceDTOS=new ArrayList<>();
            parkingSpaces=parkingSpaceService.getAll();
            for(ParkingSpaceEntity p:parkingSpaces) {
                GridItemEntity gridItemEntity = p.getGridItemEntity();
                SectionEntity sectionEntity = gridItemEntity.getSectionEntity();
                parkingSpaceDTOS.add(new ParkingSpaceGridDTO(p.getId(),p.getType().toString(),  gridItemEntity.getX(), gridItemEntity.getY(), gridItemEntity.getWidth(), gridItemEntity.getHeight(), gridItemEntity.getOrientation().toString(), sectionEntity.getName()));
            }
            return new ResponseEntity<>(parkingSpaceDTOS, HttpStatus.OK);
        } catch (Exception ue) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot get parking spaces. " + ue.getMessage());
        }
    }

    @PostMapping("/overrideParkingSpaces")
    public @ResponseBody ResponseEntity<?> overrideExistingParkingSpaces(@RequestHeader("Authorization") String authorizationHeader,
                                                              @RequestBody ParkingSpacesWraperDTO parkingSpaceDTO){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            List<ParkingSpaceGridDTO> parkingSpaceGridDTO = parkingSpaceDTO.getParkingSpaces();
            List<ParkingSpaceEntity> newParkingSpaces = new ArrayList<>();
            for(ParkingSpaceGridDTO p : parkingSpaceGridDTO) {
                //GridItemEntity gridItemEntity = gridItemService.getGridItemEntity(p.getX(), p.getY(), p.getWidth(), p.getHeight(), p.getOrientation());
                GridItemEntity gridItemEntity = new GridItemEntity(0,p.getX(), p.getY(), p.getWidth(), p.getHeight(), sectionService.getByName(p.getSection()), Orientation.valueOf(p.getOrientation().toUpperCase()));
                gridItemEntity = gridItemService.saveGridItemEntity(gridItemEntity);
                ParkingSpaceEntity parkingSpace = new ParkingSpaceEntity(0, gridItemEntity, ParkingSpaceType.valueOf(p.getType().toUpperCase()));
                newParkingSpaces.add(parkingSpace);
            }
            List<ParkingSpaceEntity> spaces = parkingSpaceService.getAll();
            parkingSpaceService.overrideParkingSpaces(newParkingSpaces);
            for(ParkingSpaceEntity parking : spaces) {
                gridItemService.deleteById(parking.getGridItemEntity().getId());
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot save parking spaces. " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<?> deleteParkingSpace(@RequestHeader("Authorization") String authorizationHeader,
                                                              @RequestBody String id) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            Optional<ParkingSpaceEntity> parkingSpace=parkingSpaceService.getById(Integer.parseInt(id));
            if(parkingSpace.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("ParkingSpace id not found");
            }
            parkingSpaceService.deleteById(Integer.parseInt(id));
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception ue) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete parkingSpace is not working. " + ue.getMessage());
        }
    }


    @PutMapping("/update")
    public @ResponseBody ResponseEntity<?> updateParkingSpace(@RequestHeader("Authorization") String authorizationHeader,
                                                              @RequestBody ParkingSpaceDTO parkingSpaceDTO){
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            Optional<ParkingSpaceEntity> optionalParkingSpaceEntity=parkingSpaceService.getById(parkingSpaceDTO.getId());
            if (optionalParkingSpaceEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("ParkingSpace with this id was not found.");
            }
            Optional<GridItemEntity> optionalGridItemEntity=gridItemService.getById((long) parkingSpaceDTO.getGridItemId());
            GridItemEntity gridItemEntity=optionalGridItemEntity.orElseThrow(() -> new RuntimeException("GridItemEntity not present"));

            ParkingSpaceEntity parkingSpaceEntity=optionalParkingSpaceEntity.orElseThrow(() -> new RuntimeException("ParkingSpaceEntity not present"));
            parkingSpaceEntity.setGridItemEntity(gridItemEntity);
            parkingSpaceEntity.setType(ParkingSpaceType.valueOf(parkingSpaceDTO.getType().toUpperCase()));
            parkingSpaceService.update(parkingSpaceEntity);
            return new ResponseEntity<>(parkingSpaceEntity, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete user does not work. " + e.getMessage());
        }
    }

    @GetMapping("/get/{sectionName}")
    public @ResponseBody ResponseEntity<?> getParkingSpaces(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String sectionName){
        try{
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            List<ParkingSpaceGridDTO> parkingSpaceDTOS = new ArrayList<>();
            for(ParkingSpaceEntity parkingSpace : parkingSpaceService.getBySectionName(sectionName)){
                GridItemEntity gridItemEntity = parkingSpace.getGridItemEntity();
                parkingSpaceDTOS.add(new ParkingSpaceGridDTO(parkingSpace.getId(),  parkingSpace.getType().toString(), gridItemEntity.getX(), gridItemEntity.getY(), gridItemEntity.getHeight(), gridItemEntity.getWidth(), gridItemEntity.getOrientation().toString(), sectionName));
            }
            return new ResponseEntity<>(parkingSpaceDTOS, HttpStatus.OK);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot get parking space by section name! " + ex.getMessage());
        }
    }
}
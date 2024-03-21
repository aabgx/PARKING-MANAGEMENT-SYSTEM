package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DTO.RoadDTO;
import project.DTO.RoadDTOList;
import project.config.JwtService;
import project.entities.GridItemEntity;
import project.entities.Orientation;
import project.entities.RoadEntity;
import project.entities.Type;
import project.service.GridItemService;
import project.service.RoadService;
import project.service.SectionService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/road")
@CrossOrigin
public class RoadController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RoadService roadService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private GridItemService gridItemService;
    @GetMapping("/get")
    public @ResponseBody ResponseEntity<?> getAllRoads(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            List<RoadDTO> roadDTOList = new ArrayList<>();
            for (RoadEntity roadEntity : roadService.getAllRoads()) {
                RoadDTO roadDTO = new RoadDTO();
                roadDTO.setId(roadEntity.getId());
                roadDTO.setCols(roadEntity.getGridItemEntity().getWidth());
                roadDTO.setRows(roadEntity.getGridItemEntity().getHeight());
                roadDTO.setX(roadEntity.getGridItemEntity().getX());
                roadDTO.setY(roadEntity.getGridItemEntity().getY());
                roadDTO.setSection(roadEntity.getGridItemEntity().getSectionEntity().getName());
                roadDTO.setType(roadEntity.getType().toString());
                roadDTO.setOrientation(roadEntity.getGridItemEntity().getOrientation().toString());
                roadDTOList.add(roadDTO);
            }
            return new ResponseEntity<>(roadDTOList, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot get roads " + e.getMessage());
        }
    }

    @PostMapping("/saveAllRoads")
    public @ResponseBody ResponseEntity<?> saveAllRoads(@RequestHeader("Authorization") String authorizationHeader, @RequestBody RoadDTOList roadsDTO) {
        try {
            String token = authorizationHeader.substring(7);
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            List<RoadEntity> roadss = roadService.getAllRoads();
            roadService.deleteAllRoads();
            for(RoadEntity road: roadss){
                gridItemService.deleteById(road.getGridItemEntity().getId());
            }
            //gridItemService.deleteAllGrids();
            List<RoadEntity> roads = new ArrayList<>();
            for (RoadDTO roadDTO : roadsDTO.getRoads()) {
                RoadEntity road = new RoadEntity();
                GridItemEntity gridItem = new GridItemEntity();
                gridItem.setWidth(roadDTO.getCols());
                gridItem.setHeight(roadDTO.getRows());
                gridItem.setX(roadDTO.getX());
                gridItem.setY(roadDTO.getY());
                gridItem.setSectionEntity(sectionService.getByName(roadDTO.getSection()));
                gridItem.setOrientation(Orientation.valueOf(roadDTO.getOrientation().toUpperCase()));
                gridItem = gridItemService.saveGridItemEntity(gridItem);
                road.setGridItemEntity(gridItem);
                road.setType(Type.valueOf(roadDTO.getType().toUpperCase()));
                roads.add(road);
            }

            List<RoadEntity> createdRoads = roadService.addAllRoads(roads);

//            List<RoadDTO> createdRoadsDTO = new ArrayList<>();
//            RoadDTO roadDTO;
//            for (RoadEntity roadEntity : createdRoads) {
//                roadDTO = new RoadDTO();
//                roadDTO.setId(roadEntity.getId());
//                roadDTO.setCols(roadEntity.getGridItemEntity().getWidth());
//                roadDTO.setRows(roadEntity.getGridItemEntity().getHeight());
//                roadDTO.setX(roadEntity.getGridItemEntity().getX());
//                roadDTO.setY(roadEntity.getGridItemEntity().getY());
//                roadDTO.setSection(roadEntity.getGridItemEntity().getSectionEntity().getName());
//                roadDTO.setType(roadEntity.getGridItemEntity().getType().toString());
//                roadDTO.setOrientation(roadEntity.getGridItemEntity().getOrientation().toString());
//                createdRoadsDTO.add(roadDTO);
//            }

            return new ResponseEntity<>(HttpStatus.OK, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot save roads " + e.getMessage());
        }
    }
}

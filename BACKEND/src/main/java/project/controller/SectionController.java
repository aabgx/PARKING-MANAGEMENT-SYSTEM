package project.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DTO.SectionDTO;
import project.DTO.SectionUpdateDTO;
import project.config.JwtService;
import project.entities.SectionEntity;
import project.service.SectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/section")
@CrossOrigin
public class SectionController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SectionService sectionService;
    @GetMapping("/get")
    public @ResponseBody ResponseEntity<?> getAllSections(@RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            List<SectionUpdateDTO> sectionDTOList = new ArrayList<>();
            for(SectionEntity sectionEntity : sectionService.getAllSections())
                sectionDTOList.add(new SectionUpdateDTO(sectionEntity.getId(), sectionEntity.getName(), sectionEntity.getHeight(), sectionEntity.getWidth()));
            return new ResponseEntity<>(sectionDTOList, HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot get sections " + e.getMessage());
        }
    }

    @PostMapping("/save")
    public @ResponseBody ResponseEntity<?> saveSection(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SectionDTO sectionDTO){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            SectionEntity sectionEntity = new SectionEntity(0,sectionDTO.getName(), sectionDTO.getHeight(), sectionDTO.getWidth(), null);
            sectionService.add(sectionEntity);
            return new ResponseEntity<>(sectionEntity, HttpStatus.CREATED);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Save section doesn't work " + ex.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public @ResponseBody ResponseEntity<?> deleteSection(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            Optional<SectionEntity> sectionEntity = sectionService.getById(Integer.parseInt(id));
            if(sectionEntity.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Section id not found!");

            sectionService.delete(Integer.parseInt(id));
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete Section is not working! " + ex.getMessage());
        }
    }

    @PutMapping("/update")
    public @ResponseBody ResponseEntity<?> updateSection(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SectionUpdateDTO sectionDTO){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            SectionEntity sectionEntity = new SectionEntity(sectionDTO.getId(), sectionDTO.getName(), sectionDTO.getHeight(),sectionDTO.getWidth(), null);
            sectionService.update(sectionEntity);
            return new ResponseEntity<>(sectionDTO, HttpStatus.OK);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update Section is not working! " + ex.getMessage());
        }
    }

    @GetMapping("/names")
    public @ResponseBody ResponseEntity<?> getSectionNames(@RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            List<String> sectionNames = new ArrayList<>();
            for(SectionEntity sectionEntity : sectionService.getAllSections()){
                sectionNames.add(sectionEntity.getName());
            }
            return new ResponseEntity<>(sectionNames, HttpStatus.OK);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot get names! " + ex.getMessage());
        }
    }

    @GetMapping("/get/{name}")
    public @ResponseBody ResponseEntity<?> getByName(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String name){
        try{
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenInvalid(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid!");
            }
            SectionEntity sectionEntity = sectionService.getByName(name);
            SectionDTO sectionDTO = new SectionDTO(sectionEntity.getName(), sectionEntity.getHeight(),sectionEntity.getWidth());
            return new ResponseEntity<>(sectionDTO, HttpStatus.OK);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot get section with this name! " + ex.getMessage());
        }
    }



}


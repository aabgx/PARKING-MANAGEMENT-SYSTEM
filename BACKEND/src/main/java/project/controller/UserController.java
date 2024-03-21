package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import project.DTO.*;
import project.config.JwtService;
import project.DTO.AuthRequest;
import project.entities.UserEntity;
import project.entities.UserType;
import project.repositories.UserRepository;
import project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) throws JsonProcessingException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated() ) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonFormat = objectMapper.writeValueAsString(jwtService.generateToken(authRequest.getUsername()));
            return ResponseEntity.ok().body(jsonFormat);
        } else
            throw new UsernameNotFoundException("invalid user request");
    }

    @GetMapping("/validate_token")
    public @ResponseBody ResponseEntity<?> isValidToken(@RequestBody TokenDTO token){
        try{
            String username = jwtService.extractUsername(token.getToken());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/role/{username}")
    public @ResponseBody ResponseEntity<?> findUserRole(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String username){
        try{
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            UserType role = userService.getUserType(username);
            return new ResponseEntity<>(role, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Find user role does not work. " + e.getMessage());
        }
    }

    @GetMapping("/data/{username}")
    public @ResponseBody ResponseEntity<?> getUserData(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String username){
        try{
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            UserEntity userDataComplete = userService.getByUsername(username);
            UserDataDTO userDataToSend = new UserDataDTO(userDataComplete.getUsername(),userDataComplete.getEmail(),userDataComplete.getPhone(),userDataComplete.getUserType().toString());
            return new ResponseEntity<>(userDataToSend, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Find user data does not work. " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public @ResponseBody ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            List<UserListDTO> users = new ArrayList<>();
            for(UserEntity user: userService.getAll())
            {
                users.add(new UserListDTO(user.getUsername(),user.getEmail(), user.getPhone(), user.getUserType().toString()));
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception ue) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("List users does not work. " + ue.getMessage());
        }
    }

    @PostMapping("/save")
    public @ResponseBody ResponseEntity<String> saveUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserSaveDTO userDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFormat;
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }

            UserType role = UserType.valueOf(userDTO.getUserType());
            UserEntity user = new UserEntity(0,userDTO.getUsername(),null,  userDTO.getPhone(), userDTO.getEmail(),role, null );
            userService.add(user);
            jsonFormat = objectMapper.writeValueAsString("user created");
            return new ResponseEntity<>(jsonFormat, HttpStatus.CREATED);
        } catch (Exception ue) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Save user does not work. " + ue.getMessage());
        }
    }

    @DeleteMapping("/delete/{username}")
    public @ResponseBody ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFormat;
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            UserEntity user = userService.getByUsername(username);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Username not found");
            }
            userService.delete(user);
            jsonFormat = objectMapper.writeValueAsString(username);
            return new ResponseEntity<>(jsonFormat, HttpStatus.OK);
        } catch (Exception ue) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete user does not work. " + ue.getMessage());
        }
    }

    @PutMapping("/modify")
    public @ResponseBody ResponseEntity<?> modifyUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserListDTO userDTO){
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            UserEntity user = userService.getByUsername(userDTO.getUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Username not found");
            }
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setUserType(UserType.valueOf(userDTO.getUserType()));
            userService.modify(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Modify user does not work. " + e.getMessage());
        }
    }

    @PutMapping("/modifymydata")
    public @ResponseBody ResponseEntity<?> modifyUserOwnData(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserModifyDTO userDTO){
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            // Check if the token is already invalidated
            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalid");
            }
            UserEntity user = userService.modifyOwnData(userDTO);
           // UserDataDTO userToSend = new UserDataDTO(user.getUsername(),user.getEmail(),user.getPhone(),user.getUserType().toString());
            return new ResponseEntity<>(jwtService.generateToken(user.getUsername()), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Modify own user data does not work. " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

            if (jwtService.isTokenInvalid(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token already invalidated");
            }

            jwtService.invalidateToken(token);
            return ResponseEntity.ok("Logout successful");
        } catch (ExpiredJwtException e) {
            // Log the exception and treat as a successful logout
            return ResponseEntity.ok("Token expired but logout processed");
        }
    }
}

package project.service.impl;

import project.DTO.UserModifyDTO;
import project.email.EmailSender;
import project.entities.UserEntity;
import project.entities.UserType;
import project.repositories.UserRepository;
import project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static String generatePassword() {
        String charLower = "abcdefghijklmnopqrstuvwxyz";
        String charUpper = charLower.toUpperCase();
        String digit = "0123456789";
        String specialChar = "!@#$%^&*()-_=+[]{}|;:'\",.<>?";

        String passwordChars = charLower + charUpper + digit + specialChar;

        StringBuilder password = new StringBuilder();
        password.append(getRandomChar(charLower));
        password.append(getRandomChar(charUpper));
        password.append(getRandomChar(digit));
        password.append(getRandomChar(specialChar));

        for (int i = 4; i < 8; i++) {
            password.append(getRandomChar(passwordChars));
        }

        // Shuffle the characters in the password to make it more random
        return shuffleString(password.toString());
    }

    private static char getRandomChar(String source) {
        int randomIndex = new SecureRandom().nextInt(source.length());
        return source.charAt(randomIndex);
    }

    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int index = new SecureRandom().nextInt(i + 1);
            char temp = chars[index];
            chars[index] = chars[i];
            chars[i] = temp;
        }
        return new String(chars);
    }

    public void add(UserEntity user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
            String password = generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            if (user.isValidData(user.getPhone(), user.getEmail())) {
                try {
                    userRepository.save(user);
                    String to = user.getEmail();
                    String subject = "Parking Management Credentials";
                    String body = "username: "+ user.getUsername() +"\n password: "+ password;
                    EmailSender.sendEmail(to, subject, body);
                } catch (Exception e) {
                    throw new Exception("Credentials already used");
                }
            } else {
                throw new Exception("Invalid credeantials.");
            }
    }

    public UserEntity getByUsername(String username) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameC = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(usernameC).get();
        if(userCurrent.getUserType().equals(UserType.ADMIN) || userCurrent.getUsername().equals(username)) {
             return userRepository.getByUsername(username);
        }
        throw new Exception("Not allowed.");
    }


    public void delete(UserEntity user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
            userRepository.delete(user);
    }

    public List<UserEntity> getAll() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
            return userRepository.findAll();

    }
    public void modify(UserEntity user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(username).get();
        if(!userCurrent.getUserType().equals(UserType.ADMIN)) {
            throw new Exception("Not allowed.");
        }
            userRepository.save(user);
    }

    private boolean validatePhone(String phone){
        if(phone.matches("\\d+") && phone.length() == 10)
            return true;
        return false;
    }

    private boolean validateEmail(String email){
        if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            return true;
        return false;
    }

    private boolean validatePassword(String pass){
        if(pass.length()>=8 && pass.matches("^.*[a-z].*$") && pass.matches("^.*[A-Z].*$") && pass.matches("^.*[0-9].*$") && pass.matches("^(.*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>?]).*$"))
            return true;
        return false;
    }
    public UserEntity modifyOwnData(UserModifyDTO user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userCurrent = userRepository.findByUsername(username).get();
        UserEntity newUser = userCurrent;
        newUser.setId(userCurrent.getId());
        if(!user.getUsername().equals(userCurrent.getUsername()) && !user.getUsername().equals("")){
            newUser.setUsername(user.getUsername());
        }
        if(!user.getPhone().equals(userCurrent.getPhone()) && !user.getPhone().equals("") && validatePhone(user.getPhone())){
            newUser.setPhone(user.getPhone());
        }
        if(!user.getEmail().equals(userCurrent.getEmail()) && !user.getEmail().equals("") && validateEmail(user.getEmail())) {
            newUser.setEmail(user.getEmail());
        }
        if(user.getNewPassword().equals(user.getConfirmNewPassword()) && validatePassword(user.getNewPassword())){
            newUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
        }
        return userRepository.save(newUser);
    }

    public UserType getUserType(String username) throws Exception {
        try {
            UserEntity user = getByUsername(username);
            return user.getUserType();
        }catch (Exception e) {
            throw new Exception("User not found");
        }
    }
}

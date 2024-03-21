package project.service;

import project.DTO.UserModifyDTO;
import project.entities.UserEntity;
import project.entities.UserType;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void add(UserEntity user) throws Exception;
    UserEntity getByUsername(String username) throws Exception;
    void modify(UserEntity user) throws Exception;
    void delete(UserEntity user) throws Exception;
    List<UserEntity> getAll() throws Exception;
    UserType getUserType(String username) throws Exception;
    UserEntity modifyOwnData(UserModifyDTO user);
}

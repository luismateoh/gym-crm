package com.luismateoh.gymcrm.service;

import java.util.Optional;

import com.luismateoh.gymcrm.dao.UserDao;
import com.luismateoh.gymcrm.domain.User;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDTO registerUser(String firstName, String lastName, String username, String password, boolean isActive) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(isActive);

        User savedUser = userDao.saveOrUpdate(user);
        return userMapper.userToUserDTO(savedUser);
    }

    public Optional<UserDTO> login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(userMapper.userToUserDTO(user));
        }
        return Optional.empty();
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String username = baseUsername;
        int counter = 1;
        while (userDao.findByUsername(username) != null) {
            username = baseUsername + counter;
            counter++;
        }
        return username;
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public void changePassword(UserDTO user, String newPassword) {
        user.setPassword(newPassword);
        userDao.saveOrUpdate(userMapper.userDTOToUser(user));
    }

    public void setActiveStatus(String username, boolean isActive) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            user.setIsActive(isActive);
            userDao.saveOrUpdate(user);
        }
    }

    public Optional<UserDTO> findByUsername(String username) {
        User user = userDao.findByUsername(username);
        return user != null ? Optional.of(userMapper.userToUserDTO(user)) : Optional.empty();
    }

    public boolean validatePassword(UserDTO user, String password) {
        return user.getPassword().equals(password);
    }
}

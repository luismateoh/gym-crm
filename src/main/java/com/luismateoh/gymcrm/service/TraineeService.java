package com.luismateoh.gymcrm.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.luismateoh.gymcrm.dao.TraineeDao;
import com.luismateoh.gymcrm.dao.UserDao;
import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.domain.User;
import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.mapper.TraineeMapper;
import com.luismateoh.gymcrm.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {
    private final TraineeDao traineeDao;
    private final UserDao userDao;
    private static final TraineeMapper traineeMapper = TraineeMapper.INSTANCE;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public TraineeService(TraineeDao traineeDao, UserDao userDao) {
        this.traineeDao = traineeDao;
        this.userDao = userDao;
    }

    public TraineeDTO createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        UserService userService = new UserService(userDao);
        String username = userService.generateUsername(firstName, lastName);
        String password = userService.generatePassword();
        UserDTO userDTO = userService.registerUser(firstName, lastName, username, password, true);

        Trainee trainee = new Trainee();
        trainee.setUser(userMapper.userDTOToUser(userDTO));
        trainee.setDateOfBirth(LocalDate.parse(dateOfBirth));
        trainee.setAddress(address);

        Trainee savedTrainee = traineeDao.saveOrUpdate(trainee);
        return traineeMapper.traineeToTraineeDTO(savedTrainee);
    }

    public TraineeDTO findTraineeByUsername(String username) {
        Trainee trainee = traineeDao.findByUsername(username);
        return traineeMapper.traineeToTraineeDTO(trainee);
    }

    public void updateTraineeProfile(TraineeDTO traineeDTO) {
        Trainee trainee = traineeMapper.traineeDTOToTrainee(traineeDTO);
        traineeDao.saveOrUpdate(trainee);
    }

    public void activateDeactivateTrainee(TraineeDTO traineeDTO, boolean isActive) {
        UserDTO userDTO = traineeDTO.getUser();
        userDTO.setIsActive(isActive);
        User user = userMapper.userDTOToUser(userDTO);
        userDao.saveOrUpdate(user);
    }

    public void changePassword(UserDTO userDTO, String newPassword) {
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(newPassword);
        userDao.saveOrUpdate(user);
    }

    public void deleteTraineeByUsername(String username) {
        Trainee trainee = traineeDao.findByUsername(username);
        traineeDao.delete(trainee);
    }

    public List<TraineeDTO> findAllTrainees() {
        return traineeDao.findAll().stream().map(traineeMapper::traineeToTraineeDTO).collect(Collectors.toList());
    }
}

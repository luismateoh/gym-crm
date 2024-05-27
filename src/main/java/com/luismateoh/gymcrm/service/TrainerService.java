package com.luismateoh.gymcrm.service;

import java.util.List;

import com.luismateoh.gymcrm.dao.TrainerDao;
import com.luismateoh.gymcrm.dao.UserDao;
import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.domain.TrainingType;
import com.luismateoh.gymcrm.domain.User;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.mapper.TrainerMapper;
import com.luismateoh.gymcrm.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final TrainingTypeService trainingTypeService;
    private static final TrainerMapper trainerMapper = TrainerMapper.INSTANCE;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public TrainerService(TrainerDao trainerDao, UserDao userDao, TrainingTypeService trainingTypeService) {
        this.trainerDao = trainerDao;
        this.userDao = userDao;
        this.trainingTypeService = trainingTypeService;
    }

    public TrainerDTO createTrainer(String firstName, String lastName, String specialization) {
        UserService userService = new UserService(userDao);
        String username = userService.generateUsername(firstName, lastName);
        String password = userService.generatePassword();
        UserDTO userDTO = userService.registerUser(firstName, lastName, username, password, true);

        TrainingType trainingType = trainingTypeService.findByName(specialization)
                .orElseThrow(() -> new IllegalArgumentException("Invalid specialization: " + specialization));

        Trainer trainer = new Trainer();
        trainer.setUser(userMapper.userDTOToUser(userDTO));
        trainer.setSpecialization(trainingType);

        Trainer savedTrainer = trainerDao.saveOrUpdate(trainer);
        return trainerMapper.trainerToTrainerDTO(savedTrainer);
    }

    public TrainerDTO findTrainerByUsername(String username) {
        Trainer trainer = trainerDao.findByUsername(username);
        return trainerMapper.trainerToTrainerDTO(trainer);
    }

    public void updateTrainerProfile(TrainerDTO trainerDTO) {
        Trainer trainer = trainerMapper.trainerDTOToTrainer(trainerDTO);

        // Fetch the TrainingType from the database
        String normalizedSpecialization = trainerDTO.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeService.findByName(normalizedSpecialization)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid specialization: " + trainerDTO.getSpecialization().getTrainingTypeName()));

        trainer.setSpecialization(trainingType);
        trainerDao.saveOrUpdate(trainer);
    }

    public void activateDeactivateTrainer(TrainerDTO trainerDTO, boolean isActive) {
        UserDTO userDTO = trainerDTO.getUser();
        userDTO.setIsActive(isActive);
        User user = userMapper.userDTOToUser(userDTO);
        userDao.saveOrUpdate(user);
    }

    public void changePassword(UserDTO userDTO, String newPassword) {
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(newPassword);
        userDao.saveOrUpdate(user);
    }

    public List<TrainerDTO> findAllTrainers() {
        return trainerDao.findAll().stream().map(trainerMapper::trainerToTrainerDTO).toList();
    }
}

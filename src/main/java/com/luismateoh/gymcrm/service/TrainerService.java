package com.luismateoh.gymcrm.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.luismateoh.gymcrm.dao.TraineeDao;
import com.luismateoh.gymcrm.dao.TrainerDao;
import com.luismateoh.gymcrm.dao.UserDao;
import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.domain.TrainingType;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.mapper.TrainerMapper;
import com.luismateoh.gymcrm.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final TraineeDao traineeDao;
    private final TrainingTypeService trainingTypeService;
    private static final TrainerMapper trainerMapper = TrainerMapper.INSTANCE;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public TrainerService(TrainerDao trainerDao, UserDao userDao, TraineeDao traineeDao,
                          TrainingTypeService trainingTypeService) {
        this.trainerDao = trainerDao;
        this.userDao = userDao;
        this.traineeDao = traineeDao;
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

        String normalizedSpecialization = trainerDTO.getSpecialization().getTrainingTypeName();
        TrainingType trainingType = trainingTypeService.findByName(normalizedSpecialization)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid specialization: " + trainerDTO.getSpecialization().getTrainingTypeName()));

        trainer.setSpecialization(trainingType);
        trainerDao.saveOrUpdate(trainer);
    }

    public List<TrainerDTO> findAllTrainers() {
        return trainerDao.findAll().stream().map(trainerMapper::trainerToTrainerDTO).toList();
    }

    public List<TrainerDTO> getUnassignedTrainers(String traineeUsername) {
        Trainee trainee = traineeDao.findByUsername(traineeUsername);
        Set<Trainer> assignedTrainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerDao.findAll();

        return allTrainers.stream()
                .filter(trainer -> !assignedTrainers.contains(trainer))
                .map(trainerMapper::trainerToTrainerDTO)
                .toList();
    }

    public void updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = traineeDao.findByUsername(traineeUsername);
        Set<Trainer> trainers = trainerUsernames.stream().map(trainerDao::findByUsername).collect(Collectors.toSet());
        trainee.setTrainers(trainers);
        traineeDao.saveOrUpdate(trainee);
    }
}

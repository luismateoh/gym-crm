package com.luismateoh.gymcrm.service;

import static com.luismateoh.gymcrm.utils.Utils.generatePassword;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import com.luismateoh.gymcrm.dao.TraineeDAO;
import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.utils.UsernameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDAO traineeDAO;
    private final UsernameGenerator usernameGenerator;

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO, UsernameGenerator usernameGenerator) {
        this.traineeDAO = traineeDAO;
        this.usernameGenerator = usernameGenerator;
    }

    @Override
    public Trainee createTrainee(TraineeDTO traineeDTO) {

        log.debug(String.format("Creating trainee with name %s %s", traineeDTO.firstName(), traineeDTO.lastName()));
        Trainee trainee = new Trainee();
        trainee.setFirstName(traineeDTO.firstName());
        trainee.setLastName(traineeDTO.lastName());
        trainee.setDateOfBirth(LocalDate.parse(traineeDTO.dateOfBirth()));
        trainee.setAddress(traineeDTO.address());

        trainee.setIsActive(true);
        trainee.setUserName(usernameGenerator.generateUsername(traineeDTO.firstName(), traineeDTO.lastName()));
        trainee.setPassword(generatePassword());

        Trainee savedTrainee = traineeDAO.save(trainee);
        log.info(String.format("Trainee with name %s %s created", traineeDTO.firstName(), traineeDTO.lastName()));

        return savedTrainee;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {

        log.debug(String.format("Updating trainee with id %d", trainee.getId()));
        Trainee updateTrainee = traineeDAO.findById(trainee.getId());
        // If the trainee exists, update the trainee
        // Password and username are not updated
        if (updateTrainee == null) {
            log.error(String.format("Trainee with id %d does not exist", trainee.getId()));
            throw new NoSuchElementException(String.format("Trainee with id %d does not exist", trainee.getId()));
        }
        updateTrainee.setFirstName(trainee.getFirstName());
        updateTrainee.setLastName(trainee.getLastName());
        updateTrainee.setDateOfBirth(trainee.getDateOfBirth());
        updateTrainee.setAddress(trainee.getAddress());
        updateTrainee.setTraining(trainee.getTraining());
        updateTrainee.setIsActive(trainee.getIsActive());

        Trainee savedTrainee = traineeDAO.save(updateTrainee);
        log.info(String.format("Trainee with id %d updated", trainee.getId()));
        return savedTrainee;

    }

    @Override
    public void deleteTrainee(Long traineeId) {
        log.debug(String.format("Deleting trainee with id %d", traineeId));
        traineeDAO.delete(traineeId);
        log.info(String.format("Trainee with id %d deleted", traineeId));
    }

    @Override
    public Trainee findTraineeById(Long traineeId) {
        log.debug(String.format("Finding trainee with id %d", traineeId));
        Trainee trainee = traineeDAO.findById(traineeId);
        if (trainee == null) {
            log.error(String.format("Trainee with id %d does not exist", traineeId));
            throw new NoSuchElementException(String.format("Trainee with id %d does not exist", traineeId));
        }
        log.info(String.format("Trainee with id %d found", traineeId));
        return trainee;
    }
}


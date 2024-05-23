package com.luismateoh.gymcrm.service;

import static com.luismateoh.gymcrm.utils.Utils.generatePassword;

import java.util.NoSuchElementException;

import com.luismateoh.gymcrm.dao.TrainerDAO;
import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.utils.UsernameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDAO trainerDAO;
    private final UsernameGenerator usernameGenerator;

    @Autowired
    public TrainerServiceImpl(TrainerDAO trainerDAO, UsernameGenerator usernameGenerator) {
        this.trainerDAO = trainerDAO;
        this.usernameGenerator = usernameGenerator;
    }

    @Override
    public Trainer createTrainer(TrainerDTO trainerDTO) {

        log.debug(String.format("Creating trainer with name %s %s", trainerDTO.firstName(), trainerDTO.lastName()));
        Trainer trainer = new Trainer();

        trainer.setFirstName(trainerDTO.firstName());
        trainer.setLastName(trainerDTO.lastName());
        //By default, a new trainer is active
        trainer.setIsActive(true);
        trainer.setUserName(usernameGenerator.generateUsername(trainerDTO.firstName(), trainerDTO.lastName()));
        trainer.setPassword(generatePassword());

        Trainer savedTrainer = trainerDAO.save(trainer);
        log.info(String.format("Trainer with name %s %s created", trainerDTO.firstName(), trainerDTO.lastName()));

        return savedTrainer;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        log.debug(String.format("Updating trainer with id %d", trainer.getId()));
        Trainer updateTrainer = trainerDAO.findById(trainer.getId());
        if (updateTrainer == null) {
            log.error(String.format("Trainer with id %d does not exist", trainer.getId()));
            throw new NoSuchElementException(String.format("Trainer with id %d does not exist", trainer.getId()));
        }
        updateTrainer.setFirstName(trainer.getFirstName());
        updateTrainer.setLastName(trainer.getLastName());
        updateTrainer.setTraining(trainer.getTraining());
        updateTrainer.setIsActive(trainer.getIsActive());
        Trainer savedTrainer = trainerDAO.save(updateTrainer);
        log.info(String.format("Trainer with id %d updated", trainer.getId()));
        return savedTrainer;
    }

    @Override
    public Trainer findTrainerById(Long trainerId) {
        log.debug(String.format("Finding trainer with id %d", trainerId));
        Trainer trainer = trainerDAO.findById(trainerId);
        if (trainer == null) {
            log.error(String.format("Trainer with id %d does not exist", trainerId));
            throw new NoSuchElementException(String.format("Trainer with id %d does not exist", trainerId));
        }
        log.info(String.format("Trainer with id %d found", trainerId));
        return trainer;
    }
}

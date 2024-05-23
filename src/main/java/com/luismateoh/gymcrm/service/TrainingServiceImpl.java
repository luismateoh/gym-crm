package com.luismateoh.gymcrm.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import com.luismateoh.gymcrm.dao.TrainingDAO;
import com.luismateoh.gymcrm.domain.Training;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingServiceImpl(TrainingDAO trainingDAO, TrainingTypeService trainingTypeService) {
        this.trainingDAO = trainingDAO;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    public Training createTraining(TrainingDTO trainingDTO) {

        log.debug(String.format("Creating training with name %s", trainingDTO.trainingName()));
        Training training = new Training();
        training.setTrainingName(trainingDTO.trainingName());
        training.setTrainingType(trainingDTO.trainingTypeId() != null ? trainingTypeService.getOrCreateTrainingType(
                trainingDTO.trainingTypeId()) : null);
        training.setTrainingDuration(trainingDTO.trainingDuration());
        training.setTrainingDate(LocalDate.parse(trainingDTO.trainingDate()));
        Training savedTraining = trainingDAO.save(training);
        log.info(String.format("Training with name %s created", trainingDTO.trainingName()));
        return savedTraining;
    }

    @Override
    public Training findTrainingById(Long id) {
        log.debug(String.format("Finding training with id %d", id));
        Training training = trainingDAO.findById(id);
        if (training == null) {
            log.error(String.format("Training with id %d does not exist", id));
            throw new NoSuchElementException(String.format("Training with id %d does not exist", id));
        }
        log.info(String.format("Training with id %d found", id));
        return training;
    }
}

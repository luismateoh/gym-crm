package com.luismateoh.gymcrm.facade;

import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.domain.Training;
import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.service.TraineeService;
import com.luismateoh.gymcrm.service.TrainerService;
import com.luismateoh.gymcrm.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CRMFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public CRMFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    // Facade methods that delegate to the services

    /*
     * Trainee Service class should support possibility to create/update/delete/select Trainee profile.
     * Trainer Service class should support possibility to create/update/select Trainer profile.
     * Training Service class should support possibility to create/select Training profile.
     */

    // Trainee Service methods
    public Trainee createTrainee(TraineeDTO traineeDTO) {
        return traineeService.createTrainee(traineeDTO);
    }

    public Trainee updateTrainee(Trainee traineeDTO) {
        return traineeService.updateTrainee(traineeDTO);
    }

    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    public Trainee selectTrainee(Long id) {
        return traineeService.findTraineeById(id);
    }

    // Trainer Service methods
    public Trainer createTrainer(TrainerDTO trainerDTO) {
        return trainerService.createTrainer(trainerDTO);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    public Trainer selectTrainer(Long id) {
        return trainerService.findTrainerById(id);
    }

    // Training Service methods
    public Training createTraining(TrainingDTO trainingDTO) {
        return trainingService.createTraining(trainingDTO);
    }

    public Training selectTraining(Long id) {
        return trainingService.findTrainingById(id);
    }

}

package com.luismateoh.gymcrm.service;

import com.luismateoh.gymcrm.domain.Training;
import com.luismateoh.gymcrm.dto.TrainingDTO;

/**
 * TrainingService
 * Training Service class should support possibility to create/select Training profile.
 */
public interface TrainingService {

    /**
     * Create a new Training profile.
     *
     * @param training Training object to be created.
     * @return Training object created.
     */
    Training createTraining(TrainingDTO training);

    /**
     * Find a Training profile by its id.
     *
     * @param id Training id.
     * @return Training object found.
     */
    Training findTrainingById(Long id);
}

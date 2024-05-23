package com.luismateoh.gymcrm.service;

import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.dto.TrainerDTO;

/**
 * TrainerService
 * Trainer Service class should support possibility to create/update/select Trainer profile.
 */
public interface TrainerService {

    /**
     * Create a new Trainer profile.
     *
     * @param trainerDTO Trainer object to be created.
     * @return Trainer object created.
     */
    Trainer createTrainer(TrainerDTO trainerDTO);

    /**
     * Update an existing Trainer profile.
     *
     * @param trainer Trainer object to be updated.
     * @return Trainer object updated.
     */
    Trainer updateTrainer(Trainer trainer);

    /**
     * Find a Trainer profile by its id.
     *
     * @param trainerId Trainer id.
     * @return Trainer object found.
     */
    Trainer findTrainerById(Long trainerId);
}

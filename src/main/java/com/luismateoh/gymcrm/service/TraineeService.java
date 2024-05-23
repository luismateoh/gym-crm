package com.luismateoh.gymcrm.service;

import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.dto.TraineeDTO;

/**
 * TraineeService
 * Trainee Service class should support possibility to create/update/delete/select Trainee profile.
 */
public interface TraineeService {

    /**
     * Create a new Trainee profile.
     *
     * @param traineeDTO TraineeDTO object to be created.
     * @return Trainee object created.
     */
    Trainee createTrainee(TraineeDTO traineeDTO);

    /**
     * Update an existing Trainee profile.
     *
     * @param traineeDTO Trainee object to be updated.
     * @return Trainee object updated.
     */
    Trainee updateTrainee(Trainee traineeDTO);

    /**
     * Delete an existing Trainee profile.
     *
     * @param traineeId Trainee object to be deleted.
     */
    void deleteTrainee(Long traineeId);

    /**
     * Find a Trainee profile by its id.
     *
     * @param traineeId Trainee id.
     * @return Trainee object found.
     */
    Trainee findTraineeById(Long traineeId);
}

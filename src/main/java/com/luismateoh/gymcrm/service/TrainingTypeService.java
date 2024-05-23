package com.luismateoh.gymcrm.service;

import com.luismateoh.gymcrm.domain.TrainingType;

/**
 * Service for TrainingType should support possibility to create/select TrainingType profile.
 */
public interface TrainingTypeService {

    /**
     * Create a new TrainingType profile.
     *
     * @param typeName TrainingType name.
     * @return TrainingType object created.
     */
    TrainingType getOrCreateTrainingType(String typeName);
}

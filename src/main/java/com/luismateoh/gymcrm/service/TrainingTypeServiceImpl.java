package com.luismateoh.gymcrm.service;

import com.luismateoh.gymcrm.dao.TrainingTypeDAO;
import com.luismateoh.gymcrm.domain.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    @Override
    public TrainingType getOrCreateTrainingType(String typeName) {
        log.debug(String.format("Getting or creating training type with name %s", typeName));
        TrainingType trainingType = trainingTypeDAO.findByNameIgnoreCase(typeName);
        if (trainingType == null) {
            trainingType = new TrainingType();
            trainingType.setName(typeName);
            trainingType = trainingTypeDAO.save(trainingType);
            log.info(String.format("Training type with name %s created", typeName));
        }
        log.info(String.format("Training type with name %s found", typeName));
        return trainingType;
    }
}

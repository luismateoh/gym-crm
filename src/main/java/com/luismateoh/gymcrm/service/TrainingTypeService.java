package com.luismateoh.gymcrm.service;

import java.util.List;
import java.util.Optional;

import com.luismateoh.gymcrm.dao.TrainingTypeDao;
import com.luismateoh.gymcrm.domain.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Autowired
    public TrainingTypeService(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    public Optional<TrainingType> findByName(String name) {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();
        return trainingTypes.stream()
                .filter(trainingType -> trainingType.getTrainingTypeName().equalsIgnoreCase(name))
                .findFirst();
    }
}

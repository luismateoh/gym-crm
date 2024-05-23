package com.luismateoh.gymcrm.dao;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.luismateoh.gymcrm.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDAO {

    //support possibility to create/select
    AtomicLong serialNumber = new AtomicLong(1);

    @Autowired
    private Map<Long, Training> trainingStorage;

    public Training save(Training training) {
        training.setId(serialNumber.getAndIncrement());
        trainingStorage.put(serialNumber.get(), training);
        return training;

    }

    public Training findById(Long id) {
        return trainingStorage.get(id);
    }

    public Training findTrainingTypeById(Long id) {
        return trainingStorage.get(id);
    }
}

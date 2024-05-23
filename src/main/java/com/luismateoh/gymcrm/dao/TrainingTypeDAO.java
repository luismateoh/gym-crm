package com.luismateoh.gymcrm.dao;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.luismateoh.gymcrm.domain.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDAO {

    AtomicLong serialNumber = new AtomicLong(1);

    @Autowired
    private Map<Long, TrainingType> trainingTypeStorage;

    public TrainingType save(TrainingType trainingType) {
        trainingType.setId(serialNumber.getAndIncrement());
        trainingTypeStorage.put(serialNumber.get(), trainingType);
        return trainingType;
    }

    public TrainingType findByNameIgnoreCase(String name) {
        return trainingTypeStorage.values()
                .stream()
                .filter(trainingType -> trainingType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
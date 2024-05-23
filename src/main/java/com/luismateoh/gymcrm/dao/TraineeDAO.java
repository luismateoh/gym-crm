package com.luismateoh.gymcrm.dao;

import java.util.Map;

import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.utils.SerialNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDAO {

    //support possibility to create/update/delete/select
    @Autowired
    private Map<Long, Trainee> traineeStorage;

    @Autowired
    private SerialNumberGenerator serialNumberGenerator;

    public Trainee save(Trainee trainee) {
        trainee.setId(serialNumberGenerator.getNextSerialNumber());
        traineeStorage.put(serialNumberGenerator.getCurrentSerialNumber(), trainee);
        return trainee;
    }

    public Trainee findById(Long id) {
        return traineeStorage.get(id);
    }

    public void delete(Long id) {
        traineeStorage.remove(id);
    }

    //verify if username exists
    public boolean exists(String username) {
        return traineeStorage.values().stream().anyMatch(trainee -> trainee.getUserName().equals(username));
    }
}

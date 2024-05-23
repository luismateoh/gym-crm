package com.luismateoh.gymcrm.dao;

import java.util.Map;
import java.util.Optional;

import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.utils.SerialNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAO {

    //support possibility to create/update/select
    @Autowired
    private Map<Long, Trainer> trainerStorage;

    @Autowired
    private SerialNumberGenerator serialNumberGenerator;

    public Trainer save(Trainer trainer) {
        trainer.setId(serialNumberGenerator.getNextSerialNumber());
        trainerStorage.put(serialNumberGenerator.getCurrentSerialNumber(), trainer);
        return trainer;
    }

    public Trainer findById(Long trainerId) {
        return trainerStorage.get(trainerId);
    }

    public Optional<Optional<Trainer>> findByUserName(String userName) {

        return Optional.of(
                trainerStorage.values().stream().filter(trainer -> trainer.getUserName().equals(userName)).findFirst());
    }

    //verify if username exists
    public boolean exists(String username) {
        return trainerStorage.values().stream().anyMatch(trainer -> trainer.getUserName().equals(username));
    }
}

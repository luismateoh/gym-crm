package com.luismateoh.gymcrm.utils;

import com.luismateoh.gymcrm.dao.TraineeDAO;
import com.luismateoh.gymcrm.dao.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private SerialNumberGenerator serialNumberGenerator;

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String username = baseUsername;

        while (traineeDAO.exists(username) || trainerDAO.exists(username)) {
            username = baseUsername + (serialNumberGenerator.getCurrentSerialNumber() + 1);
        }

        return username;
    }
}

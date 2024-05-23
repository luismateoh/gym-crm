package com.luismateoh.gymcrm.configuration;

import static com.luismateoh.gymcrm.utils.Utils.generatePassword;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luismateoh.gymcrm.domain.*;
import com.luismateoh.gymcrm.utils.UsernameGenerator;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class DataInitialization {

    @Autowired
    private StorageConfig storageConfig;
    @Value("${app.initialDataFile}")
    private String dataPath;
    @Autowired
    private UsernameGenerator usernameGenerator;

    public void loadData() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        TypeReference<List<Trainee>> traineeType = new TypeReference<>() {
        };
        TypeReference<List<Trainer>> trainerType = new TypeReference<>() {
        };
        TypeReference<List<Training>> trainingType = new TypeReference<>() {
        };

        try {

            InputStream inputStream = TypeReference.class.getResourceAsStream("/" + dataPath);
            log.info("Loading data from: {}", dataPath);
            if (inputStream == null) {
                log.error("Unable to find file: {}", dataPath);
                return;
            }
            Map<String, Object> map = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {
            });

            List<Trainee> traineeList = mapper.convertValue(map.get("trainees"), traineeType);
            List<Trainer> trainerList = mapper.convertValue(map.get("trainers"), trainerType);
            List<Training> trainingList = mapper.convertValue(map.get("trainings"), trainingType);

            for (Trainee trainee : traineeList) {
                trainee.setUserName(usernameGenerator.generateUsername(trainee.getFirstName(), trainee.getLastName()));
                trainee.setPassword(generatePassword());
                storageConfig.traineeStorage().put(trainee.getId(), trainee);
            }
            log.info("Trainees: {}", traineeList.size());

            for (Trainer trainer : trainerList) {
                trainer.setUserName(usernameGenerator.generateUsername(trainer.getFirstName(), trainer.getLastName()));
                trainer.setPassword(generatePassword());
                storageConfig.trainerStorage().put(trainer.getId(), trainer);
            }
            log.info("Trainers: {}", trainerList.size());

            for (Training training : trainingList) {
                storageConfig.trainingStorage().put(training.getId(), training);
            }
            log.info("Trainings: {}", trainingList.size());

            for (Training training : trainingList) {
                storageConfig.trainingStorage().put(training.getId(), training);

                TrainingType trainingTypeType = training.getTrainingType();
                if (trainingTypeType != null) {
                    storageConfig.trainingTypeStorage().put(trainingTypeType.getId(), trainingTypeType);
                }
            }
            log.info("Trainings: {}", trainingList.size());
            log.info("Training Types: {}", storageConfig.trainingTypeStorage().size());


            log.info("Data loaded successfully");

        } catch (IOException e) {

            log.warn("Unable to load data: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void init() {

        log.info("Loading initial data");

        log.info("Data path: {}", dataPath);
        // Load initial data from file if necessary
        loadData();
    }

}
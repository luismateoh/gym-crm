package com.luismateoh.gymcrm.ui;

import static com.luismateoh.gymcrm.utils.ValidatorUtil.validate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.TrainingTypeDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.service.TraineeService;
import com.luismateoh.gymcrm.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "userInteraction")
@Component
public class RegisterMenu extends ConsoleUI {

    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public RegisterMenu(TrainerService trainerService, TraineeService traineeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    @Override
    public void start(String username) {
        while (true) {
            displayMenu();
            int choice = Integer.parseInt(getInput("Choose an option:", input -> validateMenuOption(input, 1, 3)));

            switch (choice) {
                case 1 -> {
                    registerTrainer();
                    return;
                }
                case 2 -> {
                    registerTrainee();
                    return;
                }
                case 3 -> {
                    log.info("Exiting...");
                    return;
                }
                default -> log.info("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        String menu = """
                Register as:
                1. Trainer
                2. Trainee
                3. Exit
                """;
        log.info(menu);
    }

    private void registerTrainer() {
        String firstName = getInput("Enter first name:", input -> !input.trim().isEmpty());
        String lastName = getInput("Enter last name:", input -> !input.trim().isEmpty());
        String specialization = getInput("Enter specialization (fitness, yoga, zumba, stretching, resistance):", input -> !input.trim().isEmpty());

        try {
            UserDTO user = UserDTO.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .username("username")
                    .password("password")
                    .isActive(true)
                    .build();

            TrainerDTO trainer = TrainerDTO.builder()
                    .user(user)
                    .specialization(TrainingTypeDTO.builder().trainingTypeName(specialization).build())
                    .build();

            validate(user);
            validate(trainer);

            trainer = trainerService.createTrainer(firstName, lastName, specialization);
            log.info("Trainer registered successfully.");
            log.info(trainer.toString());
            log.info(String.format("Password: %s", trainer.getUser().getPassword()));
        } catch (IllegalArgumentException e) {
            log.error("Error registering trainer: {}", e.getMessage());
        }
    }

    private void registerTrainee() {
        String firstName = getInput("Enter first name:", input -> !input.trim().isEmpty());
        String lastName = getInput("Enter last name:", input -> !input.trim().isEmpty());
        String dateOfBirth = getInput("Enter date of birth (yyyy-mm-dd):", this::validateDate);
        String address = getInput("Enter address:", input -> !input.trim().isEmpty());

        try {
            UserDTO user = UserDTO.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .username("username")
                    .password("password")
                    .isActive(true)
                    .build();

            TraineeDTO trainee = TraineeDTO.builder()
                    .user(user)
                    .dateOfBirth(LocalDate.parse(dateOfBirth))
                    .address(address)
                    .build();

            validate(user);
            validate(trainee);

            trainee = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
            log.info("Trainee registered successfully.");
            log.info(trainee.toString());
            log.info(String.format("Password: %s", trainee.getUser().getPassword()));
        } catch (IllegalArgumentException e) {
            log.error("Error registering trainee: {}", e.getMessage());
        }
    }

    private boolean validateDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", date);
            return false;
        }
    }
}

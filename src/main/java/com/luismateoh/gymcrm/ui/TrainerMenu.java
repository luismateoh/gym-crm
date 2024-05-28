package com.luismateoh.gymcrm.ui;

import static com.luismateoh.gymcrm.utils.ValidatorUtil.validate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.dto.TrainingTypeDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.service.TraineeService;
import com.luismateoh.gymcrm.service.TrainerService;
import com.luismateoh.gymcrm.service.TrainingService;
import com.luismateoh.gymcrm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "userInteraction")
@Component
public class TrainerMenu extends ConsoleUI {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final UserService userService;

    public TrainerMenu(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService,
                       UserService userService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    @Override
    public void start(String username) {
        TrainerDTO trainer = trainerService.findTrainerByUsername(username);
        boolean keepLoggedIn = true;

        while (keepLoggedIn) {
            displayMenu(username);
            int choice = Integer.parseInt(getInput("Choose an option:", input -> validateMenuOption(input, 1, 9)));

            switch (choice) {
                case 1 -> viewProfile(trainer);
                case 2 -> updateProfile(trainer);
                case 3 -> changePassword(trainer.getUser());
                case 4 -> viewAssignedTrainees();
                case 5 -> addTrainingSession(trainer);
                case 6 -> listOwnTrainings(username);
                case 7 -> listTrainings(username);
                case 8 -> keepLoggedIn = !deactivateUser(username);
                case 9 -> {
                    log.info("Logging out...");
                    return;
                }
                default -> log.info("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu(String username) {
        String menu = String.format("""
                                
                User %s
                Trainer Menu:
                1. View profile
                2. Update profile
                3. Change password
                4. View assigned trainees
                5. Add training session
                6. List own trainings
                7. List all trainings
                8. Deactivate account
                9. Logout
                """, username);
        log.info(menu);
    }

    private void viewProfile(TrainerDTO trainer) {
        log.info(trainer.toString());
        log.info(String.format("Password: %s", trainer.getUser().getPassword()));
    }

    private void updateProfile(TrainerDTO trainer) {
        String specialization = getInput("Enter new specialization (fitness, yoga, Zumba, stretching, resistance):",
                input -> !input.trim().isEmpty());
        TrainingTypeDTO trainingType = TrainingTypeDTO.builder().trainingTypeName(specialization).build();
        trainer.setSpecialization(trainingType);
        try {
            validate(trainer);
            trainerService.updateTrainerProfile(trainer);
            log.info("Profile updated successfully.");
        } catch (IllegalArgumentException e) {
            log.error("Error updating profile: {}", e.getMessage());
        }
    }

    private void changePassword(UserDTO user) {
        String newPassword = getInput("Enter new password:", input -> !input.trim().isEmpty());
        userService.changePassword(user, newPassword);
        log.info("Password changed successfully.");
    }

    private void viewAssignedTrainees() {
        List<TraineeDTO> trainees = traineeService.findAllTrainees();
        if (trainees.isEmpty()) {
            log.info("No trainees found.");
        } else {
            trainees.forEach(trainee -> log.info(trainee.toString()));
        }
    }

    private void addTrainingSession(TrainerDTO trainer) {
        String traineeUsername = getInput("Enter trainee username:", input -> !input.trim().isEmpty());
        TraineeDTO trainee = traineeService.findTraineeByUsername(traineeUsername);
        if (trainee == null) {
            log.info("Trainee not found.");
            return;
        }

        String trainingName = getInput("Enter training name:", input -> !input.trim().isEmpty());
        String trainingType = getInput("Enter training type (fitness, yoga, Zumba, stretching, resistance):",
                input -> !input.trim().isEmpty());
        String trainingDate = getInput("Enter training date (yyyy-mm-dd):", this::validateDate);
        int trainingDuration = Integer.parseInt(
                getInput("Enter training duration (in minutes):", input -> input.matches("\\d+")));

        TrainingDTO training = TrainingDTO.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(trainingName)
                .trainingType(TrainingTypeDTO.builder().trainingTypeName(trainingType).build())
                .trainingDate(LocalDate.parse(trainingDate))
                .trainingDuration(trainingDuration)
                .build();

        try {
            validate(training);
            TrainingDTO trainingDTO = trainingService.addTraining(training);
            log.info("Training session added successfully.");
            log.info(trainingDTO.toString());
        } catch (IllegalArgumentException e) {
            log.error("Error adding training session: {}", e.getMessage());
        }
    }

    private void listOwnTrainings(String username) {
        List<TrainingDTO> trainingLog = trainingService.getTrainerTrainings(username, null, null, null);
        if (trainingLog.isEmpty()) {
            log.info("No training logs found.");
        } else {
            trainingLog.forEach(training -> log.info(training.toString()));
        }
    }

    private boolean deactivateUser(String username) {
        userService.setActiveStatus(username, false);
        log.info("User account deactivated. Returning to main menu...");
        return true;
    }

    public void listTrainings(String username) {
        Date fromDate = getInputDate("Enter from date (yyyy-mm-dd) or press Enter to skip:");
        Date toDate = getInputDate("Enter to date (yyyy-mm-dd) or press Enter to skip:");
        String trainerName = getInput("Enter trainer name or press Enter to skip:", input -> true);

        List<TrainingDTO> trainings = trainingService.getTrainerTrainings(username, fromDate, toDate, trainerName);
        if (trainings.isEmpty()) {
            log.info("No trainings found.");
        } else {
            trainings.forEach(training -> log.info(training.toString()));
        }
    }

    public boolean isTrainer(String username) {
        return trainerService.findTrainerByUsername(username) != null;
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

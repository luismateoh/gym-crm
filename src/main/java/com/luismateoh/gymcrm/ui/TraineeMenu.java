package com.luismateoh.gymcrm.ui;

import static com.luismateoh.gymcrm.utils.ValidatorUtil.validate;

import java.util.List;

import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.service.TraineeService;
import com.luismateoh.gymcrm.service.TrainerService;
import com.luismateoh.gymcrm.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "userInteraction")
@Component
public class TraineeMenu extends ConsoleUI {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TraineeMenu(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Override
    public void start(String username) {
        TraineeDTO trainee = traineeService.findTraineeByUsername(username);
        while (true) {
            displayMenu(username);
            int choice = Integer.parseInt(getInput("Choose an option:", input -> validateMenuOption(input, 1, 6)));

            switch (choice) {
                case 1 -> viewProfile(trainee);
                case 2 -> updateProfile(trainee);
                case 3 -> changePassword(trainee.getUser());
                case 4 -> listTrainers();
                case 5 -> listOwnTrainings(username);
                case 6 -> {
                    return;
                }
                default -> log.info("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu(String username) {
        String menu = String.format("""
                                
                User %s
                Trainee Menu:
                1. View profile
                2. Update profile
                3. Change password
                4. View trainers
                5. List own trainings
                6. Logout
                """, username);
        log.info(menu);
    }

    private void viewProfile(TraineeDTO trainee) {
        log.info(trainee.toString());
    }

    private void updateProfile(TraineeDTO trainee) {
        String address = getInput("Enter new address:", input -> !input.trim().isEmpty());
        trainee.setAddress(address);
        try {
            validate(trainee);
            traineeService.updateTraineeProfile(trainee);
            log.info("Profile updated successfully.");
        } catch (IllegalArgumentException e) {
            log.error("Error updating profile: {}", e.getMessage());
        }
    }

    private void changePassword(UserDTO user) {
        String newPassword = getInput("Enter new password:", input -> !input.trim().isEmpty());
        traineeService.changePassword(user, newPassword);
        log.info("Password changed successfully.");
    }

    private void listTrainers() {
        List<TrainerDTO> trainers = trainerService.findAllTrainers();
        if (trainers.isEmpty()) {
            log.info("No trainers found.");
        } else {
            trainers.forEach(trainer -> log.info(trainer.toString()));
        }
    }

    private void listOwnTrainings(String username) {
        List<TrainingDTO> trainingLog = trainingService.getTraineeTrainings(username, null, null, null, null);
        if (trainingLog.isEmpty()) {
            log.info("No training logs found.");
        } else {
            trainingLog.forEach(training -> log.info(training.toString()));
        }
    }

    public boolean isTrainee(String username) {
        return traineeService.findTraineeByUsername(username) != null;
    }
}

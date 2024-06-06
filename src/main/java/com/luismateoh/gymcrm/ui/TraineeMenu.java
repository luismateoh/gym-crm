package com.luismateoh.gymcrm.ui;

import static com.luismateoh.gymcrm.utils.ValidatorUtil.validate;

import java.util.Date;
import java.util.List;

import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.service.TraineeService;
import com.luismateoh.gymcrm.service.TrainerService;
import com.luismateoh.gymcrm.service.TrainingService;
import com.luismateoh.gymcrm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "userInteraction")
@Component
public class TraineeMenu extends ConsoleUI {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    public TraineeMenu(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService,
                       UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    @Override
    public void start(String username) {
        TraineeDTO trainee = traineeService.findTraineeByUsername(username);
        boolean keepLoggedIn = true;

        while (keepLoggedIn) {
            displayMenu(username);
            int choice = Integer.parseInt(getInput("Choose an option:", input -> validateMenuOption(input, 1, 9)));

            switch (choice) {
                case 1 -> viewProfile(trainee);
                case 2 -> updateProfile(trainee);
                case 3 -> changePassword(trainee.getUser());
                case 4 -> listTrainers();
                case 5 -> listOwnTrainings(username);
                case 6 -> listTrainings(username);
                case 7 -> keepLoggedIn = !deactivateUser(username);
                case 8 -> keepLoggedIn = !deleteProfile(username);
                case 9 -> {
                    log.info("Logging out...");
                    keepLoggedIn = false;
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
                6. List trainings
                7. Deactivate account
                8. Delete account
                9. Logout
                """, username);
        log.info(menu);
    }

    private void viewProfile(TraineeDTO trainee) {
        log.info(trainee.toString());
        log.info(String.format("Password: %s", trainee.getUser().getPassword()));
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
        userService.changePassword(user, newPassword);
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

    public void listTrainings(String username) {
        Date fromDate = getInputDate("Enter from date (yyyy-mm-dd) or press Enter to skip:");
        Date toDate = getInputDate("Enter to date (yyyy-mm-dd) or press Enter to skip:");
        String trainerName = getInput("Enter trainer name or press Enter to skip:", input -> true);
        String trainingType = getInput("Enter training type or press Enter to skip:", input -> true);

        List<TrainingDTO> trainings = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName,
                trainingType);
        if (trainings.isEmpty()) {
            log.info("No trainings found.");
        } else {
            trainings.forEach(training -> log.info(training.toString()));
        }
    }

    private boolean deactivateUser(String username) {
        userService.setActiveStatus(username, false);
        log.info("User account deactivated. Returning to main menu...");
        return true;
    }

    private boolean deleteProfile(String username) {
        traineeService.deleteTraineeByUsername(username);
        log.info("Profile deleted successfully. Returning to main menu...");
        return true;
    }

    public boolean isTrainee(String username) {
        return traineeService.findTraineeByUsername(username) != null;
    }
}

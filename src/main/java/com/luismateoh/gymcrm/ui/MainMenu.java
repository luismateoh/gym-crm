package com.luismateoh.gymcrm.ui;

import java.util.List;
import java.util.Optional;

import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.dto.UserDTO;
import com.luismateoh.gymcrm.service.TrainingService;
import com.luismateoh.gymcrm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "userInteraction")
@Component
public class MainMenu extends ConsoleUI {
    private final UserService userService;
    private final TrainingService trainingService;
    private final TrainerMenu trainerMenu;
    private final TraineeMenu traineeMenu;
    private final RegisterMenu registerMenu;

    public MainMenu(UserService userService, TrainingService trainingService, TrainerMenu trainerMenu,
                    TraineeMenu traineeMenu, RegisterMenu registerMenu) {
        this.userService = userService;
        this.trainingService = trainingService;
        this.trainerMenu = trainerMenu;
        this.traineeMenu = traineeMenu;
        this.registerMenu = registerMenu;
    }

    @Override
    public void start(String username) {
        while (true) {
            displayMenu();
            int choice = Integer.parseInt(getInput("Choose an option:", input -> validateMenuOption(input, 1, 4)));

            switch (choice) {
                case 1 -> login();
                case 2 -> startRegisterMenu(username);
                case 3 -> listTrainings();
                case 4 -> {
                    exitApplication();
                    return;
                }
                default -> invalidChoice();
            }
        }
    }

    private void displayMenu() {
        String menu = """
                Main Menu:
                1. Login
                2. Register
                3. List trainings
                4. Exit
                """;
        log.info(menu);
    }

    private void login() {
        String user = getInput("Enter username:", input -> !input.trim().isEmpty());
        String password = getInput("Enter password:", input -> !input.trim().isEmpty());

        Optional<UserDTO> userDetailsOpt = userService.findByUsername(user);
        if (userDetailsOpt.isEmpty()) {
            log.info("Invalid username or password. Please try again.");
            return;
        }

        UserDTO userDetails = userDetailsOpt.get();

        if (Boolean.FALSE.equals(userDetails.getIsActive())) {
            log.info("User is inactive. Please enter your password again to reactivate your account.");
            String confirmPassword = getInput("Confirm password:", input -> !input.trim().isEmpty());
            if (userService.validatePassword(userDetails, confirmPassword)) {
                userService.setActiveStatus(user, true);
                log.info("User reactivated successfully.");
                login();
            } else {
                log.info("Passwords do not match. Reactivation failed.");
                return;
            }
        }

        if (userService.login(user, password).isPresent()) {
            if (traineeMenu.isTrainee(user)) {
                traineeMenu.start(user);
            } else if (trainerMenu.isTrainer(user)) {
                trainerMenu.start(user);
            } else {
                log.info("Invalid username or password.");
            }
        } else {
            log.info("Invalid username or password. Please try again.");
        }
    }

    private void startRegisterMenu(String username) {
        registerMenu.start(username);
    }

    private void listTrainings() {
        log.info("""
                List of trainings:
                ---------------------------------
                """);
        List<TrainingDTO> trainings = trainingService.findAllTrainings();
        if (trainings.isEmpty()) {
            log.info("No trainings found.");
        } else {
            trainings.forEach(training -> log.info(training.toString()));
        }
    }

    private void invalidChoice() {
        log.info("Invalid choice. Please try again.");
    }

    private void exitApplication() {
        log.info("Exiting...");
        System.exit(0);
    }
}

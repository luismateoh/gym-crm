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
        // Prompt for username and password
        String user = getInput("Enter username:", input -> !input.trim().isEmpty());
        String password = getInput("Enter password:", input -> !input.trim().isEmpty());

        // Attempt to retrieve user details
        Optional<UserDTO> userDetailsOpt = userService.findByUsername(user);
        if (!userDetailsOpt.isPresent()) {
            log.info("Invalid username or password. Please try again.");
            return;
        }

        UserDTO userDetails = userDetailsOpt.get();

        // Check if user is active
        if (!userDetails.getIsActive()) {
            log.info("User is inactive. Please enter your password again to reactivate your account.");
            String confirmPassword = getInput("Confirm password:", input -> !input.trim().isEmpty());
            if (userService.validatePassword(userDetails, confirmPassword)) {
                userService.activateUser(user);
                log.info("User reactivated successfully.");
                login(); // Recursive call to login after reactivation
            } else {
                log.info("Passwords do not match. Reactivation failed.");
                return;
            }
        }

        // Attempt to log in with provided credentials
        if (userService.login(user, password).isPresent()) {
            // Route user based on type
            if (traineeMenu.isTrainee(user)) {
                traineeMenu.start(user);
            } else if (trainerMenu.isTrainer(user)) {
                trainerMenu.start(user);
            } else {
                log.info("User type not recognized.");
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

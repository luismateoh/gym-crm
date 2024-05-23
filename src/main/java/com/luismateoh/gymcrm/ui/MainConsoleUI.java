package com.luismateoh.gymcrm.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainConsoleUI extends ConsoleUI {
    private final TraineeUI traineeUI;
    private final TrainerUI trainerUI;
    private final TrainingUI trainingUI;

    public MainConsoleUI(TraineeUI traineeUI, TrainerUI trainerUI, TrainingUI trainingUI) {
        this.traineeUI = traineeUI;
        this.trainerUI = trainerUI;
        this.trainingUI = trainingUI;
    }

    @Override
    public void start() {
        while (true) {
            try {
                System.out.println("Manage - Please select an option:");
                System.out.println("[1]. Trainees");
                System.out.println("[2]. Trainers");
                System.out.println("[3]. Trainings");
                System.out.println("[0]. Exit");

                String input = scanner.nextLine();

                switch (input) {
                    case "1":
                        traineeUI.start();
                        break;
                    case "2":
                        trainerUI.start();
                        break;
                    case "3":
                        trainingUI.start();
                        break;
                    case "0":
                        System.out.println("Exiting...");
                        log.info("Application closed by user.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to the main menu...");
            }
        }
    }
}
package com.luismateoh.gymcrm.ui;

import com.luismateoh.gymcrm.domain.Training;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.facade.CRMFacade;
import org.springframework.stereotype.Component;

@Component
public class TrainingUI extends ConsoleUI {
    private final CRMFacade crmFacade;

    public TrainingUI(CRMFacade crmFacade) {
        this.crmFacade = crmFacade;
    }

    @Override
    public void start() {
        boolean continueRunning = true;
        while (continueRunning) {
            displayTrainingMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleCreateTraining();
                    break;
                case "2":
                    handleViewTraining();
                    break;
                case "0":
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayTrainingMenu() {
        System.out.println("\nManage Trainings - Please select an option:");
        System.out.println("[1]. Create Training");
        System.out.println("[2]. View Training");
        System.out.println("[0]. Back");
    }

    private void handleCreateTraining() {
        try {
            TrainingDTO trainingDTO = new TrainingDTO(
                    getInput("Enter trainee's ID:"),
                    getInput("Enter trainer's ID:"),
                    getInput("Enter training name:"),
                    getInput("Enter training date (YYYY-MM-DD):"),
                    Integer.parseInt(getInput("Enter training duration (in minutes):")),
                    getInput("Enter training type ID:")
            );
            crmFacade.createTraining(trainingDTO);
            System.out.println("Training created successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid number.");
        }
    }

    private String getInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private void handleViewTraining() {
        try {
            Long trainingId = Long.parseLong(getInput("Enter training's ID to view:"));
            Training training = crmFacade.selectTraining(trainingId);
            if (training != null) {
                displayTrainingDetails(training);
            } else {
                System.out.println("Training not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void displayTrainingDetails(Training training) {
        System.out.println("\nTraining Details:");
        System.out.println("ID: " + training.getId());
        System.out.println("Trainee ID: " + training.getTraineeId());
        System.out.println("Trainer ID: " + training.getTrainerId());
        System.out.println("Training Name: " + training.getTrainingName());
        System.out.println("Training Date: " + training.getTrainingDate());
        System.out.println("Training Duration: " + training.getTrainingDuration());
        if (training.getTrainingType() != null) {
            System.out.println("Training Type ID: " + training.getTrainingType().getId());
            System.out.println("Training Type Name: " + training.getTrainingType().getName());
        } else {
            System.out.println("Training Type: N/A");
        }
    }
}

package com.luismateoh.gymcrm.ui;

import java.time.LocalDate;

import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import com.luismateoh.gymcrm.facade.CRMFacade;
import com.luismateoh.gymcrm.utils.Validator;
import org.springframework.stereotype.Component;

@Component
public class TrainerUI extends ConsoleUI {
    private final CRMFacade crmFacade;

    public TrainerUI(CRMFacade crmFacade) {
        this.crmFacade = crmFacade;
    }

    @Override
    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleCreateTrainer();
                    break;
                case "2":
                    handleUpdateTrainer();
                    break;
                case "3":
                    handleViewTrainer();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nManage Trainers - Please select an option:");
        System.out.println("[1]. Create Trainer");
        System.out.println("[2]. Update Trainer");
        System.out.println("[3]. View Trainer");
        System.out.println("[0]. Back");
    }

    private void handleCreateTrainer() {
        TrainerDTO trainer = new TrainerDTO(getInput("Enter trainer's first name:", Validator::validateText),
                getInput("Enter trainer's last name:", Validator::validateText),
                getInput("Enter trainer's specialization:", Validator::validateText));
        displayTrainerDetails(crmFacade.createTrainer(trainer));
    }

    private void handleUpdateTrainer() {
        try {
            Long id = Long.parseLong(getInput("Enter trainer's ID to update:", Validator::validateNumber));
            Trainer trainerToUpdate = crmFacade.selectTrainer(id);
            if (trainerToUpdate != null) {
                updateTrainerDetails(trainerToUpdate);
                crmFacade.updateTrainer(trainerToUpdate);
                System.out.println("Trainer updated successfully.");
            } else {
                System.out.println("Trainer not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void updateTrainerDetails(Trainer trainer) {
        trainer.setFirstName(getInput("Enter trainer's new first name:", Validator::validateText));
        trainer.setLastName(getInput("Enter trainer's new last name:", Validator::validateText));
        trainer.setSpecialization(getInput("Enter trainer's new specialization:", Validator::validateText));
        trainer.setIsActive(Boolean.parseBoolean(getInput("Enter trainer's new active state (true or false):", Validator::validateBoolean)));
        displayTrainerDetails(crmFacade.updateTrainer(trainer));
    }

    private void handleViewTrainer() {
        try {
            Long trainerId = Long.parseLong(getInput("Enter trainer's ID to view:", Validator::validateNumber));
            Trainer trainer = crmFacade.selectTrainer(trainerId);
            if (trainer != null) {
                displayTrainerDetails(trainer);
            } else {
                System.out.println("Trainer not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void displayTrainerDetails(Trainer trainer) {
        System.out.println("\nTrainer Details:");
        System.out.println("ID: " + trainer.getId());
        System.out.println("First Name: " + trainer.getFirstName());
        System.out.println("Last Name: " + trainer.getLastName());
        System.out.println("Specialization: " + trainer.getSpecialization());
        System.out.println("Active: " + (Boolean.TRUE.equals(trainer.getIsActive()) ? "Yes" : "No"));

        System.out.println("\nProfile:");
        System.out.println("Username: " + trainer.getUserName());
        System.out.println("Password: " + trainer.getPassword());
    }
}

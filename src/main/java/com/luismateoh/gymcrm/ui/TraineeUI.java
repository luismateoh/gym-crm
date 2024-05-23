package com.luismateoh.gymcrm.ui;

import java.time.LocalDate;
import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.dto.TraineeDTO;
import com.luismateoh.gymcrm.facade.CRMFacade;
import com.luismateoh.gymcrm.utils.Validator;
import org.springframework.stereotype.Component;

@Component
public class TraineeUI extends ConsoleUI {
    private final CRMFacade crmFacade;

    public TraineeUI(CRMFacade crmFacade) {
        this.crmFacade = crmFacade;
    }

    @Override
    public void start() {
        boolean isRunning = true;
        while (isRunning) {
            displayTraineeMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleCreateTrainee();
                    break;
                case "2":
                    handleUpdateTrainee();
                    break;
                case "3":
                    handleDeleteTrainee();
                    break;
                case "4":
                    handleViewTrainee();
                    break;
                case "0":
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayTraineeMenu() {
        System.out.println("\nManage Trainees - Please select an option:");
        System.out.println("[1]. Create Trainee");
        System.out.println("[2]. Update Trainee");
        System.out.println("[3]. Delete Trainee");
        System.out.println("[4]. View Trainee");
        System.out.println("[0]. Back");
    }

    private void handleCreateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO(
                getInput("Enter trainee's first name:", Validator::validateText),
                getInput("Enter trainee's last name:", Validator::validateText),
                getInput("Enter trainee's date of birth (YYYY-MM-DD):", Validator::validateDate),
                getInput("Enter trainee's address:", Validator::validateText)
        );
        displayTraineeDetails(crmFacade.createTrainee(traineeDTO));
    }

    private void handleUpdateTrainee() {
        try {
            Long id = Long.parseLong(getInput("Enter trainee's ID to update:", Validator::validateNumber));
            Trainee traineeToUpdate = crmFacade.selectTrainee(id);
            if (traineeToUpdate != null) {
                updateTraineeDetails(traineeToUpdate);
            } else {
                System.out.println("Trainee not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void updateTraineeDetails(Trainee trainee) {
        trainee.setFirstName(getInput("Enter trainee's new first name:", Validator::validateText));
        trainee.setLastName(getInput("Enter trainee's new last name:", Validator::validateText));
        trainee.setDateOfBirth(LocalDate.parse(getInput("Enter trainee's new date of birth (YYYY-MM-DD):", Validator::validateDate)));
        trainee.setAddress(getInput("Enter trainee's new address:", Validator::validateAddress));
        trainee.setIsActive(Boolean.parseBoolean(getInput("Enter trainee's new active state (true or false):", Validator::validateBoolean)));
        displayTraineeDetails(crmFacade.updateTrainee(trainee));
    }

    private void handleDeleteTrainee() {
        try {
            Long traineeId = Long.parseLong(getInput("Enter trainee's ID to delete:", Validator::validateNumber));
            crmFacade.deleteTrainee(traineeId);
            System.out.println("Trainee deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void handleViewTrainee() {
        try {
            Long viewTraineeId = Long.parseLong(getInput("Enter trainee's ID to view:", Validator::validateNumber));
            Trainee trainee = crmFacade.selectTrainee(viewTraineeId);
            if (trainee != null) {
                displayTraineeDetails(trainee);
            } else {
                System.out.println("Trainee not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void displayTraineeDetails(Trainee trainee) {
        System.out.println("\nTrainee Details:");
        System.out.println("ID: " + trainee.getId());
        System.out.println("First Name: " + trainee.getFirstName());
        System.out.println("Last Name: " + trainee.getLastName());
        System.out.println("Date of Birth: " + trainee.getDateOfBirth());
        System.out.println("Address: " + trainee.getAddress());
        System.out.println("Active: " + (Boolean.TRUE.equals(trainee.getIsActive()) ? "Yes" : "No"));

        System.out.println("\nProfile:");
        System.out.println("Username: " + trainee.getUserName());
        System.out.println("Password: " + trainee.getPassword());
    }
}

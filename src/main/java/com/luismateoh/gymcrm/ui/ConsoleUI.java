package com.luismateoh.gymcrm.ui;

import java.util.Date;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "userInteraction")
@Component
public abstract class ConsoleUI {
    protected Scanner scanner;

    protected ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public abstract void start(String username);

    protected String getInput(String prompt, ValidatorFunction validator) {
        String input;
        log.info(prompt);
        while (true) {
            input = scanner.nextLine();
            if (validator.validate(input)) {
                break;
            }
            log.info("Invalid input. Please try again.");
        }
        return input;
    }

    @FunctionalInterface
    public interface ValidatorFunction {
        boolean validate(String text);
    }

    protected boolean validateMenuOption(String input, int min, int max) {
        try {
            int option = Integer.parseInt(input);
            return option >= min && option <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    Date getInputDate(String prompt) {
        String dateInput = getInput(prompt, input -> true);
        if (dateInput.trim().isEmpty()) {
            return null;
        }
        try {
            return java.sql.Date.valueOf(dateInput);
        } catch (IllegalArgumentException e) {
            log.info("Invalid date format. Please try again.");
            return getInputDate(prompt);
        }
    }
}

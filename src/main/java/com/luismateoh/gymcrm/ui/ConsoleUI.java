package com.luismateoh.gymcrm.ui;

import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public abstract class ConsoleUI {
    protected Scanner scanner;

    protected ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public abstract void start();

    protected String getInput(String prompt, ValidatorFunction validator) {
        String input;
        System.out.println(prompt);
        while (true) {
            input = scanner.nextLine();
            if (validator.validate(input)) {
                break;
            }
            System.out.println("Invalid input. Please try again.");
        }
        return input;
    }

    @FunctionalInterface
    interface ValidatorFunction {
        boolean validate(String text);
    }
}

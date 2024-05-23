package com.luismateoh.gymcrm.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Validator {

    private Validator() {
    }

    private static final Pattern TEXT_PATTERN = Pattern.compile("^[a-zA-Z ]{1,50}$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d{1,10}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9 ,.-]{1,100}$");

    public static boolean validateText(String text) {
        return TEXT_PATTERN.matcher(text).matches();
    }

    public static boolean validateNumber(String number) {
        return NUMBER_PATTERN.matcher(number).matches();
    }

    public static boolean validateDate(String dateStr) {
        if (!DATE_PATTERN.matcher(dateStr).matches()) {
            return false;
        }
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validateBoolean(String booleanStr) {
        return "true".equalsIgnoreCase(booleanStr) || "false".equalsIgnoreCase(booleanStr);
    }

    public static boolean validateAddress(String address) {
        return ADDRESS_PATTERN.matcher(address).matches();
    }
}
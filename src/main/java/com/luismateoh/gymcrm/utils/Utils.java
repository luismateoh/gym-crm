package com.luismateoh.gymcrm.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

    private Utils() {
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

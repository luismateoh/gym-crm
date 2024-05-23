package com.luismateoh.gymcrm.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainee extends User {

    private LocalDate dateOfBirth;

    private String address;

    private Training training;

    private String userId;
}

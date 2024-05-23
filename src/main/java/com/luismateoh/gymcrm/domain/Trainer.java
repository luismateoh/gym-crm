package com.luismateoh.gymcrm.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User {

    private String specialization;

    private Training training;

    private TrainingType trainingType;

    private String userId;
}

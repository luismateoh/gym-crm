package com.luismateoh.gymcrm.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Training {

    private Long id;

    private String traineeId;

    private String trainerId;

    private String trainingName;

    private TrainingType trainingType;

    private LocalDate trainingDate;

    private Integer trainingDuration;
}

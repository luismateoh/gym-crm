package com.luismateoh.gymcrm.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingDTO {
    private Long id;

    @NotNull(message = "Trainee cannot be null")
    private TraineeDTO trainee;

    @NotNull(message = "Trainer cannot be null")
    private TrainerDTO trainer;

    @NotBlank(message = "Training name cannot be blank")
    private String trainingName;

    @NotNull(message = "Training type cannot be null")
    private TrainingTypeDTO trainingType;

    @FutureOrPresent(message = "Training date must be in the present or future")
    private LocalDate trainingDate;

    @Positive(message = "Training duration must be positive")
    @Max(value = 240, message = "Training duration must be less than 240 minutes")
    private Integer trainingDuration;

    @Override
    public String toString() {
        return "Training -> " + "ID: " + id + "\nName: '" + trainingName + ", Type: " + trainingType.getTrainingTypeName() + ", Date: " + trainingDate + ", Duration: " + trainingDuration + ", Trainer: " + trainer.getUser()
                .getUsername() + ", Trainee: " + trainee.getUser().getUsername() + "\n";

    }
}

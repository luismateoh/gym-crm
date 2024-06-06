package com.luismateoh.gymcrm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerDTO {
    private Long id;

    @NotNull(message = "User cannot be null")
    private UserDTO user;

    @NotNull(message = "Specialization cannot be null")
    private TrainingTypeDTO specialization;

    @Override
    public String toString() {
        return "Trainer -> " + "ID: " + id + ", Specialization: " + specialization.getTrainingTypeName() + "\n" + user + "\n";
    }
}

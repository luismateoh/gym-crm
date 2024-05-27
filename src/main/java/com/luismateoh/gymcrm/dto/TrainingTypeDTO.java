package com.luismateoh.gymcrm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingTypeDTO {
    private Long id;

    @NotBlank(message = "Training type name cannot be blank")
    private String trainingTypeName;

    @Override
    public String toString() {
        return "TrainingType:" + "ID:" + id + "\n Type Name:" + trainingTypeName;
    }
}

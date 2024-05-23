package com.luismateoh.gymcrm.dto;

public record TrainingDTO(String traineeId, String trainerId, String trainingName, String trainingDate,
                          Integer trainingDuration, String trainingTypeId) {
}

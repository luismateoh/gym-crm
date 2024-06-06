package com.luismateoh.gymcrm.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraineeDTO {
    private Long id;

    @NotNull(message = "User cannot be null")
    private UserDTO user;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 100, message = "Address cannot be longer than 100 characters")
    private String address;

    @Override
    public String toString() {
        return "Trainee -> " + "ID: " + id + ", dateOfBirth: " + dateOfBirth + ", address: " + address + "\n" + user + "\n";
    }
}

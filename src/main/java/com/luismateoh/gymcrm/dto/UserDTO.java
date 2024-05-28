package com.luismateoh.gymcrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot be longer than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot be longer than 50 characters")
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username cannot be longer than 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 50, message = "Password cannot be longer than 50 characters")
    private String password;

    @NotNull(message = "Active status cannot be null")
    private Boolean isActive;

    @Override
    public String toString() {

        return "User -> " + " ID: " + id + ", First Name: " + firstName + ", Last Name: " + lastName + ", Username: " + username + ", Active: " + isActive;
    }
}

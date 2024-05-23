package com.luismateoh.gymcrm.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private Boolean isActive = true;
}

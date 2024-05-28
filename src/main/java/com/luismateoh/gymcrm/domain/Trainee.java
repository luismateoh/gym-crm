package com.luismateoh.gymcrm.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trainees")
public class Trainee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth", nullable = true)
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private String address;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

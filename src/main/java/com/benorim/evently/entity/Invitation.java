package com.benorim.evently.entity;

import com.benorim.evently.enums.Rsvp;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    private Guest guest;

    private Boolean attended;

    @ManyToOne
    private Event event;

    @Enumerated(EnumType.STRING)
    private Rsvp rsvp;

    private LocalDate dateSent;

    private LocalDate rsvpByDate;

    private LocalDate dateRsvped;
}

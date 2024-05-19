package com.benorim.evently.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Guest {
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String email;
    private String phone;
}

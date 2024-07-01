package com.benorim.evently.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Embedded
    private Address address;

    private String imageUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    private Boolean isPrivate;

    @ManyToOne
    private EventlyUser createdBy;

    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<Invitation> invitations;

    private String additionalNotes;

    public Event(String title, String description, Address address, String imageUrl, LocalDateTime startTime, LocalDateTime endTime, List<Invitation> invitations, String additionalNotes, Boolean isPrivate) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.invitations = invitations;
        this.additionalNotes = additionalNotes;
        this.isPrivate = isPrivate;
    }

    public Event(String title, String description, Address address, String imageUrl, LocalDateTime startTime, LocalDateTime endTime, String additionalNotes, Boolean isPrivate) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.additionalNotes = additionalNotes;
        this.isPrivate = isPrivate;
    }
}

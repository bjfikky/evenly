package com.benorim.evently.service;

import com.benorim.evently.entity.Invitation;
import com.benorim.evently.enums.Rsvp;
import com.benorim.evently.exception.EmailSendException;
import com.benorim.evently.exception.IllegalOperationException;
import com.benorim.evently.exception.ResourceNotFoundException;
import com.benorim.evently.repository.InvitationRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    private final EventService eventService;

    public InvitationService(InvitationRepository invitationRepository, EmailService emailService, EventService eventService) {
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
        this.eventService = eventService;
    }

    @Transactional(rollbackFor = MessagingException.class)
    public Invitation save(Invitation invitation) {
        if (invitation.getEvent().getIsPrivate()) {
            eventService.validateEventOwner(invitation.getEvent().getId());
        }
        invitation.setDateSent(LocalDate.now());
        Invitation savedInvitation = invitationRepository.save(invitation);

        sendInvitationEmail(savedInvitation);
        return savedInvitation;
    }

    @Transactional(rollbackFor = MessagingException.class)
    public List<Invitation> saveAll(List<Invitation> invitations) {
        invitations.forEach(invitation -> {
            if (invitation.getEvent().getIsPrivate()) {
                eventService.validateEventOwner(invitation.getEvent().getId());
            }
            invitation.setDateSent(LocalDate.now());
        });
        List<Invitation> savedInvitations = invitationRepository.saveAll(invitations);

        savedInvitations.forEach(this::sendInvitationEmail);

        return savedInvitations;
    }

    public Invitation findById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
    }

    public List<Invitation> findAllByEventId(Long eventId) {
        return invitationRepository.findAllByEventId(eventId);
    }

    public Invitation updateRsvp(Rsvp rsvp, Long id, String token) {
        Invitation invitation = findById(id);
        if (!invitation.getToken().equals(token)) {
            throw new IllegalOperationException("Invalid token");
        }
        invitation.setRsvp(rsvp);
        invitation.setDateRsvped(LocalDate.now());
        return invitationRepository.save(invitation);
    }

    private void sendInvitationEmail(Invitation savedInvitation) {
        try {
            emailService.sendHtmlMessage(
                    savedInvitation.getGuest().getEmail(),
                    "Invitation to " + savedInvitation.getEvent().getTitle(),
                    savedInvitation);
        } catch (MessagingException e) {
            throw new EmailSendException("Email send error");
        }
    }
}

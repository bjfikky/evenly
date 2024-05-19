package com.benorim.evently.service;

import com.benorim.evently.entity.Invitation;
import com.benorim.evently.repository.InvitationRepository;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    public InvitationService(InvitationRepository invitationRepository, EmailService emailService) {
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    @Transactional(rollbackFor = MailException.class)
    public Invitation save(Invitation invitation) {
        invitation.setDateSent(LocalDate.now());
        Invitation savedInvitation = invitationRepository.save(invitation);

        emailService.sendSimpleMessage(
                invitation.getGuest().getEmail(),
                "Invitation to " + invitation.getEvent().getTitle(),
                "Body of the invitation");
        return savedInvitation;
    }

    public Invitation findById(Long id) {
        return invitationRepository.findById(id).orElse(null);
    }

    public List<Invitation> findAllByEventId(Long eventId) {
        return invitationRepository.findAllByEventId(eventId);
    }
}

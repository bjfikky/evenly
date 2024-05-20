package com.benorim.evently.api;

import com.benorim.evently.api.request.InvitationRequest;
import com.benorim.evently.entity.Event;
import com.benorim.evently.entity.Invitation;
import com.benorim.evently.mapper.InvitationMapper;
import com.benorim.evently.service.EventService;
import com.benorim.evently.service.InvitationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitations")
public class InvitationApi {

    private final InvitationService invitationService;
    private final EventService eventService;

    public InvitationApi(InvitationService invitationService, EventService eventService) {
        this.invitationService = invitationService;
        this.eventService = eventService;
    }

    @PostMapping("/send-one")
    public ResponseEntity<?> sendInvitation(@Valid @RequestBody InvitationRequest invitationRequest) {
        Event event = eventService.getEventById(invitationRequest.eventId());
        Invitation invitation = InvitationMapper.mapInvitationRequestToInvitation(invitationRequest);
        invitation.setEvent(event);
        Invitation saved = invitationService.save(invitation);
        return ResponseEntity.ok(saved);
    }

//    @PostMapping("/send-all")
//    public Invitation sendInvitations(@Valid @RequestBody List<InvitationRequest> invitationRequests) {
//
//    }
}

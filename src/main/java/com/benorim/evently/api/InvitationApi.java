package com.benorim.evently.api;

import com.benorim.evently.api.request.InvitationRequest;
import com.benorim.evently.api.response.InvitationResponse;
import com.benorim.evently.api.response.InvitationsResponse;
import com.benorim.evently.entity.Event;
import com.benorim.evently.entity.Invitation;
import com.benorim.evently.enums.Rsvp;
import com.benorim.evently.service.EventService;
import com.benorim.evently.service.InvitationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.benorim.evently.mapper.InvitationMapper.mapInvitationRequestToInvitation;
import static com.benorim.evently.mapper.InvitationMapper.mapInvitationToInvitationResponse;
import static com.benorim.evently.mapper.InvitationMapper.mapInvitationsToInvitationResponseList;
import static org.apache.commons.lang3.StringUtils.upperCase;

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
    public ResponseEntity<InvitationResponse> sendInvitation(@Valid @RequestBody InvitationRequest invitationRequest) {
        Event event = eventService.getEventById(invitationRequest.eventId());
        Invitation invitation = mapInvitationRequestToInvitation(invitationRequest);
        invitation.setEvent(event);
        Invitation saved = invitationService.save(invitation);
        return ResponseEntity.ok(mapInvitationToInvitationResponse(saved));
    }

    @PostMapping("/send-all")
    public ResponseEntity<InvitationsResponse> sendInvitations(@Valid @RequestBody List<InvitationRequest> invitationRequests) {
        List<InvitationResponse> invitationResponses = new ArrayList<>();
        invitationRequests.forEach(invitationRequest -> {
            Event event = eventService.getEventById(invitationRequest.eventId());
            Invitation invitation = mapInvitationRequestToInvitation(invitationRequest);
            invitation.setEvent(event);
            invitationResponses.add(mapInvitationToInvitationResponse(invitationService.save(invitation)));
        });

        return ResponseEntity.ok(new InvitationsResponse(invitationResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvitationResponse> getInvitationById(@PathVariable long id) {
        Invitation invitation = invitationService.findById(id);
        if (invitation == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapInvitationToInvitationResponse(invitation));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<InvitationsResponse> getInvitationByEvent(@PathVariable long eventId) {
        List<Invitation> invitations = invitationService.findAllByEventId(eventId);
        return ResponseEntity.ok(new InvitationsResponse(mapInvitationsToInvitationResponseList(invitations)));
    }

    @GetMapping("/{id}/respond/{rsvp}")
    public ResponseEntity<?> respondToInvitation(
            @PathVariable long id, @PathVariable Rsvp rsvp, @RequestParam String token) {
        invitationService.updateRsvp(Rsvp.valueOf(upperCase(rsvp.name())), id, token);
        return ResponseEntity.ok().build();
    }
}

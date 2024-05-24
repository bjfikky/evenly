package com.benorim.evently.mapper;

import com.benorim.evently.api.request.InvitationRequest;
import com.benorim.evently.api.response.EventResponse;
import com.benorim.evently.api.response.InvitationResponse;
import com.benorim.evently.entity.Guest;
import com.benorim.evently.entity.Invitation;

public class InvitationMapper {

    public static Invitation mapInvitationRequestToInvitation(InvitationRequest invitationRequest) {
        Guest guest = new Guest(
                invitationRequest.firstName(),
                invitationRequest.lastName(),
                invitationRequest.email(),
                invitationRequest.phone());

        return new Invitation(null, guest, null, null, null, null, null, null);
    }

    public static InvitationResponse mapInvitationToInvitationResponse(Invitation invitation) {
        EventResponse eventResponse = new EventResponse(
                invitation.getEvent().getId(),
                invitation.getEvent().getTitle(),
                invitation.getEvent().getDescription(),
                invitation.getEvent().getAddress().getAddressLine1(),
                invitation.getEvent().getAddress().getAddressLine2(),
                invitation.getEvent().getAddress().getCity(),
                invitation.getEvent().getAddress().getState(),
                invitation.getEvent().getAddress().getZipCode(),
                invitation.getEvent().getImageUrl(),
                invitation.getEvent().getStartTime(),
                invitation.getEvent().getEndTime(),
                invitation.getEvent().getCreatedAt(),
                invitation.getEvent().getUpdatedAt(),
                invitation.getEvent().getAdditionalNotes()
        );

        return new InvitationResponse(
                invitation.getId(),
                invitation.getGuest().getEmail(),
                invitation.getGuest().getFirstName(),
                invitation.getGuest().getLastName(),
                invitation.getGuest().getPhone(),
                invitation.getRsvp(),
                eventResponse,
                invitation.getDateSent(),
                invitation.getRsvpByDate(),
                invitation.getDateRsvped()
        );
    }
}

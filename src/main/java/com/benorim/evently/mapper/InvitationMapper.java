package com.benorim.evently.mapper;

import com.benorim.evently.api.request.InvitationRequest;
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
}

package com.benorim.evently.service;

import com.benorim.evently.entity.Address;
import com.benorim.evently.entity.Event;
import com.benorim.evently.entity.Guest;
import com.benorim.evently.entity.Invitation;
import com.benorim.evently.exception.EmailSendException;
import com.benorim.evently.repository.InvitationRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private InvitationService invitationService;

    @Captor
    private ArgumentCaptor<Invitation> invitationCaptor;

    private Invitation invitation;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setAddressLine1("address line 1");
        address.setAddressLine2("address line 2");
        address.setCity("city");
        address.setState("state");
        address.setZipCode("00000");

        Guest guest = new Guest();
        guest.setEmail("test@email.com");

        Event event = new Event();
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setAddress(address);

        invitation = new Invitation();
        invitation.setEvent(event);
        invitation.setGuest(guest);
    }

    @Test
    void save() throws MessagingException {
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);

        Invitation savedInvitation = invitationService.save(invitation);

        assertNotNull(savedInvitation);
        assertEquals(LocalDate.now(), savedInvitation.getDateSent());
        verify(invitationRepository, times(1)).save(invitationCaptor.capture());
        verify(emailService, times(1)).sendHtmlMessage(anyString(), anyString(), anyString());

        Invitation capturedInvitation = invitationCaptor.getValue();
        assertEquals(invitation, capturedInvitation);
    }

    @Test
    void saveAll() throws MessagingException {
        List<Invitation> invitations = Arrays.asList(invitation, invitation);
        when(invitationRepository.saveAll(anyList())).thenReturn(invitations);

        List<Invitation> savedInvitations = invitationService.saveAll(invitations);

        assertNotNull(savedInvitations);
        assertEquals(2, savedInvitations.size());
        savedInvitations.forEach(savedInvitation -> assertEquals(LocalDate.now(), savedInvitation.getDateSent()));
        verify(invitationRepository, times(1)).saveAll(invitations);
        verify(emailService, times(2)).sendHtmlMessage(anyString(), anyString(), anyString());
    }

    @Test
    void findById() {
        Long id = 1L;
        when(invitationRepository.findById(id)).thenReturn(Optional.of(invitation));

        Invitation foundInvitation = invitationService.findById(id);

        assertNotNull(foundInvitation);
        assertEquals(invitation, foundInvitation);
        verify(invitationRepository, times(1)).findById(id);
    }

    @Test
    void findAllByEventId() {
        Long eventId = 1L;
        List<Invitation> invitations = Arrays.asList(invitation, new Invitation());
        when(invitationRepository.findAllByEventId(eventId)).thenReturn(invitations);

        List<Invitation> foundInvitations = invitationService.findAllByEventId(eventId);

        assertNotNull(foundInvitations);
        assertEquals(invitations.size(), foundInvitations.size());
        verify(invitationRepository, times(1)).findAllByEventId(eventId);
    }

    @Test
    void sendInvitationEmailException() throws MessagingException {
        doThrow(new MessagingException()).when(emailService).sendHtmlMessage(anyString(), anyString(), anyString());
        when(invitationRepository.save(invitation)).thenReturn(invitation);
        assertThrows(EmailSendException.class, () -> invitationService.save(invitation));
        verify(invitationRepository, times(1)).save(any(Invitation.class));
        verify(emailService, times(1)).sendHtmlMessage(anyString(), anyString(), anyString());
    }

    @Test
    void updateRsvp() {

    }
}

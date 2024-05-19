package com.benorim.evently.repository;

import com.benorim.evently.entity.Invitation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends ListCrudRepository<Invitation, Long> {
    List<Invitation> findAllByEventId(Long eventId);
}

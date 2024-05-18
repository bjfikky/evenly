package com.benorim.evently.repository;

import com.benorim.evently.entity.EventlyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventlyUserRepository extends JpaRepository<EventlyUser, Long> {
    Optional<EventlyUser> findByEmail(String email);
}

package com.benorim.evently.repository;

import com.benorim.evently.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends ListCrudRepository<Event, Long> {
    List<Event> findAll(Pageable pageable);
    List<Event> findAllByAddressState(String state);
    List<Event> findAllByAddressCity(String city);
    List<Event> findAllByAddressCityAndAddressState(String city, String state);
}

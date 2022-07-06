package com.example.demo.facade;

import com.example.demo.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface CalendarFacade {
    Optional<List<PersonDTO>> findSlotTime(Long candidateId, Integer weekNumber);
}

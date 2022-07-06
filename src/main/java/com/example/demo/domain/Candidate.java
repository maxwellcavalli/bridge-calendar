package com.example.demo.domain;


import com.example.demo.dto.PersonDTO;

import javax.persistence.Entity;

@Entity
public class Candidate extends Person {

    public static Candidate of(final PersonDTO personDTO) {
        Candidate candidate = new Candidate();
        candidate.setId(personDTO.getId());
        candidate.setName(personDTO.getName());
        candidate.setCalendarAvailabilities(
                CalendarAvailability.listOf(personDTO.getCalendar())
        );

        return candidate;
    }

}

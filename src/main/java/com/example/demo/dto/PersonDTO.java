package com.example.demo.dto;

import com.example.demo.domain.Person;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.List;

@Data
public class PersonDTO {

    private Long id;

    @NotBlank(message = "Provide name")
    private String name;

    @NotNull(message = "Provide calendar information")
    @Valid
    private LinkedHashSet<CalendarDTO> calendar;

    private List<SlotTimeDTO> freeSlotTime;

    public static PersonDTO of(final Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setName(person.getName());
        personDTO.setCalendar(CalendarDTO.listOf(person.getCalendarAvailabilities()));

        return personDTO;
    }

    public static PersonDTO simpleOf(final Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setName(person.getName());

        return personDTO;
    }


}

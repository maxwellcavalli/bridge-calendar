package com.example.demo.dto;

import com.example.demo.domain.Person;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.List;

@Data
@Builder
public class PersonDTO {

    private Long id;

    @NotBlank(message = "Provide name")
    private String name;

    @NotNull(message = "Provide calendar information")
    @Valid
    private LinkedHashSet<CalendarDTO> calendar;

    private List<SlotTimeDTO> freeSlotTime;

    public static PersonDTO of(final Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .calendar(CalendarDTO.listOf(person.getCalendarAvailabilities()))
                .build();
    }

    public static PersonDTO simpleOf(final Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .build();
    }


}

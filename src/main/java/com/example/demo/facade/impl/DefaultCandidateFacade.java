package com.example.demo.facade.impl;

import com.example.demo.domain.CalendarAvailability;
import com.example.demo.domain.Candidate;
import com.example.demo.dto.CalendarDTO;
import com.example.demo.dto.PersonDTO;
import com.example.demo.facade.CandidateFacade;
import com.example.demo.service.CandidateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component
public class DefaultCandidateFacade implements CandidateFacade {

    private final CandidateService candidateService;

    @Override
    public Optional<PersonDTO> create(final PersonDTO personDTO) {
        log.debug("Creating Candidate {}", personDTO);

        Candidate candidate = Candidate.of(personDTO);

        Candidate created = candidateService.create(candidate);

        log.debug("Candidate created {}", created);

        return Optional.of(PersonDTO.of(created));
    }

    @Override
    public Page<PersonDTO> list(final Pageable pageable) {
        Page<Candidate> list = candidateService.list(pageable);

        return new PageImpl<>(list.map(PersonDTO::simpleOf).stream()
                .toList(),
                pageable,
                list.getTotalElements());
    }

    @Override
    public Optional<PersonDTO> get(final Long id) {
        return candidateService.get(id)
                .map(PersonDTO::of);
    }

    @Override
    public Optional<PersonDTO> patch(final Long id, final PersonDTO personDTO) {
        return candidateService.get(id)
                .map(employee -> {
                    Map<Integer, List<CalendarDTO>> weekGroup = personDTO.getCalendar().stream()
                            .collect(Collectors.groupingBy(CalendarDTO::getWeekNumber));

                    for (Map.Entry<Integer, List<CalendarDTO>> groupEntry : weekGroup.entrySet()) {

                        Map<DayOfWeek, List<CalendarDTO>> collect = groupEntry.getValue().stream().collect(Collectors.groupingBy(CalendarDTO::getDayOfWeek));
                        for (Map.Entry<DayOfWeek, List<CalendarDTO>> dayOfWeekListEntry : collect.entrySet()) {

                            employee.getCalendarAvailabilities().
                                    removeIf(calendarAvailability -> calendarAvailability.getWeekNumber() == groupEntry.getKey()
                                            && calendarAvailability.getDayOfWeek().equals(dayOfWeekListEntry.getKey()));

                            Set<CalendarAvailability> calendarAvailabilities = CalendarAvailability.listOf(new LinkedHashSet<>(dayOfWeekListEntry.getValue()));

                            employee.getCalendarAvailabilities().addAll(calendarAvailabilities);
                        }
                    }

                    Candidate updated = candidateService.update(id, employee);
                    updated.setId(id);
                    return PersonDTO.of(updated);
                });
    }

    @Override
    public Optional<PersonDTO> put(final Long id, final PersonDTO personDTO) {
        return candidateService.get(id)
                .map(employee -> {
                    employee.getCalendarAvailabilities().clear();

                    Candidate toUpdate = Candidate.of(personDTO);

                    employee.setId(id);
                    employee.setName(personDTO.getName());
                    employee.setCalendarAvailabilities(toUpdate.getCalendarAvailabilities());
                    employee.getCalendarAvailabilities().forEach(calendarAvailability -> calendarAvailability.setPerson(employee));

                    Candidate updated = candidateService.update(id, employee);
                    updated.setId(id);
                    return PersonDTO.of(updated);
                });
    }


}

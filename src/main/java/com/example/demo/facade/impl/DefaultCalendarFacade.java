package com.example.demo.facade.impl;

import com.example.demo.domain.CalendarAvailability;
import com.example.demo.domain.Employee;
import com.example.demo.dto.PersonDTO;
import com.example.demo.dto.SlotTimeDTO;
import com.example.demo.exception.CandidateNotFoundException;
import com.example.demo.facade.CalendarFacade;
import com.example.demo.service.CandidateService;
import com.example.demo.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class DefaultCalendarFacade implements CalendarFacade {

    private CandidateService candidateService;
    private EmployeeService employeeService;

    @Override
    public Optional<List<PersonDTO>> findSlotTime(final Long candidateId, final Integer weekNumber) {
        final Optional<List<PersonDTO>> empty = Optional.empty();

        return candidateService.get(candidateId)
                .map(candidate -> {
                    List<CalendarAvailability> candidateAvailability = candidate.getCalendarAvailabilities().stream()
                            .filter(calendarAvailability -> calendarAvailability.getWeekNumber() == weekNumber)
                            .toList();

                    if (candidateAvailability.isEmpty()) {
                        return empty;
                    }

                    final List<Employee> allEmployees = employeeService.listAll()
                            .stream()
                            .filter(employee -> employee.getCalendarAvailabilities().stream()
                                    .anyMatch(calendarAvailability -> calendarAvailability.getWeekNumber() == weekNumber))
                            .toList();

                    if (allEmployees.isEmpty()) {
                        return empty;
                    }

                    Map<DayOfWeek, List<CalendarAvailability>> candidateDayOfWeek = candidateAvailability.stream()
                            .collect(Collectors.groupingBy(CalendarAvailability::getDayOfWeek));

                    List<PersonDTO> employeeFreeSlotTime = new ArrayList<>();

                    allEmployees.forEach(employee -> {
                        PersonDTO employeeDTO = PersonDTO.simpleOf(employee);
                        employeeDTO.setFreeSlotTime(new ArrayList<>());

                        candidateDayOfWeek.keySet().stream().sorted().toList().forEach(dayOfWeek ->
                                candidateDayOfWeek.get(dayOfWeek).forEach(candidateTime ->
                                        employee.getCalendarAvailabilities().stream()
                                                .filter(calendarAvailability -> calendarAvailability.getDayOfWeek().equals(candidateTime.getDayOfWeek()))
                                                .filter(calendarAvailability -> calendarAvailability.getStartTime().getHour() == candidateTime.getStartTime().getHour())
                                                .findFirst()
                                                .ifPresent(calendarAvailability -> {

                                                    final SlotTimeDTO slotTimeDTO = SlotTimeDTO.builder()
                                                            .dayOfWeek(dayOfWeek)
                                                            .start(calendarAvailability.getStartTime())
                                                            .end(calendarAvailability.getEndTime())
                                                            .build();


                                                    employeeDTO.getFreeSlotTime().add(slotTimeDTO);
                                                })));

                        employeeFreeSlotTime.add(employeeDTO);
                    });

                    employeeFreeSlotTime.removeIf(personDTO -> personDTO.getFreeSlotTime().isEmpty());
                    return Optional.of(employeeFreeSlotTime);
                })
                .orElseThrow(() -> new CandidateNotFoundException(String.valueOf(candidateId)));
    }


}

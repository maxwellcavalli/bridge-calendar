package com.example.demo.facade.impl;

import com.example.demo.domain.CalendarAvailability;
import com.example.demo.domain.Employee;
import com.example.demo.dto.CalendarDTO;
import com.example.demo.dto.PersonDTO;
import com.example.demo.facade.EmployeeFacade;
import com.example.demo.service.EmployeeService;
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
public class DefaultEmployeeFacade implements EmployeeFacade {

    private final EmployeeService employeeService;

    @Override
    public Optional<PersonDTO> create(final PersonDTO personDTO) {
        log.debug("Creating Employee {}", personDTO);

        Employee employee = Employee.of(personDTO);

        Employee created = employeeService.create(employee);

        log.debug("Employee created {}", created);

        return Optional.of(PersonDTO.of(created));
    }

    @Override
    public Page<PersonDTO> list(final Pageable pageable) {
        Page<Employee> list = employeeService.list(pageable);

        return new PageImpl<>(list.map(PersonDTO::simpleOf).stream()
                .toList(),
                pageable,
                list.getTotalElements());
    }

    @Override
    public Optional<PersonDTO> get(final Long id) {
        return employeeService.get(id)
                .map(PersonDTO::of);
    }

    @Override
    public Optional<PersonDTO> patch(final Long id, final PersonDTO personDTO) {
        return employeeService.get(id)
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

                    Employee updated = employeeService.update(id, employee);
                    updated.setId(id);
                    return PersonDTO.of(updated);
                });
    }

    @Override
    public Optional<PersonDTO> put(final Long id, final PersonDTO personDTO) {
        return employeeService.get(id)
                .map(employee -> {
                    employee.getCalendarAvailabilities().clear();

                    Employee toUpdate = Employee.of(personDTO);

                    employee.setId(id);
                    employee.setName(personDTO.getName());
                    employee.setCalendarAvailabilities(toUpdate.getCalendarAvailabilities());
                    employee.getCalendarAvailabilities().forEach(calendarAvailability -> calendarAvailability.setPerson(employee));

                    Employee updated = employeeService.update(id, employee);
                    updated.setId(id);
                    return PersonDTO.of(updated);
                });
    }


}

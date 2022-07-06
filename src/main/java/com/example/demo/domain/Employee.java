package com.example.demo.domain;


import com.example.demo.dto.PersonDTO;

import javax.persistence.Entity;

@Entity
public class Employee extends Person {

    public static Employee simpleOf(final PersonDTO personDTO) {
        Employee employee = new Employee();
        employee.setId(personDTO.getId());
        employee.setName(personDTO.getName());
        return employee;

    }

    public static Employee of(final PersonDTO personDTO) {
        Employee employee = new Employee();
        employee.setId(personDTO.getId());
        employee.setName(personDTO.getName());
        employee.setCalendarAvailabilities(
                CalendarAvailability.listOf(personDTO.getCalendar())
        );

        employee.getCalendarAvailabilities().forEach(calendarAvailability ->
                calendarAvailability.setPerson(employee));

        return employee;
    }


}

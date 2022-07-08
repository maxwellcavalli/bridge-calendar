package com.example.demo.service.impl;

import com.example.demo.domain.CalendarAvailability;
import com.example.demo.domain.Employee;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DefaultEmployeeServiceTest {

    @InjectMocks
    DefaultEmployeeService defaultEmployeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    void givenValidEmployee_whenCreate_whenReturnCreatedData(){
        Employee mockEmployee = mockEmployee();

        Mockito.when(employeeRepository.save(any())).thenReturn(mockEmployee);

        Employee returnValue = assertDoesNotThrow(() -> defaultEmployeeService.create(mockEmployee));
        assertNotNull(returnValue.getId());
    }

    @Test
    void givenExistingEmployeeAndValidEmployeeData_whenUpdate_thenReturnUpdatedData(){
        Employee mockEmployee = mockEmployee();
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.of(mockEmployee));
        Mockito.when(employeeRepository.save(any())).thenReturn(mockEmployee);

        Employee returnValue = assertDoesNotThrow(() -> defaultEmployeeService.update(1L
                , mockEmployee));
        assertNotNull(returnValue);
    }

    @Test
    void givenNonExistingEmployee_whenUpdate_thenThrowEmployeeNotFoundException(){
        Employee mockEmployee = mockEmployee();
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> defaultEmployeeService.update(1L, mockEmployee));
    }

    @Test
    void givenExistingEmployee_whenDelete_thenDoesntThrowException(){
        Employee mockEmployee = mockEmployee();
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.of(mockEmployee));
        Mockito.doNothing().when(employeeRepository).delete(any());

        assertDoesNotThrow(() -> defaultEmployeeService.delete(1L));
    }

    @Test
    void givenNonExistingEmployee_whenDelete_thenThrowEmployeeNotFoundException(){
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> defaultEmployeeService.delete(1L));
    }


    @Test
    void givenExistingEmployee_whenGet_thenDoesntThrowException(){
        Employee mockEmployee = mockEmployee();
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.of(mockEmployee));

        Optional<Employee> returnValue = assertDoesNotThrow(() -> defaultEmployeeService.get(1L));
        assertFalse(returnValue.isEmpty());
    }

    @Test
    void givenNonExistingEmployee_whenGet_thenThrowEmployeeNotFoundException(){
        Mockito.when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, ()  -> defaultEmployeeService.get(1L));
    }

    @Test
    void givenValidPageable_whenList_thenReturnPagedData(){
        Mockito.when(employeeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(mockEmployee())));

        Page<Employee> returnValue = assertDoesNotThrow(() -> defaultEmployeeService.list(PageRequest.of(0, 10)));

        assertNotNull(returnValue);
        assertTrue(returnValue.getTotalElements() > 0);
    }

    private Employee mockEmployee() {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(123L);
        mockEmployee.setName("Mock Name");
        mockEmployee.setCalendarAvailabilities(new LinkedHashSet<>());

        CalendarAvailability calendarAvailability = new CalendarAvailability();
        calendarAvailability.setStartTime(LocalTime.now());
        calendarAvailability.setEndTime(LocalTime.now());
        calendarAvailability.setWeekNumber(1);
        calendarAvailability.setDayOfWeek(DayOfWeek.FRIDAY);
        calendarAvailability.setPerson(mockEmployee);
        mockEmployee.getCalendarAvailabilities().add(calendarAvailability);
        return mockEmployee;
    }


}
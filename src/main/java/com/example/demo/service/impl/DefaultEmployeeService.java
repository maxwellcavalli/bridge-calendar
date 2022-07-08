package com.example.demo.service.impl;

import com.example.demo.domain.Employee;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee create(final Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(final Long id, final Employee employee) {
        return employeeRepository.findById(id)
                .map(db -> {
                    db.setName(employee.getName());
                    db.setCalendarAvailabilities(employee.getCalendarAvailabilities());
                    return employeeRepository.save(db);
                }).orElseThrow(() -> new EmployeeNotFoundException(String.valueOf(id)));
    }

    @Override
    public void delete(final long id) {
        employeeRepository.findById(id)
                .map(employee -> {
                    employeeRepository.delete(employee);
                    return true;
                }).orElseThrow(() -> new EmployeeNotFoundException(String.valueOf(id)));
    }

    @Override
    public Optional<Employee> get(long id) {
        return Optional.ofNullable(employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(String.valueOf(id))));
    }

    @Override
    public Page<Employee> list(final Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public List<Employee> listAll() {
        final List<Employee> result = new ArrayList<>();

        employeeRepository.findAll()
                .forEach(result::add);

        return result;
    }

}

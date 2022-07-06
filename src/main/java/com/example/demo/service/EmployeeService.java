package com.example.demo.service;

import com.example.demo.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee create(Employee employee);

    Employee update(Long id, Employee employee);

    void delete(long id);

    Optional<Employee> get(long id);

    Page<Employee> list(final Pageable pageable);

    List<Employee> listAll();
}

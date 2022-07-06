package com.example.demo.exception;

public class EmployeeNotFoundException extends DataNotFoundException {

    public EmployeeNotFoundException(String message) {
        super("Employee not found " + message);
    }
}

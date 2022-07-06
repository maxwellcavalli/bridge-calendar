package com.example.demo.exception;

public class CalendarException extends RuntimeException {

    public CalendarException() {
        super("The calendar has invalid start date or end time. Only accepted full hour (09:00, 10:00, etc");
    }
}

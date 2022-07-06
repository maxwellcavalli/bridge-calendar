package com.example.demo.exception;

public class CalendarBusinnessHourException extends RuntimeException {

    public CalendarBusinnessHourException() {
        super("The calendar has invalid start date or end time. You need to use business time (09AM - 5PM) ");
    }
}

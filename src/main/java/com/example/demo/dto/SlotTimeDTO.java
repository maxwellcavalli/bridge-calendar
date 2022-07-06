package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class SlotTimeDTO {

    private DayOfWeek dayOfWeek;

    private LocalTime start;

    private LocalTime end;

}

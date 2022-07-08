package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class PeriodDTO {

    @NotNull
    private LocalTime start;

    @NotNull
    private LocalTime end;

}

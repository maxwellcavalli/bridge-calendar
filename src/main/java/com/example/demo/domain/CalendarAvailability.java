package com.example.demo.domain;

import com.example.demo.dto.CalendarDTO;
import com.example.demo.dto.PeriodDTO;
import com.example.demo.exception.CalendarBusinnessHourException;
import com.example.demo.exception.CalendarException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CalendarAvailability")
@Data
@EqualsAndHashCode(exclude="person")
public class CalendarAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Person person;

    @NotNull
    private int weekNumber;

    @NotNull
    @Column(name = "dayOfWeek")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @NotNull
    @Column(name = "startTime")
    private LocalTime startTime;

    @NotNull
    @Column(name = "endTime")
    private LocalTime endTime;


    public static Set<CalendarAvailability> listOf(final Set<CalendarDTO> calendarDTOs) {
        final Set<CalendarAvailability> list = new LinkedHashSet<>();

        calendarDTOs.forEach(calendarDTO -> {
            if (calendarDTO.getMorning() != null) {
                calendarDTO.getMorning().
                        stream()
                        .map(periodDTO -> convertPeriodToCalendar(calendarDTO, periodDTO))
                        .forEach(list::addAll);
            }

            if (calendarDTO.getAfternoon() != null) {
                calendarDTO.getAfternoon().
                        stream()
                        .map(periodDTO -> convertPeriodToCalendar(calendarDTO, periodDTO))
                        .forEach(list::addAll);
            }
        });

        return list;
    }

    private static List<CalendarAvailability> convertPeriodToCalendar(final CalendarDTO calendarDTO, final PeriodDTO periodDTO) {

        final List<CalendarAvailability> list = new ArrayList<>();
        if (periodDTO.getStart().getHour() < 9 || periodDTO.getEnd().getHour() > 18) {
            throw new CalendarBusinnessHourException();
        }

        if (periodDTO.getStart().getMinute() != 0 || periodDTO.getEnd().getMinute() != 0) {
            throw new CalendarException();
        }

        LocalTime localTime = periodDTO.getStart();
        while (localTime.getHour() < periodDTO.getEnd().getHour()) {

            CalendarAvailability calendarAvailability = new CalendarAvailability();
            calendarAvailability.setDayOfWeek(calendarDTO.getDayOfWeek());
            calendarAvailability.setWeekNumber(calendarDTO.getWeekNumber());
            calendarAvailability.setStartTime(localTime);
            calendarAvailability.setEndTime(localTime.plusHours(1));

            list.add(calendarAvailability);
            localTime = localTime.plusHours(1);
        }

        return list;
    }
}

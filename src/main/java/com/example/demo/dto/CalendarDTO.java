package com.example.demo.dto;

import com.example.demo.domain.CalendarAvailability;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Data
public class CalendarDTO {

    @NotNull(message = "weekNumber cannot be null")
    @Min(0)
    @Max(52)
    private int weekNumber;

    @NotNull(message = "dayOfWeek cannot be null")
    private DayOfWeek dayOfWeek;

    private Set<PeriodDTO> morning;

    private Set<PeriodDTO> afternoon;

    public static LinkedHashSet<CalendarDTO> listOf(final Set<CalendarAvailability> calendarAvailabilities) {
        LinkedHashSet<CalendarDTO> returnList = new LinkedHashSet<>();

        Map<Integer, List<CalendarAvailability>> weekData = calendarAvailabilities.stream()
                .collect(Collectors.groupingBy(CalendarAvailability::getWeekNumber));

        for (Map.Entry<Integer, List<CalendarAvailability>> weekEntry : weekData.entrySet()) {

            processWeekData(returnList, weekEntry);
        }

        return returnList;
    }

    private static void processWeekData(Set<CalendarDTO> returnList, Map.Entry<Integer, List<CalendarAvailability>> weekEntry) {
        Map<DayOfWeek, List<CalendarAvailability>> dayOfWeekGroup = weekEntry.getValue().stream()
                .collect(Collectors.groupingBy(CalendarAvailability::getDayOfWeek));

        for (DayOfWeek dayOfWeek : dayOfWeekGroup.keySet().stream().sorted().toList()) {
            List<CalendarAvailability> value = dayOfWeekGroup.get(dayOfWeek);

            List<CalendarAvailability> hours = value.stream()
                    .sorted(Comparator.comparing(CalendarAvailability::getStartTime))
                    .toList();

            processDayOfWeekData(returnList, weekEntry.getKey(), dayOfWeek, hours);
        }
    }

    private static void processDayOfWeekData(Set<CalendarDTO> returnList,
                                             Integer weekNumber,
                                             DayOfWeek dayOfWeek,
                                             List<CalendarAvailability> hours) {
        int groupIndex = 0;
        Map<Integer, List<CalendarAvailability>> gapGroup = extractDayPeriodTime(hours, groupIndex);

        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setWeekNumber(weekNumber);
        calendarDTO.setDayOfWeek(dayOfWeek);
        calendarDTO.setAfternoon(new LinkedHashSet<>());
        calendarDTO.setMorning(new LinkedHashSet<>());

        for (Map.Entry<Integer, List<CalendarAvailability>> integerListEntry : gapGroup.entrySet()) {

            LocalTime startTime = integerListEntry.getValue().get(0).getStartTime();
            LocalTime endTime = integerListEntry.getValue().get(integerListEntry.getValue().size() - 1).getEndTime();

            PeriodDTO periodDTO = new PeriodDTO();
            periodDTO.setStart(startTime);
            periodDTO.setEnd(endTime);

            if (endTime.getHour() <= 12) {
                calendarDTO.getMorning().add(periodDTO);
            } else {
                calendarDTO.getAfternoon().add(periodDTO);
            }
        }

        returnList.add(calendarDTO);
    }

    private static Map<Integer, List<CalendarAvailability>> extractDayPeriodTime(List<CalendarAvailability> hours, int groupIndex) {
        Map<Integer, List<CalendarAvailability>> gapGroup = new HashMap<>();

        boolean afternoon = false;
        for (int i = 0; i < hours.size(); i++) {
            if (i + 1 < hours.size()) {

                if (hours.get(i).getStartTime().getHour() > 11 && !afternoon) {
                    afternoon = true;
                    groupIndex++;
                } else {
                    int diff = hours.get(i + 1).getStartTime().getHour() - hours.get(i).getStartTime().getHour();
                    if (diff > 1) {
                        groupIndex++;
                    }
                }
            }

            List<CalendarAvailability> tmp = gapGroup.get(groupIndex);
            if (tmp == null) {
                tmp = new ArrayList<>();
            }
            tmp.add(hours.get(i));

            gapGroup.put(groupIndex, tmp);
        }
        return gapGroup;
    }

}

package com.example.demo.controller;


import com.example.demo.facade.CalendarFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/calendars")
public class CalendarController {

    private CalendarFacade calendarFacade;

    @GetMapping(value = "/{id}/{weekNumber}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> get(@PathVariable Long id, @PathVariable Integer weekNumber) {
        return calendarFacade.findSlotTime(id, weekNumber)
                .map(personDTOS -> new ResponseEntity(personDTOS, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }


}

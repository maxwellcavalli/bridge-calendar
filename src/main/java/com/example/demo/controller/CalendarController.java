package com.example.demo.controller;


import com.example.demo.facade.CalendarFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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

    @ApiOperation(value = "Calendar Free Slots")
    @ApiParam
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee List returned", response = Page.class),
            @ApiResponse(code = 500, message = "Something wrong happened"),
    })
    @GetMapping(value = "/{id}/{weekNumber}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> get(
            @ApiParam(
                    name = "id",
                    type = "String",
                    value = "Candidate ID",
                    example = "123456",
                    required = true)
            @PathVariable Long id,

            @ApiParam(
                    name = "weekNumber",
                    type = "Integer",
                    value = "Number of week",
                    example = "27",
                    required = true)
            @PathVariable Integer weekNumber
    ) {
        return calendarFacade.findSlotTime(id, weekNumber)
                .map(personDTOS -> new ResponseEntity(personDTOS, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }


}

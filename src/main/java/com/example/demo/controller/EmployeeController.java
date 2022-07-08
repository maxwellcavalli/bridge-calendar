package com.example.demo.controller;


import com.example.demo.domain.Candidate;
import com.example.demo.domain.Employee;
import com.example.demo.dto.PersonDTO;
import com.example.demo.facade.EmployeeFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    private EmployeeFacade employeeFacade;

    @ApiOperation(value = "Create new Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee created", responseHeaders = {@ResponseHeader(name = "Location", description = "Created ID")}),
            @ApiResponse(code = 500, message = "Something wrong happened"),
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> save(@Valid @RequestBody final PersonDTO employee) {
        return employeeFacade.create(employee)
                .map(created -> ResponseEntity.created(URI.create(created.getId().toString())).build())
                .orElse(ResponseEntity.badRequest().build());
    }

    @ApiOperation(value = "Return simple Employee List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee List returned", response = Page.class),
            @ApiResponse(code = 500, message = "Something wrong happened"),
    })
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> list(final Pageable pageable) {
        return new ResponseEntity<>(employeeFacade.list(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Return a specific Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Employee Data", response = Employee.class),
            @ApiResponse(code = 404, message = "Candidate not found"),
    })
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> get(@PathVariable Long id) {
        return employeeFacade.get(id)
                .map(personDTO -> new ResponseEntity(personDTO, HttpStatus.OK) )
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Patch Employee calendar info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee was patched", response = Employee.class),
            @ApiResponse(code = 404, message = "Employee not found"),
            @ApiResponse(code = 500, message = "Something wrong happened"),
    })
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> patch(@PathVariable Long id, @Valid @RequestBody final PersonDTO employee) {
        return employeeFacade.patch(id, employee)
                .map(personDTO -> new ResponseEntity(personDTO, HttpStatus.OK) )
                .orElse(ResponseEntity.notFound().build());
    }


    @ApiOperation(value = "Update Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee was updated", response = Employee.class),
            @ApiResponse(code = 404, message = "Employee not found"),
            @ApiResponse(code = 500, message = "Something wrong happened"),
    })
    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody final PersonDTO employee) {
        return employeeFacade.put(id, employee)
                .map(personDTO -> new ResponseEntity(personDTO, HttpStatus.OK) )
                .orElse(ResponseEntity.notFound().build());
    }
}

package com.example.demo.controller;


import com.example.demo.dto.PersonDTO;
import com.example.demo.facade.CandidateFacade;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@RestController
@RequestMapping("/v1/candidates")
public class CandidateController {

    private CandidateFacade candidateFacade;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> save(@Valid @RequestBody final PersonDTO person) {
        return candidateFacade.create(person)
                .map(created -> new ResponseEntity(created, HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> list(final Pageable pageable) {
        return new ResponseEntity<>(candidateFacade.list(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> get(@PathVariable Long id) {
        return candidateFacade.get(id)
                .map(personDTO -> new ResponseEntity(personDTO, HttpStatus.OK) )
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> patch(@PathVariable Long id, @Valid @RequestBody final PersonDTO employee) {
        return candidateFacade.patch(id, employee)
                .map(personDTO -> new ResponseEntity(personDTO, HttpStatus.OK) )
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody final PersonDTO employee) {
        return candidateFacade.put(id, employee)
                .map(personDTO -> new ResponseEntity(personDTO, HttpStatus.OK) )
                .orElse(ResponseEntity.notFound().build());
    }
}

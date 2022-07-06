package com.example.demo.facade;

import com.example.demo.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeFacade {
    Optional<PersonDTO> create(PersonDTO personDTO);

    Page<PersonDTO> list(Pageable pageable);

    Optional<PersonDTO> get(Long id);

    Optional<PersonDTO> patch(Long id, PersonDTO personDTO);

    Optional<PersonDTO> put(Long id, PersonDTO personDTO);
}

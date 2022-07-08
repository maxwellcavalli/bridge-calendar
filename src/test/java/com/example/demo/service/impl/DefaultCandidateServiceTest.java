package com.example.demo.service.impl;

import com.example.demo.domain.CalendarAvailability;
import com.example.demo.domain.Candidate;
import com.example.demo.exception.CandidateNotFoundException;
import com.example.demo.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DefaultCandidateServiceTest {

    @InjectMocks
    DefaultCandidateService defaultCandidateService;

    @Mock
    CandidateRepository candidateRepository;

    @Test
    void givenValidCandidate_whenCreate_whenReturnCreatedData(){
        Candidate mockCandidate = mockCandidate();

        Mockito.when(candidateRepository.save(any())).thenReturn(mockCandidate);

        Candidate returnValue = assertDoesNotThrow(() -> defaultCandidateService.create(mockCandidate));
        assertNotNull(returnValue.getId());
    }

    @Test
    void givenExistingCandidateAndValidCandidateData_whenUpdate_thenReturnUpdatedData(){
        Candidate mockCandidate = mockCandidate();
        Mockito.when(candidateRepository.findById(any())).thenReturn(Optional.of(mockCandidate));
        Mockito.when(candidateRepository.save(any())).thenReturn(mockCandidate);

        Candidate returnValue = assertDoesNotThrow(() -> defaultCandidateService.update(1L, mockCandidate));
        assertNotNull(returnValue);
    }

    @Test
    void givenNonExistingCandidate_whenUpdate_thenThrowCandidateNotFoundException(){
        Candidate mockCandidate = mockCandidate();
        Mockito.when(candidateRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, () -> defaultCandidateService.update(1L, mockCandidate));
    }

    @Test
    void givenExistingCandidate_whenDelete_thenDoesntThrowException(){
        Candidate mockCandidate = mockCandidate();
        Mockito.when(candidateRepository.findById(any())).thenReturn(Optional.of(mockCandidate));
        Mockito.doNothing().when(candidateRepository).delete(any());

        assertDoesNotThrow(() -> defaultCandidateService.delete(1L));
    }

    @Test
    void givenNonExistingCandidate_whenDelete_thenThrowCandidateNotFoundException(){
        Mockito.when(candidateRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, () -> defaultCandidateService.delete(1L));
    }


    @Test
    void givenExistingCandidate_whenGet_thenDoesntThrowException(){
        Candidate mockCandidate = mockCandidate();
        Mockito.when(candidateRepository.findById(any())).thenReturn(Optional.of(mockCandidate));

        Optional<Candidate> returnValue = assertDoesNotThrow(() -> defaultCandidateService.get(1L));
        assertFalse(returnValue.isEmpty());
    }

    @Test
    void givenNonExistingCandidate_whenGet_thenThrowCandidateNotFoundException(){
        Mockito.when(candidateRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, ()  -> defaultCandidateService.get(1L));
    }

    @Test
    void givenValidPageable_whenList_thenReturnPagedData(){
        Mockito.when(candidateRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(mockCandidate())));

        Page<Candidate> returnValue = assertDoesNotThrow(() -> defaultCandidateService.list(PageRequest.of(0, 10)));

        assertNotNull(returnValue);
        assertTrue(returnValue.getTotalElements() > 0);
    }

    private Candidate mockCandidate() {
        Candidate mockCandidate = new Candidate();
        mockCandidate.setId(123L);
        mockCandidate.setName("Mock Name");
        mockCandidate.setCalendarAvailabilities(new LinkedHashSet<>());

        CalendarAvailability calendarAvailability = new CalendarAvailability();
        calendarAvailability.setStartTime(LocalTime.now());
        calendarAvailability.setEndTime(LocalTime.now());
        calendarAvailability.setWeekNumber(1);
        calendarAvailability.setDayOfWeek(DayOfWeek.FRIDAY);
        calendarAvailability.setPerson(mockCandidate);
        mockCandidate.getCalendarAvailabilities().add(calendarAvailability);
        return mockCandidate;
    }



}
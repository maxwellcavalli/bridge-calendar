package com.example.demo.domain;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person",  cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy("id")
    private Set<CalendarAvailability> calendarAvailabilities;


    public void setCalendarAvailabilities(Set<CalendarAvailability> calendarAvailabilities) {
        if (this.calendarAvailabilities == null){
            this.calendarAvailabilities = new LinkedHashSet<>();
        }
        this.calendarAvailabilities.addAll(calendarAvailabilities);
        for (CalendarAvailability child: calendarAvailabilities){
            child.setPerson(this);
        }
    }


}

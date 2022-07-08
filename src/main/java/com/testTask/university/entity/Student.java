package com.testTask.university.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String firstName;

    @Column
    private String LastName;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "schedule.id")
    @ToString.Exclude
    private Schedule schedule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && Objects.equals(schedule, student.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schedule);
    }
}

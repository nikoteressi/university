package com.testTask.university.entity;

import javax.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id == schedule.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

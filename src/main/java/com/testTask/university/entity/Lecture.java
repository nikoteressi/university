package com.testTask.university.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String lectureName;

    @Column
    private Date date;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "schedule.id")
    @ToString.Exclude
    private Schedule schedule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return id == lecture.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

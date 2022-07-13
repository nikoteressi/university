package com.testTask.university.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String date;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    Group group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(date, schedule.date) && Objects.equals(group, schedule.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, group);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", group=" + group +
                '}';
    }
}

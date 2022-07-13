package com.testTask.university.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @Column
    private String date;

    @ManyToOne
    @JoinColumn(name = "audience_id")
    private Audience audience;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return name.equals(lecture.name)
                && date.equals(lecture.date)
                && Objects.equals(audience, lecture.audience)
                && Objects.equals(group, lecture.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, audience, group);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", audience=" + audience +
                ", group=" + group +
                '}';
    }
}

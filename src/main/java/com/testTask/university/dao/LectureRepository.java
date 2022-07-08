package com.testTask.university.dao;

import com.testTask.university.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}

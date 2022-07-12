package com.testTask.university.dao;

import com.testTask.university.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findAllByGroup_NumberAndAndDate(int groupNumber, String date);
    Lecture findByNameAndDate(String lectureName, String date);

}

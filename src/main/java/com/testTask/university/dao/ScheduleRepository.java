package com.testTask.university.dao;

import com.testTask.university.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByStudentId(long StudentId);
}

package com.testTask.university.dao;

import com.testTask.university.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByGroup_IdAndDate (long groupId, String date);
    Schedule findByGroup_NumberAndDate(int groupNumber, String date);
}

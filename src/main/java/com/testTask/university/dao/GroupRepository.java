package com.testTask.university.dao;

import com.testTask.university.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByNumber(int number);
    boolean existsByNumber(int number);
}

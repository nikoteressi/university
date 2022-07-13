package com.testTask.university.dao;

import com.testTask.university.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {
    boolean existsByNumber(int number);
    Audience findByNumber(int number);
}

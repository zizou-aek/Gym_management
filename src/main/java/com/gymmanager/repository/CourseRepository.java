package com.gymmanager.repository;

import com.gymmanager.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByRoomId(Long roomId);
}
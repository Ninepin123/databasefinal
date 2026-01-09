package com.scholarship.repository;

import com.scholarship.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByAdvisorId(Integer advisorId);

    java.util.Optional<Student> findByUserId(Integer userId);
}

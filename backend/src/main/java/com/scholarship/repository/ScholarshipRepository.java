package com.scholarship.repository;

import com.scholarship.entity.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Integer> {
    List<Scholarship> findByNameContainingIgnoreCase(String keyword);
}

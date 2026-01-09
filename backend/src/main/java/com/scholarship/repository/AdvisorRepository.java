package com.scholarship.repository;

import com.scholarship.entity.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvisorRepository extends JpaRepository<Advisor, Integer> {

    @Query("SELECT a FROM Advisor a LEFT JOIN FETCH a.user")
    List<Advisor> findAllWithUser();
}

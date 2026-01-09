package com.scholarship.repository;

import com.scholarship.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findByIsActiveTrueOrderByCreatedAtDesc();
    List<Announcement> findAllByOrderByCreatedAtDesc();
    List<Announcement> findByTitleContainingIgnoreCase(String keyword);
}

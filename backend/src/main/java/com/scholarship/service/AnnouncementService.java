package com.scholarship.service;

import com.scholarship.entity.Announcement;
import com.scholarship.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Announcement> getActiveAnnouncements() {
        return announcementRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    public Announcement getAnnouncementById(Integer id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
    }

    public List<Announcement> searchAnnouncements(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveAnnouncements();
        }
        return announcementRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Announcement createAnnouncement(Announcement announcement, Integer adminId) {
        announcement.setAdminId(adminId);
        announcement.setIsActive(true);
        return announcementRepository.save(announcement);
    }

    public Announcement updateAnnouncement(Integer id, Announcement announcement) {
        Announcement existing = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));

        existing.setTitle(announcement.getTitle());
        existing.setContent(announcement.getContent());
        if (announcement.getIsActive() != null) {
            existing.setIsActive(announcement.getIsActive());
        }

        return announcementRepository.save(existing);
    }

    public void deleteAnnouncement(Integer id) {
        if (!announcementRepository.existsById(id)) {
            throw new RuntimeException("Announcement not found with id: " + id);
        }
        announcementRepository.deleteById(id);
    }

    public Announcement toggleActive(Integer id) {
        Announcement existing = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        existing.setIsActive(!existing.getIsActive());
        return announcementRepository.save(existing);
    }
}

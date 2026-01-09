package com.scholarship.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnnouncementID")
    private Integer announcementId;

    @Column(name = "Title", length = 255)
    private String title;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "AdminID")
    private Integer adminId;

    @Column(name = "PublishDate")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "IsActive")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AdminID", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "password", "student", "advisor", "admin", "reviewer", "hibernateLazyInitializer",
            "handler" })
    private User admin;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

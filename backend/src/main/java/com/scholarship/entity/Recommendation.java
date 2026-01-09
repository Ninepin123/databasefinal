package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendationletters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Recommendation {

    @EmbeddedId
    private RecommendationId id;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "FillDate")
    private LocalDateTime fillDate;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("advisorId")
    @JoinColumn(name = "AdvisorID")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Advisor advisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("applicationId")
    @JoinColumn(name = "ApplicationID")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private com.scholarship.entity.Application application;
}

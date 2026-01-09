package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "scholarships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Scholarship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScholarshipID")
    private Integer scholarshipId;

    @Column(name = "Deadline")
    private LocalDateTime deadline;

    @Column(name = "StartDate")
    private LocalDateTime startDate;

    @Column(name = "Conditions", columnDefinition = "TEXT")
    private String conditions;

    @Column(name = "Name", nullable = false, length = 255)
    private String name;

    @Column(name = "Quota")
    private Integer quota;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Type", length = 50)
    private String type;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "UnitID")
    private Integer unitId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UnitID", insertable = false, updatable = false)
    private SponsorUnit sponsorUnit;
}

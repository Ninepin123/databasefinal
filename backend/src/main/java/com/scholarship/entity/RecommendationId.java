package com.scholarship.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationId implements Serializable {

    @Column(name = "AdvisorID")
    private Integer advisorId;

    @Column(name = "ApplicationID")
    private Integer applicationId;
}

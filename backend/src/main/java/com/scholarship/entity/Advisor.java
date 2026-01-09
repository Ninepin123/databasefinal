package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 導師實體 - 對應 advisors 資料表
 */
@Entity
@Table(name = "advisors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Advisor {

    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "Office", length = 50)
    private String office;

    @Column(name = "Department", length = 50)
    private String department;

    @Column(name = "Title", length = 50)
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User user;
}

package com.scholarship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 使用者實體 - 對應 users 資料表
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "student", "advisor", "admin", "reviewer" })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "Account", nullable = false, unique = true, length = 50)
    private String account;

    @JsonIgnore
    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "Role", length = 20)
    private String role;

    // 關聯 - 使用 userId 作為外鍵查詢
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Student student;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Advisor advisor;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Admin admin;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Reviewer reviewer;
}

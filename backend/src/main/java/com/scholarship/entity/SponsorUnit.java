package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "sponsorunits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UnitID")
    private Integer unitId;

    @Column(name = "ContactPerson", length = 50)
    private String contactPerson;

    @Column(name = "UnitName", nullable = false, length = 100)
    private String unitName;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "Website", length = 255)
    private String website;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "AdminID")
    private Integer adminId;
}

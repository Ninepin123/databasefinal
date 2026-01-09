package com.scholarship.repository;

import com.scholarship.entity.SponsorUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorUnitRepository extends JpaRepository<SponsorUnit, Integer> {
}

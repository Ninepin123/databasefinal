package com.scholarship.service;

import com.scholarship.entity.SponsorUnit;
import com.scholarship.repository.SponsorUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorUnitService {

    @Autowired
    private SponsorUnitRepository sponsorUnitRepository;

    public List<SponsorUnit> getAllSponsorUnits() {
        return sponsorUnitRepository.findAll();
    }

    public SponsorUnit getSponsorUnitById(Integer id) {
        return sponsorUnitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SponsorUnit not found with id: " + id));
    }

    public SponsorUnit createSponsorUnit(SponsorUnit sponsorUnit) {
        return sponsorUnitRepository.save(sponsorUnit);
    }

    public SponsorUnit updateSponsorUnit(Integer id, SponsorUnit sponsorUnit) {
        SponsorUnit existing = sponsorUnitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SponsorUnit not found with id: " + id));

        existing.setUnitName(sponsorUnit.getUnitName());
        existing.setContactPerson(sponsorUnit.getContactPerson());
        existing.setEmail(sponsorUnit.getEmail());
        existing.setAddress(sponsorUnit.getAddress());
        existing.setWebsite(sponsorUnit.getWebsite());
        existing.setPhone(sponsorUnit.getPhone());
        existing.setAdminId(sponsorUnit.getAdminId());

        return sponsorUnitRepository.save(existing);
    }

    public void deleteSponsorUnit(Integer id) {
        if (!sponsorUnitRepository.existsById(id)) {
            throw new RuntimeException("SponsorUnit not found with id: " + id);
        }
        sponsorUnitRepository.deleteById(id);
    }
}

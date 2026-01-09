package com.scholarship.controller;

import com.scholarship.entity.SponsorUnit;
import com.scholarship.service.SponsorUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sponsor-units")
@CrossOrigin(origins = "*")
public class SponsorUnitController {

    @Autowired
    private SponsorUnitService sponsorUnitService;

    @Autowired
    private com.scholarship.security.JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<SponsorUnit>> getAllSponsorUnits() {
        return ResponseEntity.ok(sponsorUnitService.getAllSponsorUnits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorUnit> getSponsorUnitById(@PathVariable Integer id) {
        return ResponseEntity.ok(sponsorUnitService.getSponsorUnitById(id));
    }

    @PostMapping
    public ResponseEntity<SponsorUnit> createSponsorUnit(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SponsorUnit sponsorUnit) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(sponsorUnitService.createSponsorUnit(sponsorUnit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorUnit> updateSponsorUnit(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SponsorUnit sponsorUnit) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(sponsorUnitService.updateSponsorUnit(id, sponsorUnit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSponsorUnit(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        sponsorUnitService.deleteSponsorUnit(id);
        return ResponseEntity.ok(Map.of("message", "SponsorUnit deleted successfully"));
    }
}

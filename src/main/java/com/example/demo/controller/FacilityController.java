package com.example.demo.controller;

import com.example.demo.domain.Facility;
import com.example.demo.dto.FacilityNameDTO;
import com.example.demo.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping
    public List<FacilityNameDTO> getAllFacilityNames() {
        return facilityService.getAllFacilities().stream()
                .map(facility -> new FacilityNameDTO(facility.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/contain/{name}")
    public List<String> getFacilitiesByName(@PathVariable String name) {
        return facilityService.getFacilityNamesByNameContaining(name);
    }

    @PostMapping
    public Facility createFacility(@RequestBody Facility facility) {
        return facilityService.createFacility(facility);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> updateFacility(@PathVariable Long id, @RequestBody Facility facilityDetails) {
        Facility updatedFacility = facilityService.updateFacility(id, facilityDetails);
        return ResponseEntity.ok(updatedFacility);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }
}



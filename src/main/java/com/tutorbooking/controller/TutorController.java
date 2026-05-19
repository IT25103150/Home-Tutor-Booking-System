package com.tutorbooking.controller;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tutors")
@CrossOrigin(origins = "*")
public class TutorController {
    @Autowired
    private TutorService tutorService;

    // Get all tutors
    @GetMapping
    public ResponseEntity<List<Tutor>> getAllTutors() {
        return ResponseEntity.ok(tutorService.getAllTutors());
    }

    // Get tutor by ID
    @GetMapping("/{tutorId}")
    public ResponseEntity<?> getTutorById(@PathVariable String tutorId) {
        Tutor tutor = tutorService.getTutorById(tutorId);
        if (tutor != null) {
            return ResponseEntity.ok(tutor);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutor not found");
    }

    // Search tutors by subject
    @GetMapping("/search/subject")
    public ResponseEntity<List<Tutor>> searchBySubject(@RequestParam String subject) {
        return ResponseEntity.ok(tutorService.searchTutorsBySubject(subject));
    }

    // Search tutors by name
    @GetMapping("/search/name")
    public ResponseEntity<List<Tutor>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(tutorService.searchTutorsByName(name));
    }

    // Create tutor (admin only)
    @PostMapping
    public ResponseEntity<?> createTutor(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            String subjects = (String) request.get("subjects");
            String availability = (String) request.get("availability");
            Double hourlyRate = ((Number) request.get("hourlyRate")).doubleValue();
            String description = (String) request.get("description");

            tutorService.createTutor(name, subjects, availability, hourlyRate, description);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tutor created successfully");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Update tutor (admin only)
    @PutMapping("/{tutorId}")
    public ResponseEntity<?> updateTutor(@PathVariable String tutorId,
                                        @RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            String subjects = (String) request.get("subjects");
            String availability = (String) request.get("availability");
            Double hourlyRate = request.get("hourlyRate") != null ? 
                               ((Number) request.get("hourlyRate")).doubleValue() : null;
            String description = (String) request.get("description");

            tutorService.updateTutor(tutorId, name, subjects, availability, hourlyRate, description);
            return ResponseEntity.ok("Tutor updated successfully");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Delete tutor (admin only)
    @DeleteMapping("/{tutorId}")
    public ResponseEntity<?> deleteTutor(@PathVariable String tutorId) {
        try {
            tutorService.deleteTutor(tutorId);
            return ResponseEntity.ok("Tutor deleted successfully");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get tutor statistics
    @GetMapping("/{tutorId}/stats")
    public ResponseEntity<?> getTutorStats(@PathVariable String tutorId) {
        Tutor tutor = tutorService.getTutorById(tutorId);
        if (tutor != null) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("tutorId", tutorId);
            stats.put("name", tutor.getName());
            stats.put("averageRating", tutorService.calculateAverageRating(tutorId));
            stats.put("reviewCount", tutorService.getReviewCount(tutorId));
            stats.put("hourlyRate", tutor.getHourlyRate());
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutor not found");
    }
}

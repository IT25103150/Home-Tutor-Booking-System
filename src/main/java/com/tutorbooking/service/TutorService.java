package com.tutorbooking.service;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.repository.TutorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class TutorService {
    private final TutorRepository tutorRepository = new TutorRepository();
    private final ReviewService reviewService = new ReviewService();

    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }

    public Tutor getTutorById(String tutorId) {
        return tutorRepository.findById(tutorId);
    }

    public List<Tutor> searchTutorsBySubject(String subject) {
        return tutorRepository.findBySubject(subject);
    }

    public List<Tutor> searchTutorsByName(String name) {
        return tutorRepository.findByName(name);
    }

    public void createTutor(String name, String subjects, String availability, Double hourlyRate, 
                           String description) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Tutor name is required");
        }
        if (subjects == null || subjects.trim().isEmpty()) {
            throw new Exception("Subjects are required");
        }
        if (hourlyRate == null || hourlyRate <= 0) {
            throw new Exception("Hourly rate must be greater than 0");
        }

        Tutor tutor = new Tutor();
        tutor.setTutorId(UUID.randomUUID().toString());
        tutor.setName(name);
        tutor.setSubjects(subjects);
        tutor.setAvailability(availability != null ? availability : "Available");
        tutor.setHourlyRate(hourlyRate);
        tutor.setDescription(description != null ? description : "");
        tutor.setRating(0.0);
        tutor.setReviewCount(0);

        tutorRepository.save(tutor);
    }

    public void updateTutor(String tutorId, String name, String subjects, String availability, 
                           Double hourlyRate, String description) throws Exception {
        Tutor tutor = tutorRepository.findById(tutorId);
        if (tutor == null) {
            throw new Exception("Tutor not found");
        }

        if (name != null && !name.trim().isEmpty()) {
            tutor.setName(name);
        }
        if (subjects != null && !subjects.trim().isEmpty()) {
            tutor.setSubjects(subjects);
        }
        if (availability != null && !availability.trim().isEmpty()) {
            tutor.setAvailability(availability);
        }
        if (hourlyRate != null && hourlyRate > 0) {
            tutor.setHourlyRate(hourlyRate);
        }
        if (description != null) {
            tutor.setDescription(description);
        }

        tutorRepository.update(tutorId, tutor);
    }

    public void deleteTutor(String tutorId) throws Exception {
        if (!tutorRepository.deleteById(tutorId)) {
            throw new Exception("Failed to delete tutor");
        }
    }

    public Double calculateAverageRating(String tutorId) {
        return reviewService.getAverageRating(tutorId);
    }

    public Integer getReviewCount(String tutorId) {
        return reviewService.getReviewCount(tutorId);
    }
}

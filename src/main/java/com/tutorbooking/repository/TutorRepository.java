package com.tutorbooking.repository;

import com.tutorbooking.model.Tutor;
import com.tutorbooking.util.FileHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TutorRepository {
    private static final String FILE_NAME = "tutors.txt";

    public List<Tutor> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Tutor> tutors = new ArrayList<>();
        for (String line : lines) {
            Tutor tutor = parseTutor(line);
            if (tutor != null) {
                tutors.add(tutor);
            }
        }
        return tutors;
    }

    public Tutor findById(String tutorId) {
        return findAll().stream()
                .filter(t -> t.getTutorId().equals(tutorId))
                .findFirst()
                .orElse(null);
    }

    public List<Tutor> findBySubject(String subject) {
        return findAll().stream()
                .filter(t -> t.getSubjects().toLowerCase().contains(subject.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Tutor> findByName(String name) {
        return findAll().stream()
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void save(Tutor tutor) {
        if (tutor.isValid()) {
            FileHelper.appendLine(FILE_NAME, tutor.toString());
        }
    }

    public boolean update(String tutorId, Tutor tutor) {
        return FileHelper.updateRecordById(FILE_NAME, tutorId, tutor.toString());
    }

    public boolean deleteById(String tutorId) {
        return FileHelper.deleteRecordById(FILE_NAME, tutorId);
    }

    private Tutor parseTutor(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                Tutor tutor = new Tutor();
                tutor.setTutorId(parts[0]);
                tutor.setName(parts[1]);
                tutor.setSubjects(parts[2]);
                tutor.setRating(Double.parseDouble(parts[3]));
                tutor.setAvailability(parts[4]);
                tutor.setHourlyRate(Double.parseDouble(parts[5]));
                tutor.setDescription(parts[6]);
                return tutor;
            }
        } catch (Exception e) {
            System.err.println("Error parsing tutor line: " + line + " - " + e.getMessage());
        }
        return null;
    }
}

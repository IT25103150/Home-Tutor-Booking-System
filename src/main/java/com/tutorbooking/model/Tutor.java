package com.tutorbooking.model;

public class Tutor {
    private String tutorId;
    private String name;
    private String subjects; // comma-separated
    private Double rating; // average rating from reviews
    private String availability; // e.g., "Monday-Friday, 9AM-5PM"
    private Double hourlyRate;
    private String description;
    private Integer reviewCount;

    // Constructors
    public Tutor() {}

    public Tutor(String tutorId, String name, String subjects, Double rating, 
                 String availability, Double hourlyRate, String description) {
        this.tutorId = tutorId;
        this.name = name;
        this.subjects = subjects;
        this.rating = rating;
        this.availability = availability;
        this.hourlyRate = hourlyRate;
        this.description = description;
        this.reviewCount = 0;
    }

    // Validation
    public boolean isValid() {
        return tutorId != null && !tutorId.isEmpty() &&
               name != null && !name.isEmpty() &&
               subjects != null && !subjects.isEmpty() &&
               rating != null && rating >= 0 && rating <= 5 &&
               availability != null && !availability.isEmpty() &&
               hourlyRate != null && hourlyRate > 0;
    }

    // Getters and Setters
    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public String toString() {
        return tutorId + "|" + name + "|" + subjects + "|" + rating + "|" + availability + "|" + hourlyRate + "|" + description;
    }
}

package com.tutorbooking.repository;

import com.tutorbooking.model.Review;
import com.tutorbooking.util.FileHelper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewRepository {
    private static final String FILE_NAME = "reviews.txt";

    public List<Review> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Review> reviews = new ArrayList<>();
        for (String line : lines) {
            Review review = parseReview(line);
            if (review != null) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    public void save(Review review) {
        if (review.isValid()) {
            FileHelper.appendLine(FILE_NAME, review.toString());
        }
    }

    public boolean update(String reviewId, Review review) {
        return FileHelper.updateRecordById(FILE_NAME, reviewId, review.toString());
    }

    public boolean deleteById(String reviewId) {
        return FileHelper.deleteRecordById(FILE_NAME, reviewId);
    }

    public Review findById(String reviewId) {
        return findAll().stream()
                .filter(r -> r.getReviewId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    public List<Review> findByTutorId(String tutorId) {
        return findAll().stream()
                .filter(r -> r.getTutorId().equals(tutorId))
                .collect(Collectors.toList());
    }

    public List<Review> findByUserId(String userId) {
        return findAll().stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Review> findByBookingId(String bookingId) {
        return findAll().stream()
                .filter(r -> r.getBookingId().equals(bookingId))
                .collect(Collectors.toList());
    }

    private Review parseReview(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                Review review = new Review();
                review.setReviewId(parts[0]);
                review.setBookingId(parts[1]);
                review.setTutorId(parts[2]);
                review.setUserId(parts[3]);
                review.setRating(Integer.parseInt(parts[4]));
                review.setComment(parts[5]);
                review.setCreatedDate(LocalDate.parse(parts[6]));
                return review;
            }
        } catch (Exception e) {
            System.err.println("Error parsing review line: " + line + " - " + e.getMessage());
        }
        return null;
    }
}

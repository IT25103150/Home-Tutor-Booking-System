package com.tutorbooking.controller;

import com.tutorbooking.model.Review;
import com.tutorbooking.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // Get all reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // Get reviews by tutor
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Review>> getReviewsByTutor(@PathVariable String tutorId) {
        return ResponseEntity.ok(reviewService.getReviewsByTutor(tutorId));
    }

    // Get reviews by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    // Get review by ID
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable String reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
    }

    // Create review
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> request) {
        try {
            String bookingId = (String) request.get("bookingId");
            String tutorId = (String) request.get("tutorId");
            String userId = (String) request.get("userId");
            Integer rating = ((Number) request.get("rating")).intValue();
            String comment = (String) request.get("comment");

            Review review = reviewService.createReview(bookingId, tutorId, userId, rating, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Update review
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable String reviewId, 
                                         @RequestBody Map<String, Object> request) {
        try {
            Integer rating = ((Number) request.get("rating")).intValue();
            String comment = (String) request.get("comment");
            
            Review review = reviewService.updateReview(reviewId, rating, comment);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Delete review
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable String reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Review deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get average rating for tutor
    @GetMapping("/tutor/{tutorId}/rating")
    public ResponseEntity<?> getAverageRating(@PathVariable String tutorId) {
        Map<String, Object> response = new HashMap<>();
        response.put("averageRating", reviewService.getAverageRating(tutorId));
        response.put("reviewCount", reviewService.getReviewCount(tutorId));
        return ResponseEntity.ok(response);
    }
}

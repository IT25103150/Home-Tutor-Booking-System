package com.tutorbooking.service;

import com.tutorbooking.model.Review;
import com.tutorbooking.model.Booking;
import com.tutorbooking.repository.ReviewRepository;
import com.tutorbooking.repository.BookingRepository;
import com.tutorbooking.repository.TutorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository = new ReviewRepository();
    private final BookingRepository bookingRepository = new BookingRepository();
    private final TutorRepository tutorRepository = new TutorRepository();

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByTutor(String tutorId) {
        return reviewRepository.findByTutorId(tutorId);
    }

    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Review getReviewById(String reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public Double getAverageRating(String tutorId) {
        List<Review> reviews = getReviewsByTutor(tutorId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public Integer getReviewCount(String tutorId) {
        return getReviewsByTutor(tutorId).size();
    }

    public Review createReview(String bookingId, String tutorId, String userId, Integer rating, 
                              String comment) throws Exception {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new Exception("Rating must be between 1 and 5");
        }

        // Validate comment
        if (comment == null || comment.trim().isEmpty()) {
            throw new Exception("Comment cannot be empty");
        }

        // Verify booking exists and is completed
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found");
        }
        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new Exception("Can only review completed bookings");
        }

        // Check if user hasn't already reviewed
        List<Review> existingReviews = reviewRepository.findByBookingId(bookingId);
        if (!existingReviews.isEmpty()) {
            throw new Exception("This booking has already been reviewed");
        }

        Review review = new Review();
        review.setReviewId(UUID.randomUUID().toString());
        review.setBookingId(bookingId);
        review.setTutorId(tutorId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedDate(LocalDate.now());

        reviewRepository.save(review);

        // Update tutor's average rating
        updateTutorRating(tutorId);

        return review;
    }

    public Review updateReview(String reviewId, Integer rating, String comment) throws Exception {
        if (rating < 1 || rating > 5) {
            throw new Exception("Rating must be between 1 and 5");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new Exception("Comment cannot be empty");
        }

        Review review = reviewRepository.findById(reviewId);
        if (review == null) {
            throw new Exception("Review not found");
        }

        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.update(reviewId, review);

        // Update tutor's average rating
        updateTutorRating(review.getTutorId());
        return review;
    }

    public void deleteReview(String reviewId) throws Exception {
        Review review = reviewRepository.findById(reviewId);
        if (review == null) {
            throw new Exception("Review not found");
        }

        if (!reviewRepository.deleteById(reviewId)) {
            throw new Exception("Failed to delete review");
        }

        // Update tutor's average rating
        updateTutorRating(review.getTutorId());
    }

    private void updateTutorRating(String tutorId) {
        Double avgRating = getAverageRating(tutorId);
        var tutor = tutorRepository.findById(tutorId);
        if (tutor != null) {
            tutor.setRating(avgRating);
            tutor.setReviewCount(getReviewCount(tutorId));
            tutorRepository.update(tutorId, tutor);
        }
    }
}

package com.tutorbooking.model;

import java.time.LocalDate;

public class Review {
    private String reviewId;
    private String bookingId;
    private String tutorId;
    private String userId;
    private Integer rating; // 1-5
    private String comment;
    private LocalDate createdDate;

    // Constructors
    public Review() {}

    public Review(String reviewId, String bookingId, String tutorId, String userId, 
                  Integer rating, String comment, LocalDate createdDate) {
        this.reviewId = reviewId;
        this.bookingId = bookingId;
        this.tutorId = tutorId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdDate = createdDate;
    }

    // Validation
    public boolean isValid() {
        return reviewId != null && !reviewId.isEmpty() &&
               bookingId != null && !bookingId.isEmpty() &&
               tutorId != null && !tutorId.isEmpty() &&
               userId != null && !userId.isEmpty() &&
               rating != null && rating >= 1 && rating <= 5 &&
               comment != null && !comment.isEmpty() &&
               createdDate != null;
    }

    // Getters and Setters
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return reviewId + "|" + bookingId + "|" + tutorId + "|" + userId + "|" + rating + "|" + comment + "|" + createdDate;
    }
}

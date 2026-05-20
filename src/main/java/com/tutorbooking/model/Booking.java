package com.tutorbooking.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private String bookingId;
    private String tutorId;
    private String userId;
    private LocalDate date;
    private LocalTime time;
    private Integer duration; // in minutes
    private String subject;
    private BookingStatus status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private Double hourlyRate;
    private Double totalCost;

    public enum BookingStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    // Constructors
    public Booking() {}

    public Booking(String bookingId, String tutorId, String userId, LocalDate date, 
                   LocalTime time, Integer duration, String subject, BookingStatus status, 
                   Double hourlyRate, Double totalCost) {
        this.bookingId = bookingId;
        this.tutorId = tutorId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.subject = subject;
        this.status = status;
        this.hourlyRate = hourlyRate;
        this.totalCost = totalCost;
    }

    // Validation
    public boolean isValid() {
        return bookingId != null && !bookingId.isEmpty() &&
               tutorId != null && !tutorId.isEmpty() &&
               userId != null && !userId.isEmpty() &&
               date != null && time != null &&
               duration != null && duration > 0 &&
               subject != null && !subject.isEmpty() &&
               status != null && hourlyRate != null && hourlyRate > 0;
    }

    // Getters and Setters
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return bookingId + "|" + tutorId + "|" + userId + "|" + date + "|" + time + "|" 
               + duration + "|" + subject + "|" + status + "|" + hourlyRate + "|" + totalCost;
    }
}

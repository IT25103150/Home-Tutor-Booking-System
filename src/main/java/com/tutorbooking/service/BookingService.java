package com.tutorbooking.service;

import com.tutorbooking.model.Booking;
import com.tutorbooking.repository.BookingRepository;
import com.tutorbooking.repository.TutorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository = new BookingRepository();
    private final TutorRepository tutorRepository = new TutorRepository();

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByTutor(String tutorId) {
        return bookingRepository.findByTutorId(tutorId);
    }

    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public Booking createBooking(String tutorId, String userId, LocalDate date, LocalTime time, 
                                 Integer duration, String subject) throws Exception {
        // Validate date is not in the past
        if (date.isBefore(LocalDate.now())) {
            throw new Exception("Cannot book for a past date");
        }

        // Verify tutor exists
        if (tutorRepository.findById(tutorId) == null) {
            throw new Exception("Tutor not found");
        }

        // Get hourly rate from tutor
        Double hourlyRate = tutorRepository.findById(tutorId).getHourlyRate();
        Double totalCost = calculateTotalCost(hourlyRate, duration);

        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setTutorId(tutorId);
        booking.setUserId(userId);
        booking.setDate(date);
        booking.setTime(time);
        booking.setDuration(duration);
        booking.setSubject(subject);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setHourlyRate(hourlyRate);
        booking.setTotalCost(totalCost);

        bookingRepository.save(booking);
        return booking;
    }

    public Booking updateBookingStatus(String bookingId, Booking.BookingStatus status) throws Exception {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found");
        }
        booking.setStatus(status);
        bookingRepository.update(bookingId, booking);
        return booking;
    }

    public Booking cancelBooking(String bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found");
        }
        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            throw new Exception("Cannot cancel a completed booking");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.update(bookingId, booking);
        return booking;
    }

    public void deleteBooking(String bookingId) throws Exception {
        if (!bookingRepository.deleteById(bookingId)) {
            throw new Exception("Failed to delete booking");
        }
    }

    public Double calculateTotalCost(Double hourlyRate, Integer durationMinutes) {
        double hours = durationMinutes / 60.0;
        return hourlyRate * hours;
    }
}

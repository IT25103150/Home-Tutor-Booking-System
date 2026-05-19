package com.tutorbooking.repository;

import com.tutorbooking.model.Booking;
import com.tutorbooking.util.FileHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingRepository {
    private static final String FILE_NAME = "bookings.txt";

    public List<Booking> findAll() {
        List<String> lines = FileHelper.readAllLines(FILE_NAME);
        List<Booking> bookings = new ArrayList<>();
        for (String line : lines) {
            Booking booking = parseBooking(line);
            if (booking != null) {
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public void save(Booking booking) {
        if (booking.isValid()) {
            FileHelper.appendLine(FILE_NAME, booking.toString());
        }
    }

    public boolean update(String bookingId, Booking booking) {
        return FileHelper.updateRecordById(FILE_NAME, bookingId, booking.toString());
    }

    public boolean deleteById(String bookingId) {
        return FileHelper.deleteRecordById(FILE_NAME, bookingId);
    }

    public Booking findById(String bookingId) {
        return findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public List<Booking> findByUserId(String userId) {
        return findAll().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Booking> findByTutorId(String tutorId) {
        return findAll().stream()
                .filter(b -> b.getTutorId().equals(tutorId))
                .collect(Collectors.toList());
    }

    public List<Booking> findByStatus(Booking.BookingStatus status) {
        return findAll().stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    private Booking parseBooking(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 10) {
                Booking booking = new Booking();
                booking.setBookingId(parts[0]);
                booking.setTutorId(parts[1]);
                booking.setUserId(parts[2]);
                booking.setDate(LocalDate.parse(parts[3]));
                booking.setTime(LocalTime.parse(parts[4]));
                booking.setDuration(Integer.parseInt(parts[5]));
                booking.setSubject(parts[6]);
                booking.setStatus(Booking.BookingStatus.valueOf(parts[7]));
                booking.setHourlyRate(Double.parseDouble(parts[8]));
                booking.setTotalCost(Double.parseDouble(parts[9]));
                return booking;
            }
        } catch (Exception e) {
            System.err.println("Error parsing booking line: " + line + " - " + e.getMessage());
        }
        return null;
    }
}

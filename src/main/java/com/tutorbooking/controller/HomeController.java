package com.tutorbooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/tutors")
    public String tutorList() {
        return "tutor-list";
    }

    @GetMapping("/bookings")
    public String bookingList() {
        return "booking-list";
    }

    @GetMapping("/bookings/create")
    public String bookingCreate() {
        return "booking-create";
    }

    @GetMapping("/bookings/edit")
    public String bookingEdit() {
        return "booking-edit";
    }

    @GetMapping("/reviews")
    public String reviewList() {
        return "review-list";
    }

    @GetMapping("/reviews/create")
    public String reviewCreate() {
        return "review-create";
    }

    @GetMapping("/reviews/edit")
    public String reviewEdit() {
        return "review-edit";
    }
}

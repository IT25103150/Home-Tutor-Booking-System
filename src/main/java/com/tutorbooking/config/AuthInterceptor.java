package com.tutorbooking.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        
        // Skip check for login/register/admin/home pages, public pages, API endpoints, and static resources
        if (uri.contains("/login") || uri.contains("/users/register") || uri.contains("/tutors/register") || uri.contains("/css") || uri.contains("/js") 
            || uri.contains("/admin/login") || uri.equals("/") || uri.contains("/tutors") || uri.contains("/bookings") || uri.contains("/reviews") || uri.contains("/api")) {
            return true;
        }

        // Check for admin routes
        if (uri.contains("/admin/")) {
            if (session.getAttribute("adminUser") == null) {
                response.sendRedirect("/admin/login");
                return false;
            }
            return true;
        }

        // Check for regular user routes
        if (session.getAttribute("loggedUser") == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}

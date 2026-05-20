# HomeTutor Booking & Review System - Setup Guide

## 📋 What Was Created

### PHASE 1: Backend Models ✅
- **Booking.java** - Fields: bookingId, tutorId, userId, date, time, duration, subject, status (PENDING/CONFIRMED/COMPLETED/CANCELLED), hourlyRate, totalCost
- **Review.java** - Fields: reviewId, bookingId, tutorId, userId, rating (1-5), comment, createdDate
- **Tutor.java** - Fields: tutorId, name, subjects, rating, availability, hourlyRate, description, reviewCount

### PHASE 2: Repositories ✅
- **BookingRepository.java** - CRUD operations for bookings (findAll, findById, findByUserId, findByTutorId, findByStatus, save, update, delete)
- **ReviewRepository.java** - CRUD operations for reviews (findAll, findById, findByTutorId, findByUserId, findByBookingId, save, update, delete)
- **TutorRepository.java** - Read operations for tutors (findAll, findById, findBySubject, findByName, save, update, delete)

### PHASE 3: Services (Business Logic) ✅
- **BookingService.java** - Business logic for bookings (validate past dates, calculate costs, manage status)
- **ReviewService.java** - Business logic for reviews (validate ratings 1-5, link to completed bookings, update tutor ratings)
- **TutorService.java** - Business logic for tutors (CRUD, search by subject/name, calculate average ratings)

### PHASE 4: REST Controllers ✅
- **BookingController** (`/api/bookings`) - REST endpoints for booking management
- **ReviewController** (`/api/reviews`) - REST endpoints for review management
- **TutorController** (`/api/tutors`) - REST endpoints for tutor list and search

### PHASE 5-7: Frontend ✅

**HTML Pages:**
- **tutor-list.html** - Browse all tutors, search/filter by name and subject, book sessions
- **booking-list.html** - View user's bookings, manage status (PENDING→CONFIRMED→COMPLETED), cancel bookings
- **review-list.html** - View reviews left, edit/delete reviews, star ratings

**CSS Styling (Bootstrap 5 + Font Awesome):**
- **tutor-list.css** - Tutor cards with gradient styling, responsive grid
- **booking-list.css** - Status badges, tab navigation, action buttons
- **review-list.css** - Review cards, star rating UI, edit/delete modals

**JavaScript (API Integration):**
- **tutor-list.js** - Load tutors, filter/search, open booking modal, calculate costs
- **booking-list.js** - Fetch user bookings, update status, cancel bookings, display by status
- **review-list.js** - Load reviews, edit/delete with modals, manage star ratings

### Sample Data ✅
- **tutors.txt** - 8 dummy tutors with varied subjects, ratings, and hourly rates

---

## 🚀 Getting Started

### 1. **Start the Application**
```bash
mvn clean package -DskipTests
java -jar target/hometutor-system-0.0.1-SNAPSHOT.jar
```

### 2. **API Base URL**
```
http://localhost:8080/api
```

### 3. **Frontend Routes**
- **Tutor List:** `/tutors`
- **My Bookings:** `/bookings`
- **My Reviews:** `/reviews`

---

## 📡 REST API Endpoints

### Bookings (`/api/bookings`)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/` | Get all bookings |
| GET | `/user/{userId}` | Get user's bookings |
| GET | `/tutor/{tutorId}` | Get tutor's bookings |
| GET | `/{bookingId}` | Get booking details |
| POST | `/` | Create booking |
| PUT | `/{bookingId}/status` | Update booking status |
| PUT | `/{bookingId}/cancel` | Cancel booking |
| DELETE | `/{bookingId}` | Delete booking |

**Create Booking Request:**
```json
{
  "tutorId": "T001",
  "userId": "user123",
  "date": "2024-05-25",
  "time": "14:00",
  "duration": 60,
  "subject": "Mathematics"
}
```

### Reviews (`/api/reviews`)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/` | Get all reviews |
| GET | `/tutor/{tutorId}` | Get tutor's reviews |
| GET | `/user/{userId}` | Get user's reviews |
| GET | `/{reviewId}` | Get review details |
| POST | `/` | Create review |
| PUT | `/{reviewId}` | Update review |
| DELETE | `/{reviewId}` | Delete review |
| GET | `/tutor/{tutorId}/rating` | Get tutor's avg rating |

**Create Review Request:**
```json
{
  "bookingId": "B123",
  "tutorId": "T001",
  "userId": "user123",
  "rating": 5,
  "comment": "Excellent tutor! Very helpful and professional."
}
```

### Tutors (`/api/tutors`)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/` | Get all tutors |
| GET | `/{tutorId}` | Get tutor details |
| GET | `/search/subject?subject=Math` | Search by subject |
| GET | `/search/name?name=John` | Search by name |
| GET | `/{tutorId}/stats` | Get tutor statistics |
| POST | `/` | Create tutor (admin) |
| PUT | `/{tutorId}` | Update tutor (admin) |
| DELETE | `/{tutorId}` | Delete tutor (admin) |

---

## 📂 File Structure

```
src/main/java/com/tutorbooking/
├── model/
│   ├── Booking.java (NEW)
│   ├── Review.java (UPDATED)
│   └── Tutor.java (NEW)
├── repository/
│   ├── BookingRepository.java (UPDATED)
│   ├── ReviewRepository.java (UPDATED)
│   └── TutorRepository.java (NEW)
├── service/
│   ├── BookingService.java (UPDATED)
│   ├── ReviewService.java (UPDATED)
│   └── TutorService.java (NEW)
└── controller/
    ├── BookingController.java (UPDATED)
    ├── ReviewController.java (UPDATED)
    └── TutorController.java (NEW)

src/main/resources/
├── templates/
│   ├── tutor-list.html (NEW)
│   ├── booking-list.html (NEW)
│   └── review-list.html (NEW)
├── static/
│   ├── css/
│   │   ├── tutor-list.css (NEW)
│   │   ├── booking-list.css (NEW)
│   │   └── review-list.css (NEW)
│   └── js/
│       ├── tutor-list.js (NEW)
│       ├── booking-list.js (NEW)
│       └── review-list.js (NEW)
└── data/
    └── tutors.txt (NEW - with 8 sample tutors)
```

---

## 🔑 Key Features

✅ **Booking Management**
- Create bookings with date, time, duration validation
- Prevent booking in past dates
- Track booking status (PENDING → CONFIRMED → COMPLETED → CANCELLED)
- Automatic cost calculation based on tutor's hourly rate
- Cancel bookings with status validation

✅ **Review Management**
- Leave reviews only for completed bookings
- Rate tutors 1-5 stars with comments
- Edit/delete own reviews
- Automatic tutor rating updates

✅ **Tutor List**
- Browse all tutors with subject filtering
- Search by name or subject
- Display tutor info: name, subjects, rating, availability, hourly rate
- Quick booking from tutor list

✅ **Responsive UI**
- Bootstrap 5 + Font Awesome styling
- Professional gradient headers
- Mobile-friendly cards and modals
- Status badges and icons
- Smooth animations and hover effects

---

## 📝 Next Steps

1. **Connect to Frontend:** Update `userId` in JavaScript files (currently hardcoded as 'user123')
2. **Add Authentication:** Integrate Spring Security for user login/session management
3. **Database Integration:** Replace txt files with actual database (MySQL, PostgreSQL)
4. **Payment Integration:** Add payment processing for bookings
5. **Email Notifications:** Send confirmation emails for bookings and reviews
6. **Admin Dashboard:** Create admin panel to manage tutors and bookings

---

## 🐛 Troubleshooting

**Issue:** Tutors not loading
- Check `src/main/resources/data/tutors.txt` exists
- Verify FileHelper.readAllLines() is working

**Issue:** Bookings creation fails
- Verify tutorId exists in tutors.txt
- Check date is not in the past
- Ensure duration and hourlyRate are valid numbers

**Issue:** CORS errors
- Controller already has `@CrossOrigin(origins = "*")`
- Check frontend is calling correct API_BASE URL

---

## 📞 Support
For issues or questions, check the error messages in:
- Browser Console (F12)
- Spring Boot logs
- FileHelper error output

---

**Status: ✅ COMPLETE & READY TO TEST**
All components created and verified with zero compilation errors!

// Review Create Form JavaScript
const API_BASE = '/api';
const bookingId = new URLSearchParams(window.location.search).get('bookingId');
const tutorId = new URLSearchParams(window.location.search).get('tutorId');
let selectedRating = 0;

document.addEventListener('DOMContentLoaded', () => {
    loadBookingAndTutorDetails();
    initializeStarRating();
    setupForm();
});

async function loadBookingAndTutorDetails() {
    try {
        const tutorResponse = await fetch(`${API_BASE}/tutors/${tutorId}`);
        const tutor = await tutorResponse.json();
        
        document.getElementById('tutorName').textContent = tutor.name;
        document.getElementById('tutorSubject').textContent = tutor.subjects;
        document.getElementById('tutorId').value = tutorId;
        document.getElementById('bookingId').value = bookingId;
    } catch (error) {
        console.error('Error loading booking details:', error);
        showAlert('Failed to load tutor details', 'danger');
    }
}

function initializeStarRating() {
    const container = document.getElementById('starRating');
    container.innerHTML = '';
    
    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('span');
        star.classList.add('star');
        star.innerHTML = '<i class="fas fa-star"></i>';
        star.onclick = () => selectRating(i);
        container.appendChild(star);
    }
}

function selectRating(rating) {
    selectedRating = rating;
    document.getElementById('rating').value = rating;
    
    const stars = document.querySelectorAll('#starRating .star');
    stars.forEach((star, index) => {
        if (index < rating) {
            star.classList.add('active');
        } else {
            star.classList.remove('active');
        }
    });
}

function setupForm() {
    const commentField = document.getElementById('comment');
    const charCount = document.getElementById('charCount');
    
    commentField.addEventListener('input', () => {
        charCount.textContent = commentField.value.length;
    });
    
    document.getElementById('reviewForm').addEventListener('submit', handleFormSubmit);
}

function handleFormSubmit(e) {
    e.preventDefault();
    
    const rating = selectedRating;
    const comment = document.getElementById('comment').value.trim();
    
    if (!rating || rating === 0) {
        showAlert('Please select a rating', 'warning');
        return;
    }
    
    if (!comment || comment.length < 10) {
        showAlert('Please write a review (at least 10 characters)', 'warning');
        return;
    }
    
    const reviewData = {
        bookingId,
        tutorId,
        userId: 'user123', // Replace with actual user ID
        rating,
        comment
    };
    
    fetch(`${API_BASE}/reviews`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(reviewData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Review submitted successfully! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = '/reviews';
            }, 1500);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to submit review', 'danger');
    });
}

function showAlert(message, type) {
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert" style="position: fixed; top: 20px; right: 20px; min-width: 300px; z-index: 9999;">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    const container = document.getElementById('alertContainer');
    container.innerHTML = alertHtml;
    
    setTimeout(() => {
        const alert = container.querySelector('.alert');
        if (alert) alert.remove();
    }, 5000);
}

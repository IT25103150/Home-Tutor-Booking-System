// Review Edit Form JavaScript
const API_BASE = '/api';
const reviewId = new URLSearchParams(window.location.search).get('reviewId');
let selectedRating = 0;

document.addEventListener('DOMContentLoaded', () => {
    if (reviewId) {
        loadReviewData();
    }
    initializeStarRating();
    setupForm();
});

async function loadReviewData() {
    try {
        const response = await fetch(`${API_BASE}/reviews/${reviewId}`);
        const review = await response.json();
        
        document.getElementById('reviewId').value = review.reviewId;
        document.getElementById('comment').value = review.comment;
        document.getElementById('charCount').textContent = review.comment.length;
        
        // Set rating
        selectedRating = review.rating;
        updateStarDisplay();
        
        // Load tutor details
        const tutorResponse = await fetch(`${API_BASE}/tutors/${review.tutorId}`);
        const tutor = await tutorResponse.json();
        
        document.getElementById('tutorName').textContent = tutor.name;
        document.getElementById('tutorSubject').textContent = tutor.subjects;
    } catch (error) {
        console.error('Error loading review:', error);
        showAlert('Failed to load review details', 'danger');
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
    
    updateStarDisplay();
}

function selectRating(rating) {
    selectedRating = rating;
    document.getElementById('rating').value = rating;
    updateStarDisplay();
}

function updateStarDisplay() {
    const stars = document.querySelectorAll('#starRating .star');
    stars.forEach((star, index) => {
        if (index < selectedRating) {
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
    
    document.getElementById('editReviewForm').addEventListener('submit', handleFormSubmit);
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
        rating,
        comment
    };
    
    fetch(`${API_BASE}/reviews/${reviewId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(reviewData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Review updated successfully! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = '/reviews';
            }, 1500);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to update review', 'danger');
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

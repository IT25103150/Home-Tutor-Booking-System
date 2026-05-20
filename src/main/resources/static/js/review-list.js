// Review List JavaScript
const API_BASE = '/api';
let allReviews = [];
let selectedReviewId = null;

// Initialize page
document.addEventListener('DOMContentLoaded', () => {
    loadReviews();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('saveEdit').addEventListener('click', saveEditedReview);
    document.getElementById('confirmDelete').addEventListener('click', confirmDeleteReview);
}

async function loadReviews() {
    try {
        const userId = 'user123'; // Get from session/auth
        const response = await fetch(`${API_BASE}/reviews/user/${userId}`);
        allReviews = await response.json();
        
        displayReviews();
        updateReviewCount();
    } catch (error) {
        console.error('Error loading reviews:', error);
        showAlert('Failed to load reviews', 'danger');
    }
}

function displayReviews() {
    const container = document.getElementById('reviewsContainer');
    const noReviews = document.getElementById('noReviews');

    if (allReviews.length === 0) {
        container.innerHTML = '';
        noReviews.classList.remove('d-none');
        return;
    }

    noReviews.classList.add('d-none');
    container.innerHTML = allReviews.map(review => `
        <div class="review-card">
            <div class="review-header">
                <div class="review-tutor">
                    <i class="fas fa-user-tie"></i> Tutor: ${review.tutorId}
                </div>
                <div class="review-date">
                    <i class="fas fa-calendar"></i> ${formatDate(review.createdDate)}
                </div>
            </div>

            <div class="review-rating">
                ${renderReviewStars(review.rating)}
            </div>

            <div class="review-comment">
                <i class="fas fa-quote-left"></i> ${escapeHtml(review.comment)}
            </div>

            <div class="review-actions">
                <button class="btn-action btn-edit" onclick="openEditModal('${review.reviewId}', ${review.rating}, '${escapeHtml(review.comment).replace(/'/g, "&#39;")}')">
                    <i class="fas fa-edit"></i> Edit
                </button>
                <button class="btn-action btn-delete" onclick="openDeleteModal('${review.reviewId}')">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </div>
        </div>
    `).join('');
}

function renderReviewStars(rating) {
    let stars = '';
    for (let i = 0; i < 5; i++) {
        if (i < rating) {
            stars += '<span class="star"><i class="fas fa-star"></i></span>';
        } else {
            stars += '<span class="star empty"><i class="far fa-star"></i></span>';
        }
    }
    return stars;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
}

function openEditModal(reviewId, rating, comment) {
    selectedReviewId = reviewId;
    document.getElementById('editReviewId').value = reviewId;
    document.getElementById('editComment').value = comment;
    
    // Generate star rating selector
    const ratingContainer = document.getElementById('editRating');
    ratingContainer.innerHTML = '';
    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('span');
        star.classList.add('star');
        if (i <= rating) {
            star.classList.add('selected');
        }
        star.innerHTML = '<i class="fas fa-star"></i>';
        star.onclick = () => selectRating(i);
        ratingContainer.appendChild(star);
    }

    const modal = new bootstrap.Modal(document.getElementById('editModal'));
    modal.show();
}

function selectRating(rating) {
    const stars = document.querySelectorAll('#editRating .star');
    stars.forEach((star, index) => {
        if (index < rating) {
            star.classList.add('selected');
        } else {
            star.classList.remove('selected');
        }
    });
}

function saveEditedReview() {
    const reviewId = document.getElementById('editReviewId').value;
    const comment = document.getElementById('editComment').value;
    const selectedStars = document.querySelectorAll('#editRating .star.selected').length;

    if (!comment.trim()) {
        showAlert('Comment cannot be empty', 'warning');
        return;
    }

    if (selectedStars === 0) {
        showAlert('Please select a rating', 'warning');
        return;
    }

    fetch(`${API_BASE}/reviews/${reviewId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            rating: selectedStars,
            comment
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Review updated successfully!', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
            modal.hide();
            loadReviews();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to update review', 'danger');
    });
}

function openDeleteModal(reviewId) {
    selectedReviewId = reviewId;
    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
}

function confirmDeleteReview() {
    if (!selectedReviewId) return;

    fetch(`${API_BASE}/reviews/${selectedReviewId}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Review deleted successfully!', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'));
            modal.hide();
            loadReviews();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to delete review', 'danger');
    });
}

function updateReviewCount() {
    document.getElementById('reviewCount').textContent = allReviews.length;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showAlert(message, type) {
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    const alertContainer = document.createElement('div');
    alertContainer.innerHTML = alertHtml;
    document.body.insertBefore(alertContainer.firstChild, document.body.firstChild);

    setTimeout(() => {
        const alert = document.querySelector('.alert');
        if (alert) {
            alert.remove();
        }
    }, 5000);
}

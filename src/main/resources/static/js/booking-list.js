// Booking List JavaScript
const API_BASE = '/api';
let allBookings = [];
let selectedBookingId = null;

// Initialize page
document.addEventListener('DOMContentLoaded', () => {
    loadBookings();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('confirmCancel').addEventListener('click', confirmCancelBooking);
    document.getElementById('confirmDelete').addEventListener('click', confirmDeleteBooking);
}

async function loadBookings() {
    try {
        const userId = 'user123'; // Get from session/auth
        const response = await fetch(`${API_BASE}/bookings/user/${userId}`);
        allBookings = await response.json();
        
        displayBookings();
        updateCountBadges();
    } catch (error) {
        console.error('Error loading bookings:', error);
        showAlert('Failed to load bookings', 'danger');
    }
}

function displayBookings() {
    const allContainer = document.getElementById('allBookings');
    const pendingContainer = document.getElementById('pendingBookings');
    const confirmedContainer = document.getElementById('confirmedBookings');
    const completedContainer = document.getElementById('completedBookings');
    const noBookings = document.getElementById('noBookings');

    if (allBookings.length === 0) {
        allContainer.innerHTML = '';
        noBookings.classList.remove('d-none');
        return;
    }

    noBookings.classList.add('d-none');

    allContainer.innerHTML = renderBookings(allBookings);
    pendingContainer.innerHTML = renderBookings(allBookings.filter(b => b.status === 'PENDING'));
    confirmedContainer.innerHTML = renderBookings(allBookings.filter(b => b.status === 'CONFIRMED'));
    completedContainer.innerHTML = renderBookings(allBookings.filter(b => b.status === 'COMPLETED'));
}

function renderBookings(bookings) {
    if (bookings.length === 0) {
        return '<p class="text-muted text-center">No bookings in this category</p>';
    }

    return bookings.map(booking => `
        <div class="booking-card">
            <div class="booking-header">
                <div class="booking-tutor">Tutor: ${booking.tutorId}</div>
                <span class="booking-status status-${booking.status.toLowerCase()}">${booking.status}</span>
            </div>
            
            <div class="booking-subject">
                <i class="fas fa-book"></i> ${booking.subject}
            </div>

            <div class="booking-details">
                <div class="booking-detail-item">
                    <div class="booking-detail-icon">
                        <i class="fas fa-calendar"></i>
                    </div>
                    <div class="booking-detail-content">
                        <div class="booking-detail-label">Date</div>
                        <div class="booking-detail-value">${booking.date}</div>
                    </div>
                </div>

                <div class="booking-detail-item">
                    <div class="booking-detail-icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="booking-detail-content">
                        <div class="booking-detail-label">Time</div>
                        <div class="booking-detail-value">${booking.time}</div>
                    </div>
                </div>

                <div class="booking-detail-item">
                    <div class="booking-detail-icon">
                        <i class="fas fa-hourglass"></i>
                    </div>
                    <div class="booking-detail-content">
                        <div class="booking-detail-label">Duration</div>
                        <div class="booking-detail-value">${booking.duration} minutes</div>
                    </div>
                </div>

                <div class="booking-detail-item">
                    <div class="booking-detail-icon">
                        <i class="fas fa-dollar-sign"></i>
                    </div>
                    <div class="booking-detail-content">
                        <div class="booking-detail-label">Rate</div>
                        <div class="booking-detail-value">$${booking.hourlyRate}/hour</div>
                    </div>
                </div>
            </div>

            <div class="booking-cost">
                Total Cost: $${booking.totalCost.toFixed(2)}
            </div>

            <div class="booking-actions">
                ${getActionButtons(booking)}
            </div>
        </div>
    `).join('');
}

function getActionButtons(booking) {
    let buttons = '';

    if (booking.status === 'PENDING') {
        buttons += `
            <button class="btn-action btn-confirm" onclick="updateStatus('${booking.bookingId}', 'CONFIRMED')">
                <i class="fas fa-check"></i> Confirm
            </button>
        `;
    }

    if (booking.status === 'CONFIRMED') {
        buttons += `
            <button class="btn-action btn-mark-complete" onclick="updateStatus('${booking.bookingId}', 'COMPLETED')">
                <i class="fas fa-check-double"></i> Mark Complete
            </button>
        `;
    }

    if (booking.status === 'COMPLETED') {
        buttons += `
            <button class="btn-action btn-review" onclick="goToReview('${booking.bookingId}', '${booking.tutorId}')">
                <i class="fas fa-star"></i> Write Review
            </button>
        `;
    }

    if (booking.status === 'PENDING') {
        buttons += `
            <button class="btn-action btn-cancel" onclick="openCancelModal('${booking.bookingId}')">
                <i class="fas fa-times"></i> Cancel
            </button>
        `;
    }

    // Delete button available for all bookings
    buttons += `
        <button class="btn-action btn-delete" onclick="openDeleteModal('${booking.bookingId}')">
            <i class="fas fa-trash"></i> Delete
        </button>
    `;

    return buttons;
}

function updateStatus(bookingId, status) {
    fetch(`${API_BASE}/bookings/${bookingId}/status`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Booking status updated!', 'success');
            loadBookings();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to update booking', 'danger');
    });
}

function openCancelModal(bookingId) {
    selectedBookingId = bookingId;
    const modal = new bootstrap.Modal(document.getElementById('cancelModal'));
    modal.show();
}

function confirmCancelBooking() {
    if (!selectedBookingId) return;

    fetch(`${API_BASE}/bookings/${selectedBookingId}/cancel`, {
        method: 'PUT'
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Booking cancelled!', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('cancelModal'));
            modal.hide();
            loadBookings();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to cancel booking', 'danger');
    });
}

function updateCountBadges() {
    document.getElementById('countAll').textContent = allBookings.length;
    document.getElementById('countPending').textContent = allBookings.filter(b => b.status === 'PENDING').length;
    document.getElementById('countConfirmed').textContent = allBookings.filter(b => b.status === 'CONFIRMED').length;
    document.getElementById('countCompleted').textContent = allBookings.filter(b => b.status === 'COMPLETED').length;
}

function goToReview(bookingId, tutorId) {
    window.location.href = `/reviews/create?bookingId=${bookingId}&tutorId=${tutorId}`;
}

function openDeleteModal(bookingId) {
    selectedBookingId = bookingId;
    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
}

function confirmDeleteBooking() {
    if (!selectedBookingId) return;

    fetch(`${API_BASE}/bookings/${selectedBookingId}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Booking deleted successfully!', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'));
            modal.hide();
            loadBookings();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to delete booking', 'danger');
    });
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

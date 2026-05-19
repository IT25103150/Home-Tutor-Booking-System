// Booking Edit Form JavaScript
const API_BASE = '/api';
const bookingId = new URLSearchParams(window.location.search).get('bookingId');

document.addEventListener('DOMContentLoaded', () => {
    if (bookingId) {
        loadBookingData();
    }
    document.getElementById('editBookingForm').addEventListener('submit', handleFormSubmit);
});

async function loadBookingData() {
    try {
        const response = await fetch(`${API_BASE}/bookings/${bookingId}`);
        const booking = await response.json();
        
        document.getElementById('bookingId').value = booking.bookingId;
        document.getElementById('tutorInfo').value = booking.tutorId;
        document.getElementById('editDate').value = booking.date;
        document.getElementById('editTime').value = booking.time;
        document.getElementById('editStatus').value = booking.status;
        document.getElementById('editSubject').textContent = booking.subject;
        document.getElementById('editDuration').textContent = booking.duration;
        document.getElementById('editTotalCost').textContent = booking.totalCost.toFixed(2);
    } catch (error) {
        console.error('Error loading booking:', error);
        showAlert('Failed to load booking details', 'danger');
    }
}

function handleFormSubmit(e) {
    e.preventDefault();
    
    const bookingId = document.getElementById('bookingId').value;
    const date = document.getElementById('editDate').value;
    const time = document.getElementById('editTime').value;
    const status = document.getElementById('editStatus').value;
    
    if (!date || !time || !status) {
        showAlert('Please fill all required fields', 'warning');
        return;
    }
    
    // Update status
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
            showAlert('Booking updated successfully! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = '/bookings';
            }, 1500);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to update booking', 'danger');
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

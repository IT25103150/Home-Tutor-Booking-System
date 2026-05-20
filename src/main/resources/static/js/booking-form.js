// Booking Create Form JavaScript
const API_BASE = '/api';
let tutorsData = [];
let selectedTutorHourlyRate = 0;

document.addEventListener('DOMContentLoaded', () => {
    loadTutors();
    setupForm();
    setMinDate();
});

function setMinDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('bookingDate').setAttribute('min', today);
}

function setupForm() {
    document.getElementById('bookingForm').addEventListener('submit', handleFormSubmit);
    document.getElementById('duration').addEventListener('change', calculateCost);
}

async function loadTutors() {
    try {
        const response = await fetch(`${API_BASE}/tutors`);
        tutorsData = await response.json();
        
        const select = document.getElementById('tutorId');
        tutorsData.forEach(tutor => {
            const option = document.createElement('option');
            option.value = tutor.tutorId;
            option.textContent = `${tutor.name} - $${tutor.hourlyRate}/hour`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading tutors:', error);
        showAlert('Failed to load tutors', 'danger');
    }
}

function updateTutorDetails() {
    const tutorId = document.getElementById('tutorId').value;
    const tutor = tutorsData.find(t => t.tutorId === tutorId);
    
    if (tutor) {
        selectedTutorHourlyRate = tutor.hourlyRate;
        document.getElementById('tutorRate').textContent = tutor.hourlyRate.toFixed(2);
        document.getElementById('summaryRate').textContent = tutor.hourlyRate.toFixed(2);
        calculateCost();
    }
}

function calculateCost() {
    const duration = parseInt(document.getElementById('duration').value) || 0;
    const hourlyRate = selectedTutorHourlyRate;
    const hours = duration / 60;
    const totalCost = hourlyRate * hours;
    
    document.getElementById('summaryDuration').textContent = duration;
    document.getElementById('totalCost').textContent = totalCost.toFixed(2);
}

function handleFormSubmit(e) {
    e.preventDefault();
    
    const tutorId = document.getElementById('tutorId').value;
    const userId = 'user123'; // Replace with actual user ID
    const date = document.getElementById('bookingDate').value;
    const time = document.getElementById('bookingTime').value;
    const duration = parseInt(document.getElementById('duration').value);
    const subject = document.getElementById('subject').value;
    
    if (!tutorId || !date || !time || !duration || !subject) {
        showAlert('Please fill all required fields', 'warning');
        return;
    }
    
    const bookingData = {
        tutorId,
        userId,
        date,
        time,
        duration,
        subject
    };
    
    fetch(`${API_BASE}/bookings`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showAlert(data.error, 'danger');
        } else {
            showAlert('Booking created successfully! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = '/bookings';
            }, 1500);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to create booking', 'danger');
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

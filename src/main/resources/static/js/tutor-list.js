// Tutor List JavaScript
const API_BASE = '/api';
let allTutors = [];
let selectedTutorHourlyRate = 0;

// Initialize page
document.addEventListener('DOMContentLoaded', () => {
    loadTutors();
    setupEventListeners();
    setMinDate();
});

function setMinDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('bookingDate').setAttribute('min', today);
}

function setupEventListeners() {
    document.getElementById('searchInput').addEventListener('input', filterTutors);
    document.getElementById('subjectFilter').addEventListener('change', filterTutors);
    document.getElementById('duration').addEventListener('change', updateEstimatedCost);
    document.getElementById('confirmBooking').addEventListener('click', submitBooking);
}

async function loadTutors() {
    try {
        const response = await fetch(`${API_BASE}/tutors`);
        allTutors = await response.json();
        
        populateSubjectFilter();
        displayTutors(allTutors);
    } catch (error) {
        console.error('Error loading tutors:', error);
        showAlert('Failed to load tutors', 'danger');
    }
}

function populateSubjectFilter() {
    const subjects = new Set();
    allTutors.forEach(tutor => {
        tutor.subjects.split(',').forEach(subject => {
            subjects.add(subject.trim());
        });
    });

    const filterSelect = document.getElementById('subjectFilter');
    subjects.forEach(subject => {
        const option = document.createElement('option');
        option.value = subject;
        option.textContent = subject;
        filterSelect.appendChild(option);
    });
}

function filterTutors() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const selectedSubject = document.getElementById('subjectFilter').value;

    const filtered = allTutors.filter(tutor => {
        const matchesName = tutor.name.toLowerCase().includes(searchText);
        const matchesSubject = selectedSubject === '' || tutor.subjects.includes(selectedSubject);
        return matchesName && matchesSubject;
    });

    displayTutors(filtered);
}

function displayTutors(tutors) {
    const container = document.getElementById('tutorsContainer');
    const noResults = document.getElementById('noResults');

    if (tutors.length === 0) {
        container.innerHTML = '';
        noResults.classList.remove('d-none');
        return;
    }

    noResults.classList.add('d-none');
    container.innerHTML = tutors.map(tutor => `
        <div class="col-md-6 col-lg-4 mb-4">
            <div class="tutor-card">
                <div class="tutor-avatar">
                    <i class="fas fa-user-tie"></i>
                </div>
                <div class="tutor-info">
                    <div class="tutor-name">${tutor.name}</div>
                    <div class="tutor-subjects"><i class="fas fa-book"></i> ${tutor.subjects}</div>
                    <div class="tutor-rating">
                        <span class="rating-stars">${renderStars(tutor.rating)}</span>
                        <span class="rating-count">(${tutor.reviewCount || 0} reviews)</span>
                    </div>
                    <div class="tutor-availability">
                        <i class="fas fa-clock"></i> ${tutor.availability}
                    </div>
                    <div class="tutor-rate">$${tutor.hourlyRate}/hour</div>
                    <div class="tutor-description">${tutor.description}</div>
                    <div class="tutor-actions">
                        <button class="btn-book" onclick="openBookingModal('${tutor.tutorId}', '${tutor.name}', ${tutor.hourlyRate}, '${tutor.subjects}')">
                            <i class="fas fa-calendar"></i> Book Now
                        </button>
                        <button class="btn-view" onclick="viewTutorDetails('${tutor.tutorId}')">
                            <i class="fas fa-eye"></i> View
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

function renderStars(rating) {
    let stars = '';
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;

    for (let i = 0; i < 5; i++) {
        if (i < fullStars) {
            stars += '<i class="fas fa-star"></i>';
        } else if (i === fullStars && hasHalfStar) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        } else {
            stars += '<i class="far fa-star"></i>';
        }
    }
    return stars;
}

function openBookingModal(tutorId, tutorName, hourlyRate, subjects) {
    selectedTutorHourlyRate = hourlyRate;
    document.getElementById('tutorId').value = tutorId;
    document.getElementById('tutorName').textContent = tutorName;
    
    // Populate subjects dropdown
    const subjectSelect = document.getElementById('subject');
    subjectSelect.innerHTML = '<option value="">Select subject...</option>';
    subjects.split(',').forEach(subject => {
        const option = document.createElement('option');
        option.value = subject.trim();
        option.textContent = subject.trim();
        subjectSelect.appendChild(option);
    });

    // Show modal
    const modal = new bootstrap.Modal(document.getElementById('bookingModal'));
    modal.show();
}

function updateEstimatedCost() {
    const duration = parseInt(document.getElementById('duration').value);
    const hours = duration / 60;
    const cost = selectedTutorHourlyRate * hours;
    document.getElementById('estimatedCost').textContent = cost.toFixed(2);
}

function submitBooking() {
    const tutorId = document.getElementById('tutorId').value;
    const userId = 'user123'; // Get from session/auth
    const date = document.getElementById('bookingDate').value;
    const time = document.getElementById('bookingTime').value;
    const duration = parseInt(document.getElementById('duration').value);
    const subject = document.getElementById('subject').value;

    if (!date || !time || !subject) {
        showAlert('Please fill all fields', 'warning');
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
            showAlert('Booking created successfully!', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('bookingModal'));
            modal.hide();
            // Clear form
            document.getElementById('bookingForm').reset();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Failed to create booking', 'danger');
    });
}

function viewTutorDetails(tutorId) {
    // Navigate to tutor profile/details page
    window.location.href = `/tutors/${tutorId}`;
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

    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        const alert = document.querySelector('.alert');
        if (alert) {
            alert.remove();
        }
    }, 5000);
}

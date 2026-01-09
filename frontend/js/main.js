/**
 * ScholarshipHub - Main JavaScript
 * Shared functionality across all pages
 */

// Dark mode toggle
function toggleDarkMode() {
    document.documentElement.classList.toggle('dark');
    localStorage.setItem('darkMode', document.documentElement.classList.contains('dark'));
}

// Initialize dark mode from localStorage
function initDarkMode() {
    if (localStorage.getItem('darkMode') === 'true') {
        document.documentElement.classList.add('dark');
    }
}

// Mobile menu toggle
function toggleMobileMenu() {
    const menu = document.getElementById('mobile-menu');
    if (menu) {
        menu.classList.toggle('hidden');
    }
}

// Form validation helper
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Password visibility toggle
function togglePasswordVisibility(inputId, iconElement) {
    const input = document.getElementById(inputId);
    if (input.type === 'password') {
        input.type = 'text';
        iconElement.textContent = 'visibility';
    } else {
        input.type = 'password';
        iconElement.textContent = 'visibility_off';
    }
}

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', function () {
    initDarkMode();
    handleAdminUI();
});

// Handle Admin-specific UI adjustments
function handleAdminUI() {
    const userStr = localStorage.getItem('user');
    if (!userStr) return;

    try {
        const user = JSON.parse(userStr);
        const role = user.role?.toUpperCase();

        if (role === 'ADMIN') {
            // Hide "My Applications" nav link for admin
            const myApplicationsLink = document.getElementById('nav-my-applications');
            if (myApplicationsLink) {
                myApplicationsLink.style.display = 'none';
            }

            // Hide academic tab for admin (on profile page)
            const academicTab = document.getElementById('tab-academic');
            if (academicTab) {
                academicTab.style.display = 'none';
            }

            // Hide academic section for admin (on profile page)
            const academicSection = document.getElementById('academic');
            if (academicSection) {
                academicSection.style.display = 'none';
            }
        }
    } catch (e) {
        console.error('Error handling admin UI:', e);
    }
}

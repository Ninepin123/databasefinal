/**
 * Global Configuration
 */
const CONFIG = {
    // Automatically detect environment
    API_BASE: (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
        ? 'http://localhost:8080/api'
        : '/api' // Default to relative path for production (requires proxy or same-origin) or replace with full URL
};

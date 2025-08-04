document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('authToken');
    const loginItem = document.getElementById('loginItem');
    const registerItem = document.getElementById('registerItem');
    const logoutItem = document.getElementById('logoutItem');

    if (token) {
        // Usuario autenticado
        if (loginItem) loginItem.style.display = 'none';
        if (registerItem) registerItem.style.display = 'none';
        if (logoutItem) logoutItem.style.display = 'block';
    } else {
        // Usuario no autenticado
        if (loginItem) loginItem.style.display = 'block';
        if (registerItem) registerItem.style.display = 'block';
        if (logoutItem) logoutItem.style.display = 'none';
    }

    // Manejar cierre de sesión
    if (logoutItem) {
        logoutItem.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('authToken');
            window.location.href = '/usuarios/inicio.html';
        });
    }
});
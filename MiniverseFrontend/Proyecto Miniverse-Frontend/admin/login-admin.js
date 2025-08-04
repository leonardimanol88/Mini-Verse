// admin/login.js
const API_BASE_URL = 'http://44.209.91.221:7002';

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const errorMessage = document.getElementById('errorMessage');
        
        // Validación básica
        if (!email || !password) {
            showError('Por favor complete todos los campos', errorMessage);
            return;
        }
        
        try {
            const response = await fetch(`${API_BASE_URL}/admin/iniciarSesion`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    correo: email,
                    contrasena: password
                })
            });
            
            const data = await response.json();
            
            if (response.ok) {
                // Guardar el token JWT en localStorage
                localStorage.setItem('adminToken', data.token);
                
                // Redirigir al panel de administración
                window.location.href = '/admin/admin.html';
            } else {
                showError(data.error || 'Error en las credenciales', errorMessage);
            }
        } catch (error) {
            console.error('Error:', error);
            showError('Error de conexión con el servidor', errorMessage);
        }
    });
});

function showError(message, element) {
    element.textContent = message;
    element.style.display = 'block';
    
    setTimeout(() => {
        element.style.display = 'none';
    }, 5000);
}
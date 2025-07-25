const API_BASE_URL = 'http://44.209.91.221:7002';

document.addEventListener('DOMContentLoaded', () => {
    // Si ya está autenticado, redirigir
    if (localStorage.getItem('adminToken')) {
        const redirectTo = localStorage.getItem('redirectAfterLogin') || '/admin/admin.html';
        localStorage.removeItem('redirectAfterLogin');
        window.location.href = redirectTo;
        return;
    }

    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const loginBtn = document.getElementById('loginBtn');
    const loginText = document.getElementById('loginText');
    const loginSpinner = document.getElementById('loginSpinner');
    const errorMessage = document.getElementById('errorMessage');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const email = emailInput.value.trim();
        const password = passwordInput.value.trim();

        if (!email || !password) {
            showError('Por favor completa todos los campos');
            return;
        }

        // Mostrar estado de carga
        loginText.textContent = 'Verificando...';
        loginSpinner.style.display = 'inline-block';
        loginBtn.disabled = true;

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
                // Guardar el token y redirigir
                localStorage.setItem('adminToken', data.token);
                const redirectTo = localStorage.getItem('redirectAfterLogin') || '/admin/admin.html';
                localStorage.removeItem('redirectAfterLogin');
                window.location.href = redirectTo;
            } else {
                showError(data.error || 'Credenciales incorrectas');
            }
        } catch (error) {
            console.error('Error en el login:', error);
            showError('Error de conexión con el servidor');
        } finally {
            // Restaurar estado del botón
            loginText.textContent = 'Acceder';
            loginSpinner.style.display = 'none';
            loginBtn.disabled = false;
        }
    });

    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = 'block';
        setTimeout(() => {
            errorMessage.style.display = 'none';
        }, 5000);
    }
});
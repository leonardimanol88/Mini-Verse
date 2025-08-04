document.querySelector('form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const correo = document.getElementById('email').value;
    const contrasena = document.getElementById('password').value;

    try {
        const response = await fetch('http://44.209.91.221:7002/iniciarSesion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                correo,
                contrasena
            })
        });

        const data = await response.json();

        if (response.ok) {
            // Guardar token y rol en localStorage
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('usuarioRol', data.rol);

            showMessage('Inicio de sesión exitoso. Redirigiendo...', 'success');

            // Redireccionar segun el rol
            setTimeout(() => {
                if (data.rol === 'admin') {
                    window.location.href = '/admin/admin.html';
                } else {
                    window.location.href = '/usuarios/inicio.html';
                }
            }, 2000);
        } else {
            showMessage(data.error || 'Credenciales incorrectas', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('Error al conectar con el servidor', 'error');
    }
});

// Mostrar mensaje si viene de registro exitoso
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('registro') === 'exitoso') {
        showMessage('Registro exitoso. Por favor inicia sesión.', 'success');
    }
});

function showMessage(message, type) {
    // Eliminar mensajes anteriores
    const existingMessage = document.querySelector('.custom-message');
    if (existingMessage) existingMessage.remove();

    const messageElement = document.createElement('div');
    messageElement.className = `custom-message ${type}`;
    messageElement.textContent = message;

    // Insertar después del título del formulario
    const loginTitle = document.querySelector('.login-title');
    loginTitle.insertAdjacentElement('afterend', messageElement);

    // Eliminar después de 3 segundos (excepto para éxito que redirige)
    if (type !== 'success') {
        setTimeout(() => {
            messageElement.remove();
        }, 3000);
    }
}
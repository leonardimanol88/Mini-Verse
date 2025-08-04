document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const nombre = document.getElementById('nombre').value;
    const edad = parseInt(document.getElementById('edad').value);
    const correo = document.getElementById('correo').value;
    const contrasena = document.getElementById('contrasena').value;
    const confirmarContrasena = document.getElementById('confirmar-contrasena').value;

    // Validaciones básicas
    if (contrasena !== confirmarContrasena) {
        showMessage('Las contraseñas no coinciden', 'error');
        return;
    }

    if (edad < 1 || edad > 120) {
        showMessage('Edad no válida', 'error');
        return;
    }

    try {
        const response = await fetch('http://44.209.91.221:7002/registrarUsuario', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nombre,
                edad,
                correo,
                contrasena
            })
        });

        const data = await response.json();

        if (response.ok) {
            showMessage('Registro exitoso. Redirigiendo...', 'success');
            setTimeout(() => {
                window.location.href = '/usuarios/ingresar.html?registro=exitoso';
            }, 2000);
        } else {
            showMessage(data.error || 'Error en el registro', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('Error al conectar con el servidor', 'error');
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
    const formTitle = document.querySelector('.form-title');
    formTitle.insertAdjacentElement('afterend', messageElement);

    // Eliminar después de 3 segundos (excepto para éxito que redirige)
    if (type !== 'success') {
        setTimeout(() => {
            messageElement.remove();
        }, 3000);
    }
}
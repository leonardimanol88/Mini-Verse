document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('authToken');
    
    if (!token) {
        window.location.href = '/usuarios/ingresar.html';
        return;
    }

    // Cargar datos del usuario
    await cargarDatosUsuario(token);
    
    // Configurar eventos
    configurarEventos(token);
});

async function cargarDatosUsuario(token) {
    try {
        const response = await fetch('http://44.209.91.221:7002/mostrarMiUsuario', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener datos del usuario');
        }

        const usuario = await response.json();
        
        // Mostrar datos del usuario
        document.getElementById('userNameDisplay').textContent = usuario.nombre || 'Usuario';
        document.getElementById('email').value = usuario.correo || '';
        
        // Configurar avatar
        const avatarElement = document.getElementById('userAvatar');
        if (avatarElement) {
            const inicial = usuario.nombre ? usuario.nombre.charAt(0).toUpperCase() : 'U';
            avatarElement.textContent = inicial;
            avatarElement.style.backgroundColor = generarColorDesdeNombre(usuario.nombre || 'Usuario');
        }
        
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar los datos del usuario');
    }
}

function configurarEventos(token) {
    // Botón Guardar cambios
    document.getElementById('saveChangesBtn').addEventListener('click', async () => {
        await actualizarPerfil(token);
    });
    
    // Botón Eliminar cuenta
    document.getElementById('deleteAccountBtn').addEventListener('click', () => {
        mostrarModalConfirmacion(
            'Eliminar cuenta', 
            '¿Estás seguro que deseas eliminar tu cuenta permanentemente? Esta acción no se puede deshacer.',
            async () => {
                await eliminarCuenta(token);
            }
        );
    });
    
    // Configurar modal de confirmación
    document.getElementById('cancelModalBtn').addEventListener('click', () => {
        document.getElementById('confirmModal').style.display = 'none';
    });
}

async function actualizarPerfil(token) {
    const email = document.getElementById('email').value;
    const currentPassword = document.getElementById('current-password').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    
    
    if (!email || !currentPassword) {
        mostrarError('El correo y la contraseña actual son obligatorios');
        return;
    }
    
    if (newPassword && newPassword !== confirmPassword) {
        mostrarError('Las contraseñas nuevas no coinciden');
        return;
    }
    
    try {
        // Si hay nueva contraseña, actualizamos
        if (newPassword) {
            const actualizacionExitosa = await actualizarContrasena(token, currentPassword, newPassword);
            if (!actualizacionExitosa) return;
        }
        
        mostrarError('Cambios guardados exitosamente', 'success');
        
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message || 'Error al actualizar el perfil');
    }
}

async function actualizarContrasena(token, contrasenaActual, nuevaContrasena) {
    try {
        const response = await fetch('http://44.209.91.221:7002/actualizarContrasena', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                contrasena: contrasenaActual,
                nuevaContrasena: nuevaContrasena
            })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Error al actualizar la contraseña');
        }

        return true;
    } catch (error) {
        mostrarError(error.message);
        return false;
    }
}

async function eliminarCuenta(token) {
    const contrasena = document.getElementById('current-password').value;
    
    if (!contrasena) {
        mostrarError('Debes ingresar tu contraseña actual para eliminar la cuenta');
        return;
    }
    
    try {
        const response = await fetch('http://44.209.91.221:7002/eliminarMiUsuario', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                contrasena: contrasena
            })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Error al eliminar la cuenta');
        }

        localStorage.removeItem('authToken');
        window.location.href = '/inicio.html';
        
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message || 'Error al eliminar la cuenta');
    }
}

function mostrarModalConfirmacion(titulo, mensaje, callbackConfirmacion) {
    document.getElementById('modalTitle').textContent = titulo;
    document.getElementById('modalMessage').textContent = mensaje;
    document.getElementById('confirmModal').style.display = 'flex';
    
    // Configurar botón de confirmación
    const confirmBtn = document.getElementById('confirmModalBtn');
    confirmBtn.onclick = async () => {
        confirmBtn.disabled = true;
        document.getElementById('confirmModal').style.display = 'none';
        await callbackConfirmacion();
        confirmBtn.disabled = false;
    };
}

function generarColorDesdeNombre(nombre) {
    const colores = ['#FF5733', '#33FF57', '#3357FF', '#F333FF', '#33FFF3', '#FF33F3'];
    const hash = nombre.split('').reduce((acc, char) => char.charCodeAt(0) + acc, 0);
    return colores[hash % colores.length];
}

function mostrarError(mensaje, tipo = 'error') {
    const errorDiv = document.createElement('div');
    errorDiv.className = `error-message ${tipo}`;
    errorDiv.textContent = mensaje;
    document.body.appendChild(errorDiv);
    
    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}
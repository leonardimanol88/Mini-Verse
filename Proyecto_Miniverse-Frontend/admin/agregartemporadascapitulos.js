// Constante única para la URL base
const API_BASE_URL = 'http://44.209.91.221:7002';

// Función para mostrar mensajes visuales
function mostrarMensaje(mensaje, tipo = 'success') {
    // Eliminar mensajes anteriores
    const mensajesAnteriores = document.querySelectorAll('.mensaje-flotante');
    mensajesAnteriores.forEach(msg => msg.remove());
    
    // Crear nuevo mensaje
    const mensajeDiv = document.createElement('div');
    mensajeDiv.className = `mensaje-flotante ${tipo}`;
    mensajeDiv.textContent = mensaje;
    
    // Estilos del mensaje
    Object.assign(mensajeDiv.style, {
        position: 'fixed',
        top: '20px',
        right: '20px',
        padding: '15px 25px',
        backgroundColor: tipo === 'error' ? '#dc3545' : '#28a745',
        color: 'white',
        borderRadius: '5px',
        zIndex: '1000',
        boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
        animation: 'fadeIn 0.3s ease-out'
    });
    
    document.body.appendChild(mensajeDiv);
    
    // Desaparecer después de 5 segundos
    setTimeout(() => {
        mensajeDiv.style.animation = 'fadeOut 0.3s ease-out';
        setTimeout(() => mensajeDiv.remove(), 300);
    }, 5000);
}


function validarDuracion(duracion) {
    const regex = /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
    return regex.test(duracion);
}


function mostrarConfirmacion(mensaje, callback) {
    const overlay = document.createElement('div');
    overlay.className = 'overlay';
    
    const dialog = document.createElement('div');
    dialog.className = 'confirm-dialog';
    dialog.innerHTML = `
        <h3>Confirmar eliminación</h3>
        <p>${mensaje}</p>
        <div class="confirm-dialog-buttons">
            <button class="confirm-dialog-button cancel">Cancelar</button>
            <button class="confirm-dialog-button confirm">Eliminar</button>
        </div>
    `;
    
    document.body.appendChild(overlay);
    document.body.appendChild(dialog);
    
    const cancelBtn = dialog.querySelector('.cancel');
    const confirmBtn = dialog.querySelector('.confirm');
    
    cancelBtn.addEventListener('click', () => {
        document.body.removeChild(overlay);
        document.body.removeChild(dialog);
    });
    
    confirmBtn.addEventListener('click', () => {
        document.body.removeChild(overlay);
        document.body.removeChild(dialog);
        callback();
    });
}

async function manejarSubmitTemporada(e) {
    e.preventDefault();
    
    const form = e.target;
    const btnSubmit = form.querySelector('button[type="submit"]');
    const btnOriginalText = btnSubmit.textContent;
    
    try {
        btnSubmit.disabled = true;
        btnSubmit.textContent = 'Guardando...';
        
        // Verificar conexión antes de enviar datos
        try {
            await fetch(API_BASE_URL);
        } catch (error) {
            throw new Error('No se puede conectar con el servidor. Verifica tu conexión a internet.');
        }

        const nombreSerie = form.buscarSerie.value.trim();
        const numeroTemporada = parseInt(form.numeroTemporada.value);
        const imagenUrl = form.imagenTemporada.value.trim() || null;
        const nombreTemporada = form.nombreTemporada.value.trim();
        const descripcion = form.description.value.trim();

        // Validaciones
        if (!nombreSerie || nombreSerie.length < 2) {
            throw new Error('Nombre de serie requerido (mínimo 2 caracteres)');
        }
        
        if (isNaN(numeroTemporada)) {
            throw new Error('Número de temporada inválido');
        }
        
        if (!nombreTemporada || nombreTemporada.length < 2) {
            throw new Error('Nombre de temporada requerido (mínimo 2 caracteres)');
        }
        
        if (!descripcion || descripcion.length < 10) {
            throw new Error('Descripción requerida (mínimo 10 caracteres)');
        }
        
        const temporadaData = {
            numero: numeroTemporada,
            nombreSerie: nombreSerie,
            imagen_url: imagenUrl,
            nombreTemporada: nombreTemporada,
            descripcion: descripcion
        };
        
        const response = await fetch(`${API_BASE_URL}/admin/agregarTemporada`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(temporadaData)
        });
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.error || `Error ${response.status}: ${response.statusText}`);
        }
        
        mostrarMensaje('Temporada agregada con éxito');
        form.reset();
        
    } catch (error) {
        console.error('Error completo:', error);
        mostrarMensaje(error.message || 'Error desconocido al agregar temporada', 'error');
    } finally {
        btnSubmit.disabled = false;
        btnSubmit.textContent = btnOriginalText;
    }
}

// Función para manejar el envío de capítulos
async function manejarSubmitCapitulo(e) {
    e.preventDefault();
    const form = e.target;
    const btnSubmit = form.querySelector('button[type="submit"]');
    
    try {
        // Deshabilitar botón y mostrar estado de carga
        btnSubmit.disabled = true;
        btnSubmit.innerHTML = '<span class="spinner"></span> Guardando...';
        
        // Obtener y validar datos
        const formData = {
            titulo: document.getElementById('tituloCapitulo').value.trim(),
            numero: parseInt(document.getElementById('numeroCapitulo').value),
            duracion: document.getElementById('duracionCapitulo').value.trim(),
            nombreSerie: document.getElementById('buscarSerieCapitulo').value.trim(),
            temporada: parseInt(document.getElementById('numeroTemporadaCapitulo').value)
        };

        // Validaciones
        if (!formData.nombreSerie || formData.nombreSerie.length < 2) {
            throw new Error('Nombre de serie requerido (mínimo 2 caracteres)');
        }
        
        if (isNaN(formData.temporada) || formData.temporada < 1) {
            throw new Error('Temporada inválida (debe ser número positivo)');
        }
        
        if (!formData.titulo || formData.titulo.length < 2) {
            throw new Error('Título requerido (mínimo 2 caracteres)');
        }
        
        if (isNaN(formData.numero) || formData.numero < 1) {
            throw new Error('Número de capítulo inválido (debe ser positivo)');
        }
        
        if (!validarDuracion(formData.duracion)) {
            throw new Error('Formato de duración debe ser HH:MM:SS');
        }

        // Intentar conexión con timeout
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 10000);
        
        const response = await fetch(`${API_BASE_URL}/admin/agregarCapitulo`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(formData),
            signal: controller.signal
        });
        
        clearTimeout(timeoutId);

        // Manejar respuesta
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            const mensajeError = errorData.error || 
            (response.status === 400 ? 'Datos inválidos' : 
            (response.status === 500 ? 'Error interno del servidor' : `Error ${response.status}`));
            throw new Error(mensajeError);
        }

        mostrarMensaje('Capítulo agregado exitosamente');
        form.reset();

    } catch (error) {
        console.error('Error completo:', error);
        const mensaje = error.name === 'AbortError' 
            ? 'El servidor no respondió a tiempo'
            : error.message || 'Error de conexión';
        
        mostrarMensaje(`Error: ${mensaje}`, 'error');
    } finally {
        btnSubmit.disabled = false;
        btnSubmit.textContent = 'Guardar Capítulo';
    }
}

// Función para manejar la eliminación de temporada
async function manejarEliminarTemporada(e) {
    e.preventDefault();
    
    const form = e.target;
    const btnSubmit = form.querySelector('button[type="submit"]');
    const btnOriginalText = btnSubmit.textContent;
    
    try {
        const nombreSerie = document.getElementById('buscarSerieEliminar').value.trim();
        const numeroTemporada = parseInt(document.getElementById('numeroTemporadaEliminar').value);
        
        if (!nombreSerie || isNaN(numeroTemporada)) {
            throw new Error('Datos incompletos o inválidos');
        }
        
        mostrarConfirmacion(`¿Estás seguro de eliminar la temporada ${numeroTemporada} de "${nombreSerie}"?`, async () => {
            try {
                btnSubmit.disabled = true;
                btnSubmit.textContent = 'Eliminando...';
                
                const response = await fetch(`${API_BASE_URL}/admin/eliminarTemporada`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify({
                        numero: numeroTemporada,
                        nombreSerie: nombreSerie
                    })
                });
                
                if (!response.ok) {
                    const errorData = await response.json().catch(() => ({}));
                    throw new Error(errorData.error || `Error ${response.status}: ${response.statusText}`);
                }
                
                mostrarMensaje('Temporada eliminada con éxito');
                form.reset();
            } catch (error) {
                console.error('Error al eliminar temporada:', error);
                mostrarMensaje(error.message || 'Error al eliminar temporada', 'error');
            } finally {
                btnSubmit.disabled = false;
                btnSubmit.textContent = btnOriginalText;
            }
        });
        
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje(error.message || 'Error al procesar la solicitud', 'error');
    }
}

// Función para manejar la eliminación de capítulo
async function manejarEliminarCapitulo(e) {
    e.preventDefault();
    
    const form = e.target;
    const btnSubmit = form.querySelector('button[type="submit"]');
    const btnOriginalText = btnSubmit.textContent;
    
    try {
        const nombreSerie = document.getElementById('buscarSerieEliminarCapitulo').value.trim();
        const numTemporada = parseInt(document.getElementById('numeroTemporadaEliminarCapitulo').value);
        const tituloCapitulo = document.getElementById('tituloCapituloEliminar').value.trim();
        
        if (!nombreSerie || isNaN(numTemporada) || !tituloCapitulo) {
            throw new Error('Datos incompletos o inválidos');
        }
        
        mostrarConfirmacion(`¿Estás seguro de eliminar el capítulo "${tituloCapitulo}" de la temporada ${numTemporada} de "${nombreSerie}"?`, async () => {
            try {
                btnSubmit.disabled = true;
                btnSubmit.textContent = 'Eliminando...';
                
                const response = await fetch(`${API_BASE_URL}/admin/eliminarCapitulo`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify({
                        titulo: tituloCapitulo,
                        nombreSerie: nombreSerie,
                        temporada: numTemporada
                    })
                });
                
                if (!response.ok) {
                    const errorData = await response.json().catch(() => ({}));
                    throw new Error(errorData.error || `Error ${response.status}: ${response.statusText}`);
                }
                
                mostrarMensaje('Capítulo eliminado con éxito');
                form.reset();
            } catch (error) {
                console.error('Error al eliminar capítulo:', error);
                mostrarMensaje(error.message || 'Error al eliminar capítulo', 'error');
            } finally {
                btnSubmit.disabled = false;
                btnSubmit.textContent = btnOriginalText;
            }
        });
        
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje(error.message || 'Error al procesar la solicitud', 'error');
    }
}

// Inicialización cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    console.log('Script de temporadas y capítulos cargado correctamente');
    
    // Asignar event listeners para agregar
    const formTemporada = document.getElementById('temporadaForm');
    if (formTemporada) {
        formTemporada.addEventListener('submit', manejarSubmitTemporada);
    }

    const formCapitulo = document.getElementById('capituloForm');
    if (formCapitulo) {
        formCapitulo.addEventListener('submit', manejarSubmitCapitulo);
    }
    
    // Asignar event listeners para eliminar
    const formEliminarTemporada = document.getElementById('eliminarTemporadaForm');
    if (formEliminarTemporada) {
        formEliminarTemporada.addEventListener('submit', manejarEliminarTemporada);
    }
    
    const formEliminarCapitulo = document.getElementById('eliminarCapituloForm');
    if (formEliminarCapitulo) {
        formEliminarCapitulo.addEventListener('submit', manejarEliminarCapitulo);
    }
});
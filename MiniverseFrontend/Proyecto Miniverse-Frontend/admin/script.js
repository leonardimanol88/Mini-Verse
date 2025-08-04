const API_BASE_URL = 'http://44.209.91.221:7002';


function mostrarMensaje(mensaje, tipo = 'success') {
    const mensajesAnteriores = document.querySelectorAll('.message');
    mensajesAnteriores.forEach(msg => msg.remove());
    
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${tipo}`;
    messageDiv.textContent = mensaje;
    
    
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 8px;
        color: white;
        font-weight: 500;
        z-index: 1000;
        opacity: 0;
        transform: translateY(-20px);
        transition: all 0.3s ease;
        background-color: ${tipo === 'error' ? '#dc3545' : '#28a745'};
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
    `;
    
    document.body.appendChild(messageDiv);
    
    
    setTimeout(() => {
        messageDiv.style.opacity = '1';
        messageDiv.style.transform = 'translateY(0)';
    }, 100);
    
    
    setTimeout(() => {
        messageDiv.style.opacity = '0';
        messageDiv.style.transform = 'translateY(-20px)';
        setTimeout(() => {
            messageDiv.remove();
        }, 300);
    }, 3000);
}


function validarFormulario(nombre, anio, sinopsis, genero, director) {
    if (!nombre || nombre.trim() === '') {
        mostrarMensaje('El nombre de la serie es obligatorio', 'error');
        return false;
    }
    
    if (!anio || isNaN(anio) || anio < 1900 || anio > new Date().getFullYear() + 5) {
        mostrarMensaje('El año de estreno debe ser un valor válido', 'error');
        return false;
    }
    
    if (!sinopsis || sinopsis.trim().length < 10) {
        mostrarMensaje('La sinopsis debe tener al menos 10 caracteres', 'error');
        return false;
    }
    
    if (!genero || genero === "") {
        mostrarMensaje('Debes seleccionar un género válido', 'error');
        return false;
    }
    if (!director || director.trim() === '') {
        mostrarMensaje('El director es obligatorio', 'error');
        return false;
    }
    
    return true;
}


function limpiarFormulario() {
    document.getElementById('serieForm').reset();
    document.getElementById('seriesImg').src = 'https://placehold.co/350x500/4a4a4a/ffffff?text=SERIE';
}


document.getElementById('imageUrl').addEventListener('input', function(e) {
    const url = e.target.value.trim();
    const imgPreview = document.getElementById('seriesImg');
    
    if (url) {
        imgPreview.src = url;
    } else {
        imgPreview.src = 'https://placehold.co/350x500/4a4a4a/ffffff?text=SERIE';
    }
});


async function agregarSerie(serieData) {
    const submitBtn = document.querySelector('.btn-primary');
    const originalText = submitBtn.textContent;
    
    submitBtn.textContent = 'Guardando...';
    submitBtn.disabled = true;
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/agregarSerie`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(serieData)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            
            mostrarMensaje('Serie agregada correctamente');
            limpiarFormulario();
        } else {
            
            mostrarMensaje(result.error || 'Error al agregar la serie', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje('Error de conexión con el servidor', 'error');
    } finally {
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
}


document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando admin panel');
    cargarGeneros(); // Cargar géneros al iniciar
    cargarDirectores();
    // Configurar el formulario
    document.getElementById('serieForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const nombre = document.getElementById('nombreSerie').value.trim();
        const anio = parseInt(document.getElementById('anio').value);
        const sinopsis = document.getElementById('sinopsis').value.trim();
        const genero = document.getElementById('genero').value.trim();
        const director = document.getElementById('director').value.trim();
        const imagenUrl = document.getElementById('imageUrl').value.trim();
        
        
        if (!validarFormulario(nombre, anio, sinopsis, genero, director)) {
            return;
        }
        
        
        const serieData = {
            nombre, 
            estreno: anio,
            sinopsis,
            idGenero: genero,
            idDirector: director,
            imagenUrl: imagenUrl || 'https://placehold.co/350x500/4a4a4a/ffffff?text=SERIE'
        };
        
        
        await agregarSerie(serieData);
    });
});

async function cargarGeneros() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/obtenerGeneros`);
        console.log("Respuesta del servidor:", response); // <-- Añadir esto
        if (!response.ok) {
            throw new Error('Error al obtener géneros');
        }
        const generos = await response.json();
        console.log("Géneros recibidos:", generos); // <-- Añadir esto
        
        const selectGenero = document.getElementById('genero');
        
        // Limpiar opciones existentes (excepto la primera)
        while (selectGenero.options.length > 1) {
            selectGenero.remove(1);
        }
        
        // Agregar nuevas opciones
        generos.forEach(genero => {
            const option = document.createElement('option');
            option.value = genero.id; // Usamos el ID como valor
            option.textContent = genero.nombre;
            selectGenero.appendChild(option);
        });
        
    } catch (error) {
        console.error('Error al cargar géneros:', error);
        mostrarMensaje('Error al cargar la lista de géneros', 'error');
    }
}

document.getElementById('btnEliminarSerie').addEventListener('click', async () => {
    const nombreSerie = document.getElementById('nombreSerieEliminar').value.trim();
    
    if (!nombreSerie) {
        mostrarMensaje('Debes ingresar el nombre de la serie a eliminar', 'error');
        return;
    }

    const confirmacion = confirm(`¿Estás seguro que deseas eliminar la serie "${nombreSerie}"?`);
    if (!confirmacion) return;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/eliminarSerie/${encodeURIComponent(nombreSerie)}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (response.ok) {
            mostrarMensaje(`Serie "${nombreSerie}" eliminada correctamente`);
            document.getElementById('nombreSerieEliminar').value = '';
        } else {
            mostrarMensaje(result.error || 'Error al eliminar la serie', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje('Error de conexión con el servidor', 'error');
    }
});

// Agregar esta función para cargar directores
async function cargarDirectores() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/obtenerDirectores`);
        if (!response.ok) {
            throw new Error('Error al obtener directores');
        }
        const directores = await response.json();
        
        const selectDirector = document.getElementById('director');
        
        // Cambiar el input de texto por un select
        const directorContainer = selectDirector.parentNode;
        directorContainer.innerHTML = `
            <select id="director" class="form-input" required>
                <option value="" selected disabled>Selecciona un director</option>
                ${directores.map(d => `<option value="${d.id}">${d.nombre}</option>`).join('')}
            </select>
        `;
        
    } catch (error) {
        console.error('Error al cargar directores:', error);
        mostrarMensaje('Error al cargar la lista de directores', 'error');
    }
}

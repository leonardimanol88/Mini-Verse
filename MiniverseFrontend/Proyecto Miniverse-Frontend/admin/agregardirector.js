
const BASE_URL = 'http://44.209.91.221:7002'; 


const directorForm = document.getElementById('directorForm');
const directorNameInput = document.getElementById('directorName');
const directorBioInput = document.getElementById('directorBio');
const directorsSection = document.querySelector('.directors-section');


let directores = [];


function mostrarMensaje(mensaje, tipo = 'success') {
    
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${tipo}`;
    messageDiv.textContent = mensaje;
    
    document.body.appendChild(messageDiv);
    
    
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}


function limpiarFormulario() {
    directorNameInput.value = '';
    directorBioInput.value = '';
}


function validarFormulario(nombre, biografia) {
    if (!nombre || nombre.trim() === '') {
        mostrarMensaje('El nombre del director es obligatorio', 'error');
        return false;
    }
    
    if (!biografia || biografia.trim() === '') {
        mostrarMensaje('La biografía del director es obligatoria', 'error');
        return false;
    }
    
    if (nombre.trim().length < 2) {
        mostrarMensaje('El nombre debe tener al menos 2 caracteres', 'error');
        return false;
    }
    
    if (biografia.trim().length < 10) {
        mostrarMensaje('La biografía debe tener al menos 10 caracteres', 'error');
        return false;
    }
    
    return true;
}


async function agregarDirector(nombre, biografia) {
    try {
        
        const submitButton = document.querySelector('.save-button');
        const originalText = submitButton.textContent;
        submitButton.textContent = 'Guardando...';
        submitButton.disabled = true;
        
        
        const directorData = {
            nombre: nombre.trim(),
            biografia: biografia.trim()
        };
        
        console.log('Enviando datos:', directorData);
        console.log('URL:', `${BASE_URL}/admin/agregarDirector`);
        
        
        const response = await fetch(`${BASE_URL}/admin/agregarDirector`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(directorData)
        });
        
        console.log('Respuesta del servidor:', response.status, response.statusText);
        
        
        if (response.ok) {
            const result = await response.json();
            console.log('Respuesta exitosa:', result);
            mostrarMensaje(result.mensaje || 'Director agregado correctamente', 'success');
            
            
            directores.push({
                nombre: nombre.trim(),
                biografia: biografia.trim()
            });
            
            
            actualizarListaDirectores();
            
            
            limpiarFormulario();
            
        } else {
            const errorText = await response.text();
            console.error('Error del servidor:', response.status, errorText);
            
            try {
                const errorData = JSON.parse(errorText);
                mostrarMensaje(errorData.error || 'Error al agregar el director', 'error');
            } catch (e) {
                mostrarMensaje(`Error del servidor: ${response.status} - ${errorText}`, 'error');
            }
        }
        
    } catch (error) {
        console.error('Error detallado:', error);
        console.error('Tipo de error:', error.constructor.name);
        console.error('Mensaje:', error.message);
        
        if (error.name === 'TypeError' && error.message.includes('fetch')) {
            mostrarMensaje('No se puede conectar con el servidor. Verifique que esté ejecutándose en ' + BASE_URL, 'error');
        } else {
            mostrarMensaje('Error de conexión: ' + error.message, 'error');
        }
    } finally {
        
        const submitButton = document.querySelector('.save-button');
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    }
}


function actualizarListaDirectores() {
    
    const directorsContainer = directorsSection.querySelector('.director-card').parentNode;
    
    
    const existingCards = directorsContainer.querySelectorAll('.director-card:not(.example)');
    existingCards.forEach(card => {
        if (!card.classList.contains('example')) {
            card.remove();
        }
    });
    
    
    const exampleCard = directorsContainer.querySelector('.director-card');
    if (exampleCard) {
        exampleCard.classList.add('example');
    }
    
    
    directores.forEach(director => {
        const directorCard = document.createElement('div');
        directorCard.className = 'director-card';
        directorCard.innerHTML = `
            <div class="director-name">${director.nombre}</div>
            <div class="director-info">${director.biografia.substring(0, 100)}${director.biografia.length > 100 ? '...' : ''}</div>
        `;
        directorsContainer.appendChild(directorCard);
    });
}


async function cargarDirectoresExistentes() {
    try {
        // const response = await fetch(`${BASE_URL}/obtenerDirectores`);
        // if (response.ok) {
        //     const directoresData = await response.json();
        //     directores = directoresData;
        //     actualizarListaDirectores();
        // }
    } catch (error) {
        console.error('Error al cargar directores:', error);
    }
}


directorForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const nombre = directorNameInput.value;
    const biografia = directorBioInput.value;
    
    
    if (!validarFormulario(nombre, biografia)) {
        return;
    }
    
    
    await agregarDirector(nombre, biografia);
});


directorNameInput.addEventListener('input', (e) => {
    
    e.target.value = e.target.value.replace(/\s+/g, ' ');
});

directorBioInput.addEventListener('input', (e) => {
    
    const maxLength = 1000;
    const currentLength = e.target.value.length;
    
    if (currentLength > maxLength) {
        e.target.value = e.target.value.substring(0, maxLength);
    }
});


async function testConexion() {
    try {
        
        const response = await fetch(`${BASE_URL}/admin/agregarDirector`, {
            method: 'OPTIONS',
            headers: {
                'Accept': 'application/json'
            }
        });
        
        console.log('Test de conexión - Status:', response.status);
        
        if (response.status === 200 || response.status === 404) {
            console.log('Conexión con el backend exitosa');
            mostrarMensaje('Conexión con el servidor establecida', 'success');
        } else {
            console.warn('Backend responde pero con error:', response.status);
        }
    } catch (error) {
        console.error('No se pudo conectar con el backend:', error);
        console.error('Detalles del error:', {
            message: error.message,
            name: error.name,
            stack: error.stack
        });
        
        
        mostrarMensaje(`Error de conexión: ${error.message}. Verificar que el servidor esté en ${BASE_URL}`, 'error');
    }
}


document.addEventListener('DOMContentLoaded', () => {
    console.log('Script de agregar director cargado');
    
    
    testConexion();
    
    
    cargarDirectoresExistentes();
    
    
    directorNameInput.focus();
});


function debug(message, data = null) {
    console.log(`[DEBUG] ${message}`, data);
}


window.DirectorManager = {
    agregarDirector,
    limpiarFormulario,
    testConexion,
    debug
};